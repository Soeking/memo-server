package plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.security.MessageDigest

val isLogin = mutableListOf<Pair<String, Boolean>>()

fun Application.configureLogin() {
    routing {
        post("/login") {
            val rec = call.receive<Pair<String, String>>()
            login(rec.first, rec.second).let { call.respondText(it) }
        }

        post("/create") {
            val rec = call.receive<Pair<String, String>>()
            createAccount(rec.first, rec.second).let { call.respond(it) }
        }
    }
}

fun initLoginInfo() {
    readUsers().forEach {
        isLogin.add(Pair(it, false))
    }
}

fun createAccount(user: String, pass: String): Boolean {
    return if (addUser(user, sha256(pass))) {
        isLogin.add(Pair(user, true))
        true
    } else false
}

fun login(user: String, pass: String): String {
    val res = loginCheck(user, sha256(pass))
    return if (res) {
        isLogin.add(Pair(user, true))
        "connected"
    } else "login failed"
}

fun sha256(input: String): String {
    return MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        .fold("") { str, byte -> str + "%02x".format(byte) }
}