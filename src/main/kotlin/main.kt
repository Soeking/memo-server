import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import plugins.*

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8081
    val host = "0.0.0.0"
    embeddedServer(Netty, port, host, module = Application::main).start(wait = true)
}

fun Application.main() {
    firstCheck()
    initLoginInfo()
    configureRouting()
    configureLogin()
    configureSend()
    configureReceive()
}
