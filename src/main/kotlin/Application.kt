import io.ktor.server.engine.*
import io.ktor.server.netty.*
import plugins.configureRouting
import plugins.configureSecurity
import plugins.configureSerialization

fun main() {
    println(System.getenv().keys.sorted())
    val port = System.getenv("PORT").toIntOrNull() ?: 8081
    val host = "0.0.0.0"
    embeddedServer(Netty, port, host) {
        configureRouting()
        configureSerialization()
        configureSecurity()
    }.start(wait = true)
}
