import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import kotlinx.serialization.json.Json
import plugins.*

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8081
    val host = "0.0.0.0"
    embeddedServer(Netty, port, host, module = Application::main).start(wait = true)
}

fun Application.main() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
    firstCheck()
    initLoginInfo()
    configureRouting()
    configureLogin()
    configureSend()
    configureReceive()
}
