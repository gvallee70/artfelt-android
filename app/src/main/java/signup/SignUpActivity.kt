package signup

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import api.ArtfeltClient
import api.models.auth.signup.SignUpRequest
import com.artfelt.artfelt.R
import home.HomeActivity
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.button_signup
import kotlinx.android.synthetic.main.activity_signup.editText_password
import kotlinx.android.synthetic.main.activity_signup.editText_username
import kotlinx.android.synthetic.main.activity_signup.textView_signin
import kotlinx.android.synthetic.main.activity_signup.textView_signup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import signin.SignInActivity
import utils.*

class SignUpActivity : AppCompatActivity() {

    companion object {
        private const val NEW_USERNAME = "new-username"
        private const val NEW_PASSWORD = "new-password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        this.supportActionBar!!.hide()
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        initSignUpForm()
        initSignInButton()
    }


    private fun initSignUpForm() {
        initPersonalInformationsForm()
        initConnexionInformationsForm()
        initSignUpButton()
    }

    private fun initPersonalInformationsForm() {
        initPersonalInformationsTextView()
        initFirstNameEditText()
        initLastNameEditText()
        initAddressEditText()
    }

    private fun initConnexionInformationsForm() {
        initConnexionInformationsTextView()
        initUsernameEditText()
        initEmailEditText()
        initPasswordEditText()
    }

    private fun initPersonalInformationsTextView() {
        textView_info_personnal.text = getString(R.string.LABEL_PERSONAL_INFORMATIONS)
        textView_info_personnal.textSize = 18f
    }

    private fun initFirstNameEditText() {
        editText_first_name.hint = getString(R.string.LABEL_FIRST_NAME)
        editText_first_name.textSize = 16f
    }

    private fun initLastNameEditText() {
        editText_last_name.hint = getString(R.string.LABEL_LAST_NAME)
        editText_last_name.textSize = 16f
    }


    private fun initAddressEditText() {
        editText_address_street.hint = getString(R.string.LABEL_ADDRESS_STREET)
        editText_address_street.textSize = 16f

        editText_address_zipcode.hint = getString(R.string.LABEL_ADDRESS_ZIPCODE)
        editText_address_zipcode.textSize = 16f

        editText_address_city.hint = getString(R.string.LABEL_ADDRESS_CITY)
        editText_address_city.textSize = 16f
    }


    private fun initConnexionInformationsTextView() {
        textView_info_connexion.text = getString(R.string.LABEL_CONNECTION_INFORMATIONS)
        textView_info_connexion.textSize = 18f
    }

    private fun initUsernameEditText() {
        editText_username.hint = getString(R.string.LABEL_USERNAME)
        editText_username.textSize = 16f
    }

    private fun initEmailEditText() {
        editText_email.hint = getString(R.string.LABEL_AUTHENTICATION_EMAIL)
        editText_email.textSize = 16f
    }

    private fun initPasswordEditText() {
        editText_password.hint = getString(R.string.LABEL_AUTHENTICATION_PASSWORD)
        editText_password.textSize = 16f
    }


    private fun initSignUpButton() {
        initSignUpTextView()
        hideSignUpProgressBar()
        manageOnClickSignUpButton()
    }

    private fun initSignInButton() {
        textView_already_account.text = getString(R.string.LABEL_ALREADY_ACCOUNT)

        textView_signin.text = getString(R.string.ACTION_SIGN_IN)
        textView_signin.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        manageOnClickSignInTextView()

    }

    private fun initSignUpTextView(){
        textView_signup.text = getString(R.string.ACTION_SIGN_UP)
        textView_signup.textSize = 16f
        textView_signup.isAllCaps = true
        showSignUpTextView()
    }


    private fun showLoadingSignUpButton(){
        showSignUpProgressBar()
        hideSignUpTextView()
    }

    private fun showSignUpProgressBar() {
        progressBar_signup.visibility = View.VISIBLE
    }

