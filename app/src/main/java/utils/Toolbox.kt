package utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.artfelt.artfelt.R
import home.HomeActivity
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.HashMap
import kotlin.concurrent.schedule
import kotlin.concurrent.timerTask

class Toolbox {

    companion object {

        fun showSuccessDialog(context: Context, message: String, ) {
            val inflater: LayoutInflater = LayoutInflater.from(context)

            val layout: View = inflater.inflate(
                R.layout.dialog_success,
                null
            )

            val textView = layout.findViewById(R.id.tv_success_message) as TextView

            textView.visibility = View.INVISIBLE

            message?.let {
                textView.visibility = View.VISIBLE
                textView.text = it
            }

            val toast = Toast(context.applicationContext)

            layout.setBackgroundColor(
                ContextCompat.getColor(
                    context.applicationContext,
                    R.color.white
                )
            )


            toast.setGravity(Gravity.BOTTOM or Gravity.START or Gravity.FILL_HORIZONTAL, 0, 0)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout

            toast.show()

        }


        fun showErrorDialog(context: Context, message: String, ) {
            val inflater: LayoutInflater = LayoutInflater.from(context)

            val layout: View = inflater.inflate(
                R.layout.dialog_error,
                null
            )

            val textView = layout.findViewById(R.id.tv_error_message) as TextView

            textView.visibility = View.INVISIBLE

            message?.let {
                textView.visibility = View.VISIBLE
                textView.text = it
            }

            val toast = Toast(context.applicationContext)

            layout.setBackgroundColor(
                ContextCompat.getColor(
                    context.applicationContext,
                    R.color.white
                )
            )


            toast.setGravity(Gravity.BOTTOM or Gravity.START or Gravity.FILL_HORIZONTAL, 0, 0)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout

            toast.show()

        }
    }

}


const val EXTRA_HASHMAP = "extra-hashmap"

fun String.isEmail(): Boolean {
    val pattern: Pattern = Patterns.EMAIL_ADDRESS
    val matcher: Matcher = pattern.matcher(this)

    return matcher.find()
}

fun String.containsSpecialCharacters(): Boolean {
    val pattern: Pattern = Pattern.compile("[^a-zA-Z0-9_-]")
    val matcher: Matcher = pattern.matcher(this)

    return matcher.find()
}

fun String.isAlphaOnly(): Boolean {
    val pattern: Pattern = Pattern.compile("[^a-zA-Z]")
    val matcher: Matcher = pattern.matcher(this)

    return !matcher.find()
}




fun Activity.navigateTo(activity: Activity, finish: Boolean) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)

    if (finish) {
        finish()
    }
}


fun Activity.navigateTo(activity: Activity, data: HashMap<String, Any>? = null, finish: Boolean) {
    val intent = Intent(this, activity::class.java)
    data?.let {
        val args = Bundle()
        args.putSerializable(EXTRA_HASHMAP, data)
        intent.putExtras(args)
    }
    startActivity(intent)

    if (finish) {
        finish()
    }
}


fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}


fun Intent.getExtraPassedData(): HashMap<String, Any> {
    return getSerializableExtra(EXTRA_HASHMAP) as HashMap<String, Any>
}






