package com.example.reparatic.ui

import java.security.MessageDigest

fun encriptarMD5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(input.toByteArray())
    return digest.joinToString("") { "%02x".format(it) }
}

