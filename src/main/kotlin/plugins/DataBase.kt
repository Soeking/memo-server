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

fun addUser(addName: String, addPass: String) {
    transaction {
        User.insert {
            it[name] = addName
            it[pass] = addPass
            it[version] = 0
        }
    }
}

fun addData(userName: String, dataType: String, data: String) {
    transaction {
        val id = User.slice(User.id).select { User.name eq userName }.first()[User.id]
        Data.insert {
            it[userId] = id
            it[type] = dataType
            when (dataType) {
                "text" -> it[text] = data
                "image" -> it[image] = data
            }
        }
    }
}
