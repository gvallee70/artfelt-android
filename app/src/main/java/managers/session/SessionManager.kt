package managers.session

import android.content.Context
import android.content.SharedPreferences
import com.artfelt.artfelt.R

/**
 * Session manager to save and fetch data from SharedPreferences
 */

class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.TITLE_APP_NAME), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user-token"
    }


    fun saveAuthToken(token: String) {
        prefs.edit()
             .putString(USER_TOKEN, token)
             .apply()
    }

    fun removeAuthToken() {
        prefs.edit()
             .remove(USER_TOKEN)
             .apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun userIsLogged(): Boolean {
        return !this.fetchAuthToken().isNullOrEmpty()
    }
}