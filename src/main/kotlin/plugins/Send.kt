package plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.configureSend() {
    routing {
        get("/getData") {
            val param = call.receiveParameters()
            sendNewData(param).let {
                call.respond(NewData(it))
            }
        }
    }
}

fun sendNewData(param: Parameters): List<Pair<String, String>> {
    val name = param["user"]
    val version = param["version"]?.toIntOrNull()
    if (name == null || version == null) return emptyList()
    return if (isLogin[name] == true) getData(name, version)
    else emptyList()
}

@Serializable
data class NewData(val data: List<Pair<String, String>>)
