package signin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.artfelt.artfelt.R
import kotlinx.android.synthetic.main.activity_signin.*
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
        initEmailEditText()
        initPasswordEditText()
    }

    private fun initButtons() {
        initSignInButton()
        initSignUpButton()
    }

    private fun initEmailEditText() {
        editText_email.hint = getString(R.string.LABEL_AUTHENTICATION_EMAIL)
        editText_email.textSize = 16f
    }

    private fun initPasswordEditText() {
        editText_password.hint = getString(R.string.LABEL_AUTHENTICATION_PASSWORD)
        editText_password.textSize = 16f
    }

    private fun initSignInButton() {
        button_signin.text = getString(R.string.ACTION_SIGN_IN)
    }

    private fun initSignUpButton() {
        button_signup.text = getString(R.string.ACTION_SIGN_UP)
        manageOnClickSignUpButton()
    }

    private fun manageOnClickSignUpButton() {
        button_signup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

}