package signup

import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.artfelt.artfelt.R
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.editText_email
import kotlinx.android.synthetic.main.activity_signup.editText_password
import kotlinx.android.synthetic.main.activity_signup.textView_signin
import kotlinx.android.synthetic.main.activity_signup.textView_signup
import signin.SignInActivity

class SignUpActivity : AppCompatActivity() {
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
            showLoadingSignUpButton()
        }
    }

    private fun manageOnClickSignInTextView(){
        textView_signin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }


}