    private fun hideSignUpProgressBar() {
        progressBar_signup.visibility = View.GONE
    }

    private fun showSignUpTextView() {
        textView_signup.visibility = View.VISIBLE
    }

    private fun hideSignUpTextView() {
        textView_signup.visibility = View.GONE
    }



    private fun manageOnClickSignUpButton() {
        button_signup.setOnClickListener {
            if (signUpFormIsValid()) {
                hideKeyboard()
                signUpAPICall()
            }
        }
    }

    private fun manageOnClickSignInTextView(){
        textView_signin.setOnClickListener {
            navigateTo(SignInActivity(), false)
        }
    }


    private fun signUpFormIsValid(): Boolean {
        return checkEmptyFields() && checkErrorFields()
    }


    private fun checkEmptyFields(): Boolean {
        if ("${editText_first_name.text}".isEmpty()) {
            editText_first_name.error = getString(R.string.TEXT_EMPTY_FIRST_NAME)
            return false
        }

        if ("${editText_last_name.text}".isEmpty()) {
            editText_last_name.error = getString(R.string.TEXT_EMPTY_LAST_NAME)
            return false
        }

        if ("${editText_address_street.text}".isEmpty()) {
            editText_address_street.error = getString(R.string.TEXT_EMPTY_ADDRESS_STREET)
            return false
        }

        if ("${editText_address_zipcode.text}".isEmpty()) {
            editText_address_zipcode.error = getString(R.string.TEXT_EMPTY_ADDRESS_ZIPCODE)
            return false
        }

        if ("${editText_address_city.text}".isEmpty()) {
            editText_address_city.error = getString(R.string.TEXT_EMPTY_ADDRESS_CITY)
            return false
        }

        if ("${editText_username.text}".isEmpty()) {
            editText_username.error = getString(R.string.TEXT_EMPTY_USERNAME)
            return false
        }

        if ("${editText_email.text}".isEmpty()) {
            editText_email.error = getString(R.string.TEXT_EMPTY_EMAIL)
            return false
        }

        if ("${editText_password.text}".isEmpty()) {
            editText_password.error = getString(R.string.TEXT_EMPTY_PASSWORD)
            return false
        }

        return true
    }



    private fun checkErrorFields(): Boolean {
        if ("${editText_username.text}".containsSpecialCharacters()) {
            editText_username.error = getString(R.string.TEXT_USERNAME_ERROR)
            return false
        }

        if (!"${editText_email.text}".isEmail()) {
            editText_email.error = getString(R.string.TEXT_EMAIL_ERROR)
            return false
        }

        return true
    }



    private fun signUpAPICall() {
        val signUpRequest = SignUpRequest(
            firstName = "${editText_first_name.text}",
            lastName = "${editText_last_name.text}",
            street = "${editText_address_street.text}",
            zipCode = "${editText_address_zipcode.text}",
            city = "${editText_address_city.text}",
            username = "${editText_username.text}",
            email = "${editText_email.text}",
            password = "${editText_password.text}",
        )

        GlobalScope.launch(Dispatchers.Main) {
            try {
                showLoadingSignUpButton()

                val signUpResponse = ArtfeltClient.apiService.signUp(signUpRequest)

                if (signUpResponse.isSuccessful && signUpResponse.body() != null) {
                    Toolbox.showSuccessDialog(this@SignUpActivity, getString(R.string.TEXT_SUCCESS_SIGN_UP))

                    signUpResponse.body()?.let {
                        val data = HashMap<String, Any>()
                        data[NEW_USERNAME] = it.user.username
                        data[NEW_PASSWORD] = editText_password.text.toString()
                        navigateTo(SignInActivity(), data, true)
                    }
                } else {
                    initSignUpButton()
                }

            } catch (e: Exception) {
                initSignUpButton()
                println(e.message)
                Toolbox.showErrorDialog(this@SignUpActivity, getString(R.string.TEXT_ERROR_SIGN_UP))
            }
        }
    }

}