package signin

import android.os.Bundle
import android.view.View
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
import api.models.auth.signin.SignInRequest
import managers.session.SessionManager
import signup.SignUpActivity
import splash.SplashActivity
import utils.*

class SignInActivity : AppCompatActivity() {

    companion object {
        private const val NEW_USERNAME = "new-username"
        private const val NEW_PASSWORD = "new-password"
    }


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

        if (intent.hasExtra(EXTRA_HASHMAP)) {

            val data = intent.getExtraPassedData()
            editText_username.setText(data[NEW_USERNAME].toString())
        }
    }

    private fun initPasswordEditText() {
        editText_password.hint = getString(R.string.LABEL_AUTHENTICATION_PASSWORD)
        editText_password.textSize = 16f

        if (intent.hasExtra(EXTRA_HASHMAP)) {
            val data = intent.getExtraPassedData()
            editText_password.setText(data[NEW_PASSWORD].toString())
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
        progressBar_signin.show()
    }

    private fun hideSignInProgressBar() {
        progressBar_signin.hide()
    }

    private fun showSignInTextView() {
        textView_signin.show()
    }

    private fun hideSignInTextView() {
        textView_signin.hide()
    }



    private fun manageOnClickSignInButton() {
        button_signin.setOnClickListener {
            hideKeyboard()

            if (signInFormIsValid()) {
                signInAPICall()
            }
        }
    }


    private fun manageOnClickSignUpButton() {
        button_signup.setOnClickListener {
            navigateTo(SignUpActivity(), false)
        }
    }


    private fun signInFormIsValid(): Boolean {
        if ("${editText_username.text}".isEmpty()) {
            editText_username.error = getString(R.string.TEXT_USERNAME_EMPTY)
            return false
        }

        if ("${editText_password.text}".isEmpty()) {
            editText_password.error = getString(R.string.TEXT_PASSWORD_EMPTY)
            return false
        }

        return true
    }



    private fun signInAPICall() {
        var signInRequest = SignInRequest(
            username = "${editText_username.text}",
            password = "${editText_password.text}"
        )
        GlobalScope.launch(Dispatchers.Main) {
            try {
                showLoadingSignInButton()

                val signInResponse = ArtfeltClient().getApiService(this@SignInActivity).signIn(signInRequest)

                if (signInResponse.isSuccessful && signInResponse.body() != null) {
                    signInResponse.body()?.let {
                        SessionManager(this@SignInActivity).saveAuthToken(it.token!!)
                    }

                    println("${signInResponse.code()} ${signInResponse.message()}")
                    navigateTo(SplashActivity(), true)
                } else {
                    initSignInButton()
                    editText_username.error = getString(R.string.TEXT_SIGNIN_ERROR)
                }

            } catch (e: Exception) {
                initSignInButton()
                println(e.message)
                Toolbox.showErrorDialog(this@SignInActivity, getString(R.string.TEXT_SIGN_IN_API_ERROR))

            }
        }
    }

}