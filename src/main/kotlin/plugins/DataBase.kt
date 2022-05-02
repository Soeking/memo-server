package plugins

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object User : Table() {
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
    val image: Column<String?> = varchar("image", 50).nullable()
}

fun firstCheck() {
    val url = "jdbc:" + System.getenv("DB_URL")
    val user = System.getenv("DB_USER")
    val pass = System.getenv("DB_PASS")
    Database.connect(url, driver = "org.postgresql.Driver", user = user, password = pass)

    transaction {
        SchemaUtils.create(User, Data)
    }
}

fun addUser(addName: String, addPass: String): Boolean {
    User.select { User.name eq addName }.firstOrNull()?.let { return false }
    transaction {
        User.insert {
            it[name] = addName
            it[pass] = addPass
            it[version] = 0
        }
    }
    return true
}

fun addData(userName: String, dataType: String, data: String) {
    transaction {
        val (id, ver) = User.slice(User.id, User.version).select { User.name eq userName }.first()
            .let { it[User.id] to it[User.version] }
        Data.insert {
            it[userId] = id
            it[version] = ver + 1
            it[type] = dataType
            when (dataType) {
                "text" -> it[text] = data
                "image" -> it[image] = data
            }
        }

        User.update({ User.id eq id }) {
            with(SqlExpressionBuilder) {
                it.update(User.version, User.version + 1)
            }
        }
    }
}

fun loginCheck(user: String, pass: String): Boolean {
    val correct = User.slice(User.pass).select { User.name eq user }.firstOrNull()?.let { it[User.pass] }
    return correct == pass
}

fun readUsers(): List<String> {
    val users = mutableListOf<String>()
    transaction {
        val query = User.slice(User.name).selectAll()
        query.forEach { users.add(it[User.name]) }
    }
    return users
}
