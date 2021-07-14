package splash

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import api.ArtfeltClient
import api.models.user.User
import com.artfelt.artfelt.R
import home.HomeActivity
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import managers.session.SessionManager
import signin.SignInActivity
import utils.Toolbox
import utils.navigateTo

class SplashActivity: AppCompatActivity() {
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        this.supportActionBar!!.hide()

        sessionManager = SessionManager(this)

        if (sessionManager.isLogged()) {
            getSelfInfos()
        } else {
            navigateTo(SignInActivity(), true)
        }
    }


    private fun getSelfInfos() {
        var userToken = sessionManager.fetchAuthToken().toString()
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val selfInfosResponse = ArtfeltClient.apiService.getSelfInfos(userToken)

                if (selfInfosResponse.isSuccessful && selfInfosResponse.body() != null) {
                    selfInfosResponse.body()?.let {
                        User.infos = it
                        navigateTo(HomeActivity(), true)
                    }
                } else {
                    navigateTo(SignInActivity(), true)
                }

            } catch (e: Exception) {
                println(e.message)
                Toolbox.showErrorDialog(this@SplashActivity, getString(R.string.TEXT_ERROR_GET_USER_INFOS))
            }
        }
    }


}