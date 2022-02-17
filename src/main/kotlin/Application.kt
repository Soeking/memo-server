import io.ktor.server.engine.*
import io.ktor.server.netty.*
import plugins.configureRouting
import plugins.configureSecurity
import plugins.configureSerialization

fun main() {
    val port = System.getenv("PORT").toIntOrNull() ?: 8080
    val host = "0.0.0.0"
    embeddedServer(Netty, port, host) {
        configureRouting()
        configureSerialization()
        configureSecurity()
    }.start(wait = true)
}
