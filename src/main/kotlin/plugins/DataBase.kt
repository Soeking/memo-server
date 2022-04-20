package plugins

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object User : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 50).uniqueIndex()
    val pass: Column<String> = varchar("pass", 100)
}

object Data : Table() {
    val userId = integer("user_id") references User.id
    val type: Column<String> = varchar("type", 10)
    val text: Column<String?> = varchar("text", 200).nullable()
    val image: Column<String?> = varchar("image", 50).nullable()
}

fun firstCheck() {
    val url = System.getenv("DATABASE_URL")
    Database.connect(url, driver = "org.postgresql.Driver", user = "postgres")

    transaction {
        SchemaUtils.create(User, Data)
    }
}

fun addUser(addName: String, addPass: String) {
    transaction {
        User.insert {
            it[name] = addName
            it[pass] = addPass
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
