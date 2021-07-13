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
import api.models.user.User
import api.models.auth.signin.SignInRequest
import home.HomeActivity
import managers.session.SessionManager
import signup.SignUpActivity

class SignInActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        sessionManager = SessionManager(this)
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

        if (intent.hasExtra("NEW_USERNAME")) {
            editText_username.setText(intent.getStringExtra("NEW_USERNAME"))
        }
    }

    private fun initPasswordEditText() {
        editText_password.hint = getString(R.string.LABEL_AUTHENTICATION_PASSWORD)
        editText_password.textSize = 16f

        if (intent.hasExtra("NEW_PASSWORD")) {
            editText_password.setText(intent.getStringExtra("NEW_PASSWORD"))
        }
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
        var userToken = sessionManager.fetchAuthToken().toString()

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val selfInfosResponse = ArtfeltClient.apiService.getSelfInfos(userToken)

                if (selfInfosResponse.isSuccessful && selfInfosResponse.body() != null) {
                    selfInfosResponse.body()?.let {
                        User.infos = it
                    }
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

    private fun signInAPICall() {
        var signInRequest = SignInRequest(
            username = "${editText_username.text}",
            password = "${editText_password.text}"
        )
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val signInResponse = ArtfeltClient.apiService.signIn(signInRequest)

                showLoadingSignInButton()

                if (signInResponse.isSuccessful && signInResponse.body() != null) {
                    signInResponse.body()?.let {
                        sessionManager.saveAuthToken(it.token!!)
                        println(it.token)
                    }

                    println("${signInResponse.code()} ${signInResponse.message()}")
                    getSelfInfos()
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