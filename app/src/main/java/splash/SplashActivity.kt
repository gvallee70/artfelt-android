package splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        this.supportActionBar!!.hide()

        if (SessionManager(this).userIsLogged()) {
            println("token: ${SessionManager(this).fetchAuthToken()}")
            getSelfInfos()
        } else {
            navigateTo(SignInActivity(), true)
        }
    }


    private fun getSelfInfos() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val selfInfosResponse = ArtfeltClient().getApiService(this@SplashActivity).getSelfInfos()

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
                Toolbox.showErrorDialog(this@SplashActivity, getString(R.string.TEXT_GET_USER_INFO_API_ERROR))
            }
        }
    }


}