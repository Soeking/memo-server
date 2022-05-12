package plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("connected")
        }
        post("/demo") {
            println("demo")
            val rec = call.receiveParameters()
            call.respond(rec["a"].toString())
        }
    }
}
