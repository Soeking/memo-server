import io.ktor.server.engine.*
import io.ktor.server.netty.*
import plugins.configureRouting
import plugins.configureSecurity
import plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT").toInt(), host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
        configureSecurity()
    }.start(wait = true)
}
