package plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureReceive() {
    routing {
        post("/addNew") {
            val rec = call.receiveParameters()
            addNewData(rec["user"].toString(), rec["type"].toString(), rec["data"].toString()).let { call.respond(it) }
        }

        post("/delete") {
            val rec = call.receiveParameters()
            deleteTargetData(rec["user"].toString(), rec["target"].toString()).let { call.respond(it) }
        }
    }
}

fun addNewData(user: String, type: String, data: String): Int {
    return addData(user, type, data)
}

fun deleteTargetData(user: String, target: String) {
    deleteData(user, target.toInt())
}
