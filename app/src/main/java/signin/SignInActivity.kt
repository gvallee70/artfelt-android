package signin

import android.content.DialogInterface
import android.graphics.Paint
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
import api.models.auth.resetpassword.AskResetPasswordRequest
import api.models.auth.resetpassword.ResetPasswordRequest
import api.models.auth.signin.SignInRequest
import managers.session.SessionManager
import signup.SignUpActivity
import splash.SplashActivity
import utils.*
import java.util.*

class SignInActivity : AppCompatActivity() {
    private lateinit var resetPasswordRequestDialog: AlertDialog
    private lateinit var mResetPasswordRequestMailEditText: EditText

    private lateinit var resetPasswordDialog: AlertDialog
    private lateinit var mResetPasswordMailEditText: EditText
    private lateinit var mResetPasswordTokenEditText: EditText
    private lateinit var mResetPasswordNewPasswordEditText: EditText
    private lateinit var mResetPasswordNewPasswordConfirmationEditText: EditText

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
        initFormButtons()
        initForgotPasswordTextView()
        initCopyrightTextView()

        manageOnClickResetPasswordTextView()
    }

    private fun initSignInForm() {
        initUsernameEditText()
        initPasswordEditText()
    }

    private fun initFormButtons() {
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
        button_signup.setBackgroundResource(R.drawable.background_primary_radius_white)
        textView_signup.text = getString(R.string.ACTION_SIGN_UP)
        textView_signup.textSize = 16f
        textView_signup.isAllCaps = true
    }


    private fun initForgotPasswordTextView(){
        textView_forgot_password.text = getString(R.string.LABEL_AUTHENTICATION_FORGOT_PASSWORD)
        textView_reset_password.text = getString(R.string.ACTION_AUTHENTICATION_RESET_PASSWORD)
        textView_reset_password.paintFlags = Paint.UNDERLINE_TEXT_FLAG

    }

    private fun initCopyrightTextView() {
        textView_copyright.text = getString(R.string.LABEL_COPYRIGHT).format(Calendar.getInstance().get(Calendar.YEAR))
        textView_copyright.textSize = 12f
    }


    private fun initResetPasswordRequest() {
        resetPasswordRequestDialog = AlertDialog.Builder(this)
                .setView(R.layout.dialog_reset_password_request)
                .setPositiveButton(R.string.ACTION_SEND, null)
                .setNegativeButton(R.string.ACTION_CANCEL, null)
                .show()

        val mButtonSend = resetPasswordRequestDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        val mDialogTitle = resetPasswordRequestDialog.findViewById<TextView>(R.id.textView_reset_password_request_dialog_title)
        val mMailTitle = resetPasswordRequestDialog.findViewById<TextView>(R.id.textView_reset_password_request_mail)
        mResetPasswordRequestMailEditText = resetPasswordRequestDialog.findViewById(R.id.editText_reset_password_request_mail)!!

        mDialogTitle?.text = getString(R.string.LABEL_RESET_PASSWORD)
        mMailTitle?.text = getString(R.string.LABEL_YOUR_EMAIL)

        mButtonSend.setOnClickListener {
            if (mResetPasswordRequestMailEditText.text.toString().isEmail()) {
                resetPasswordRequestAPICall()
            } else {
                mResetPasswordRequestMailEditText.error = getString(R.string.TEXT_EMAIL_ERROR)
            }

        }
    }


    private fun initResetPasswordDialog() {
        resetPasswordDialog = AlertDialog.Builder(this)
                .setView(R.layout.dialog_reset_password)
                .setPositiveButton(R.string.ACTION_SEND, null)
                .setNegativeButton(R.string.ACTION_CANCEL, null)
                .show()

        val mButtonSend = resetPasswordDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        val mMailTitle = resetPasswordDialog.findViewById<TextView>(R.id.textView_reset_password_mail)
        val mTokenTitle = resetPasswordDialog.findViewById<TextView>(R.id.textView_reset_password_token)
        val mPasswordTitle = resetPasswordDialog.findViewById<TextView>(R.id.textView_reset_password_password)
        val mPasswordConfirmationTitle = resetPasswordDialog.findViewById<TextView>(R.id.textView_reset_password_password_confirmation)
        val mCodeDurationTextView = resetPasswordDialog.findViewById<TextView>(R.id.textView_reset_password_duration)
        mResetPasswordMailEditText = resetPasswordDialog.findViewById(R.id.editText_reset_password_mail)!!

        mMailTitle?.text = getString(R.string.LABEL_YOUR_EMAIL)
        mTokenTitle?.text = getString(R.string.LABEL_MAIL_CODE_RECEIVED)
        mPasswordTitle?.text = getString(R.string.LABEL_NEW_PASSWORD)
        mPasswordConfirmationTitle?.text = getString(R.string.LABEL_CONFIRMATION_PASSWORD)
        mCodeDurationTextView?.text = getString(R.string.TEXT_RESET_PASSWORD_REQUEST_SUCCESS)

        mResetPasswordMailEditText.text = mResetPasswordRequestMailEditText.text

        mButtonSend.setOnClickListener {
            if (resetPasswordFormIsValid()) {
                resetPasswordAPICall()
            }
        }
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
            navigateTo(SignUpActivity())
        }
    }


    private fun manageOnClickResetPasswordTextView(){
        textView_reset_password.setOnClickListener {
            initResetPasswordRequest()
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


    private fun resetPasswordFormIsValid(): Boolean {
        mResetPasswordTokenEditText = resetPasswordDialog.findViewById(R.id.editText_reset_password_token)!!
        mResetPasswordNewPasswordEditText = resetPasswordDialog.findViewById(R.id.editText_reset_password_password)!!
        mResetPasswordNewPasswordConfirmationEditText = resetPasswordDialog.findViewById(R.id.editText_reset_password_password_confirmation)!!

        return checkResetPasswordFormErrorFields()
    }



    private fun checkResetPasswordFormErrorFields(): Boolean {
        if ("${mResetPasswordTokenEditText.text}".isEmpty()) {
            mResetPasswordTokenEditText.error = getString(R.string.TEXT_RESET_PASSWORD_CODE_EMPTY)
            return false
        }

        if ("${mResetPasswordNewPasswordEditText.text}".length < 7 || "${mResetPasswordNewPasswordEditText.text}".length > 20) {
            mResetPasswordNewPasswordEditText.error = getString(R.string.TEXT_PASSWORD_LENGTH_ERROR)
            return false
        }

        if ("${mResetPasswordNewPasswordConfirmationEditText.text}".length < 7 || "${mResetPasswordNewPasswordConfirmationEditText.text}".length > 20) {
            mResetPasswordNewPasswordConfirmationEditText.error = getString(R.string.TEXT_PASSWORD_LENGTH_ERROR)
            return false
        }

        if (("${mResetPasswordNewPasswordConfirmationEditText?.text}" != ("${mResetPasswordNewPasswordEditText?.text}"))) {
            mResetPasswordNewPasswordConfirmationEditText?.error = getString(R.string.TEXT_PASSWORD_CONFIRMATION_ERROR)
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



    private fun resetPasswordRequestAPICall() {
        var askResetPasswordRequest = AskResetPasswordRequest(
                email = "${mResetPasswordRequestMailEditText.text}"
        )
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val askResetPasswordResponse = ArtfeltClient().getApiService(this@SignInActivity).askResetPassword(askResetPasswordRequest)

                if (askResetPasswordResponse.isSuccessful) {
                    Toolbox.showSuccessDialog(this@SignInActivity, getString(R.string.TEXT_RESET_PASSWORD_REQUEST_SUCCESS))
                    resetPasswordRequestDialog.dismiss()
                    initResetPasswordDialog()
                } else if (askResetPasswordResponse.code() == 404) {
                    mResetPasswordRequestMailEditText.error = getString(R.string.TEXT_EMAIL_DONT_EXIST)
                }
            } catch (e: Exception) {
                println(e.message)
                Toolbox.showErrorDialog(this@SignInActivity, getString(R.string.TEXT_RESET_PASSWORD_API_ERROR))

            }
        }
    }




    private fun resetPasswordAPICall() {
        var resetPasswordRequest = ResetPasswordRequest(
                email = "${mResetPasswordMailEditText.text}",
                confirmationCode = "${mResetPasswordTokenEditText.text}",
                password = "${mResetPasswordNewPasswordEditText.text}",
                passwordConfirmation = "${mResetPasswordNewPasswordConfirmationEditText.text}"
        )
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val resetPasswordResponse = ArtfeltClient().getApiService(this@SignInActivity).resetPassword(resetPasswordRequest)

                if (resetPasswordResponse.isSuccessful) {
                    resetPasswordResponse.body()?.let {
                        Toolbox.showSuccessDialog(this@SignInActivity, getString(R.string.TEXT_PASSWORD_CHANGED_SUCCESS))
                        resetPasswordDialog.dismiss()
                        editText_username.setText(it.username)
                        editText_password.setText(mResetPasswordNewPasswordEditText.text)
                    }
                } else if (resetPasswordResponse.code() == 400) {
                    mResetPasswordTokenEditText.error = getString(R.string.TEXT_RESET_PASSWORD_WRONG_CONFIRMATION_CODE)
                }
            } catch (e: Exception) {
                println(e.message)
                Toolbox.showErrorDialog(this@SignInActivity, getString(R.string.TEXT_RESET_PASSWORD_API_ERROR))

            }
        }
    }

}