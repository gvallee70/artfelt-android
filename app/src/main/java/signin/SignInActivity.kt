package signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.artfelt.artfelt.R
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_signin.button_signup
import kotlinx.android.synthetic.main.activity_signin.editText_username
import kotlinx.android.synthetic.main.activity_signin.editText_password
import kotlinx.android.synthetic.main.activity_signin.textView_signin
import kotlinx.android.synthetic.main.activity_signin.textView_signup
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import api.ArtfeltClient
import api.models.User
import api.models.UserBody
import home.HomeActivity
import signup.SignUpActivity

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        this.supportActionBar!!.hide()
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        initSignInForm()
        initButtons()
    }

    private fun initSignInForm() {
        initUsernameEditText()
        initPasswordEditText()
    }

    private fun initButtons() {
        initSignInButton()
        initSignUpButton()
    }

    private fun initUsernameEditText() {
        editText_username.hint = getString(R.string.LABEL_USERNAME)
        editText_username.textSize = 16f
    }

    private fun initPasswordEditText() {
        editText_password.hint = getString(R.string.LABEL_AUTHENTICATION_PASSWORD)
        editText_password.textSize = 16f
    }

    private fun initSignInButton() {
        initSignInTextView()
        hideSignInProgressBar()
        manageOnClickSignInButton()
    }

    private fun initSignUpButton() {
        initSignUpTextView()
        manageOnClickSignUpButton()
    }

    private fun initSignInTextView() {
        textView_signin.text = getString(R.string.ACTION_SIGN_IN)
        textView_signin.textSize = 16f
        textView_signin.isAllCaps = true
        showSignInTextView()

    }

    private fun initSignUpTextView(){
        textView_signup.text = getString(R.string.ACTION_SIGN_UP)
        textView_signup.textSize = 16f
        textView_signup.isAllCaps = true
    }


    private fun showLoadingSignInButton(){
        //textView_signin.text = getString(R.string.LABEL_CONNECTION_IN_PROGRESS)
        showSignInProgressBar()
        hideSignInTextView()
    }

    private fun showSignInProgressBar() {
        progressBar_signin.visibility = View.VISIBLE
    }

    private fun hideSignInProgressBar() {
        progressBar_signin.visibility = View.GONE
    }

    private fun showSignInTextView() {
        textView_signin.visibility = View.VISIBLE
    }

    private fun hideSignInTextView() {
        textView_signin.visibility = View.GONE
    }



    private fun manageOnClickSignInButton() {
        button_signin.setOnClickListener {

            if (signInFormIsValid()) {
                signInAPICall()
            }
        }
    }


    private fun manageOnClickSignUpButton() {
        button_signup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToHomePage() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun signInFormIsValid(): Boolean {
        if ("${editText_username.text}".isEmpty()) {
            editText_username.error = getString(R.string.TEXT_EMPTY_USERNAME)
            return false
        }

        if ("${editText_password.text}".isEmpty()) {
            editText_password.error = getString(R.string.TEXT_EMPTY_PASSWORD)
            return false
        }

        return true
    }


    private fun getSelfInfos() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ArtfeltClient.apiService.getSelfInfos(User.infos!!.token!!)

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    content?.let {
                        User.infos = it
                    }
                    navigateToHomePage()
                } else {
                    initSignInButton()
                    editText_username.error = getString(R.string.TEXT_SIGNIN_ERROR)
                }
                println("${response.code()} ${response.message()} : \n ${User.infos}")


            } catch (e: Exception) {
                initSignInButton()
                println(e.message)
                Toast.makeText(
                    this@SignInActivity,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun signInAPICall() {
        var user = UserBody(
            username = "${editText_username.text}",
            password = "${editText_password.text}"
        )
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ArtfeltClient.apiService.signIn(user)
                showLoadingSignInButton()
                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    content?.let {
                        User.infos = it
                    }
                    println("${response.code()} ${response.message()} : \n ${User.infos!!.token}")
                    //getSelfInfos()
                    initSignInButton()
                    navigateToHomePage()
                } else {
                    initSignInButton()
                    editText_username.error = getString(R.string.TEXT_SIGNIN_ERROR)
                }

            } catch (e: Exception) {
                initSignInButton()
                println(e.message)
                Toast.makeText(
                    this@SignInActivity,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}