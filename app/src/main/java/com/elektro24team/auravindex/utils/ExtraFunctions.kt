package com.elektro24team.auravindex.utils
import java.text.Normalizer

//ignore accents
fun String.normalize(): String {
    return Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("\\p{Mn}+".toRegex(), "")
        .lowercase()
}