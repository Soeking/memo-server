package plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.configureSend() {
    routing {
        post("/getData") {
            val param = call.receiveParameters()
            sendNewData(param).let {
                call.respond(NewData(it, getVersion(param["user"].toString())))
            }
        }
    }
}

fun sendNewData(param: Parameters): List<Triple<String, String, Int>> {
    val name = param["user"]
    val version = param["version"]?.toIntOrNull()
    if (name == null || version == null) return emptyList()
    return if (isLogin[name] == true) getData(name, version)
    else emptyList()
}

@Serializable
data class NewData(val data: List<Triple<String, String, Int>>, val version: Int)
