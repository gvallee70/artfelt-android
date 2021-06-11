package utils

import kotlinx.android.synthetic.main.activity_signup.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class Toolbox {

    companion object {

    }
}

fun String.isEmail(): Boolean {
    if (this.contains("@") && this.contains(".")) {
        return true
    }
    return false
}

fun String.containsSpecialCharacters(): Boolean {
    val pattern: Pattern = Pattern.compile("[^a-zA-Z0-9_-]")
    val matcher: Matcher = pattern.matcher(this)

    return matcher.find()
}