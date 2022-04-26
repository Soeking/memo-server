package plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import java.security.MessageDigest

fun Application.configureLogin(){
    routing {
        get("/login"){
            login()
        }

        get("/create"){
            createAccount()
        }
    }
}

fun createAccount(){

}

fun login(){

}

fun sha256(input: String): String {
    return MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        .fold("") { str, byte -> str + "%02x".format(byte) }
}