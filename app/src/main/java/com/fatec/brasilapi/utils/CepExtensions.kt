package com.fatec.brasilapi.utils

fun String.isValidCep(): Boolean {
    return this.length == 8 && this.all { it.isDigit() }
}

fun String.formatCep(): String {
    return if (this.length == 8) {
        "${this.substring(0, 5)}-${this.substring(5)}"
    } else {
        this
    }
}
