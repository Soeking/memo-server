package plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSend() {
    routing {
        get("/getData") {
            val param = call.request.queryParameters
            sendNewData(param).run {
                forEach {
                    call.respondText(it.first + it.second)
                }
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
