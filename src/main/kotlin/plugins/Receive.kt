package plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureReceive() {
    routing {
        post("/addNew") {
            val rec = call.receive<Triple<String, String, String>>()
            addNewData(rec.first, rec.second, rec.third).let { call.respond(it) }
        }
    }
}

fun addNewData(user: String, type: String, data: String): Int {
    return addData(user, type, data)
}
