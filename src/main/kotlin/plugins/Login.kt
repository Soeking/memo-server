package plugins

import java.security.MessageDigest

fun createAccount(){

}

fun login(){

}

fun sha256(input: String): String {
    return MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        .fold("") { str, byte -> str + "%02x".format(byte) }
}