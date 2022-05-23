package plugins

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 50).uniqueIndex()
    val pass: Column<String> = varchar("pass", 100)
    val version: Column<Int> = integer("version")
}

object Data : Table() {
    val userId: Column<Int> = integer("user_id")
    val version: Column<Int> = integer("version")
    val type: Column<String> = varchar("type", 10)
    val text: Column<String?> = varchar("text", 200).nullable()
    val image: Column<String?> = varchar("image", 100).nullable()
}

fun firstCheck() {
    val url = "jdbc:" + System.getenv("DB_URL")
    val user = System.getenv("DB_USER")
    val pass = System.getenv("DB_PASS")
    Database.connect(url, driver = "org.postgresql.Driver", user = user, password = pass)

    transaction {
        SchemaUtils.create(Users, Data)
    }
}

fun addUser(addName: String, addPass: String): Boolean {
    return transaction {
        if (Users.select { Users.name eq addName }.firstOrNull() == null) {
            Users.insert {
                it[name] = addName
                it[pass] = addPass
                it[version] = 0
            }
            true
        } else false
    }
}

fun addData(userName: String, dataType: String, data: String): Int {
    return transaction {
        val (id, ver) = Users.slice(Users.id, Users.version).select { Users.name eq userName }.first()
            .let { it[Users.id] to it[Users.version] }
        Data.insert {
            it[userId] = id
            it[version] = ver + 1
            it[type] = dataType
            when (dataType) {
                "text" -> it[text] = data
                "image" -> it[image] = data
            }
        }

        Users.update({ Users.id eq id }) {
            with(SqlExpressionBuilder) {
                it.update(Users.version, Users.version + 1)
            }
        }

        ver + 1
    }
}

fun loginCheck(user: String, pass: String): Boolean {
    val correct = transaction {
        Users.slice(Users.pass).select { Users.name eq user }.firstOrNull()?.let { it[Users.pass] }
    }
    return correct == pass
}

fun readUsers(): List<String> {
    val users = mutableListOf<String>()
    transaction {
        val query = Users.slice(Users.name).selectAll()
        query.forEach { users.add(it[Users.name]) }
    }
    return users
}

fun getData(userName: String, versionId: Int): List<Pair<String, String>> {
    val dataList = mutableListOf<Pair<String, String>>()
    transaction {
        val id = Users.slice(Users.id).select { Users.name eq userName }.first()[Users.id]
        Data.slice(Data.type, Data.text, Data.image).select { (Data.userId eq id) and (Data.version greater versionId) }
            .forEach {
                when (it[Data.type]) {
                    "text" -> dataList.add(it[Data.type] to (it[Data.text] ?: ""))
                    "image" -> dataList.add(it[Data.type] to (it[Data.image] ?: ""))
                }
            }
    }
    return dataList
}

fun getVersion(userName: String): Int {
    return transaction {
        Users.slice(Users.version).select { Users.name eq userName }.first()[Users.version]
    }
}

fun deleteData(userName: String, target: Int) {
    transaction {
        val id = Users.slice(Users.id).select { Users.name eq userName }.first()[Users.id]
        Data.deleteWhere { (Data.userId eq id) and (Data.version eq target) }
    }
}
