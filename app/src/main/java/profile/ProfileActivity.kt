package profile

import android.content.DialogInterface
import android.graphics.Paint
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import api.ArtfeltClient
import api.models.auth.changepassword.ChangePasswordRequest
import api.models.user.User
import com.artfelt.artfelt.R
import com.squareup.picasso.Picasso
import common.HeaderDelegate
import common.HeaderView
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.dialog_change_password.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import signin.SignInActivity
import signup.SignUpActivity
import utils.*

class ProfileActivity: AppCompatActivity(), HeaderDelegate {
    private lateinit var changePasswordDialog: AlertDialog
    private lateinit var mOldPasswordEditText: EditText
    private lateinit var mNewPasswordEditText: EditText
    private lateinit var mConfirmationPasswordEditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        this.supportActionBar!!.hide()
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        initHeader()
        initProfilePicture()
        initModifyProfileForm()
        initBecomeArtistButton()
    }

    private fun initHeader() {
        HeaderView(this, block_header_profile, true, this)
    }

    private fun initProfilePicture() {
        imageView_profile_profile_pic.clipToOutline = true

        if (User.infos?.avatarUrl.isNullOrEmpty()) {
            imageView_profile_profile_pic.setImageResource(R.drawable.ic_add_user_picture)
        } else {
            Picasso.get().load(User.infos?.avatarUrl).into(imageView_profile_profile_pic)
        }
    }



    private fun initModifyProfileForm() {
        initPersonalInformationsForm()
        initConnexionInformationsForm()
        initSaveButton()
    }

    private fun initPersonalInformationsForm() {
        initPersonalInformationsTextView()
        initFirstNameEditText()
        initLastNameEditText()
        initAddressEditText()
        initChangePasswordButton()

        manageOnClickChangePasswordButton()
    }

    private fun initConnexionInformationsForm() {
        initConnexionInformationsTextView()
        initUsernameEditText()
        initEmailEditText()
    }

    private fun initPersonalInformationsTextView() {
        textView_profile_info_personnal.text = getString(R.string.LABEL_PERSONAL_INFORMATIONS)
        textView_profile_info_personnal.textSize = 18f
    }

    private fun initFirstNameEditText() {
        editText_profile_first_name.hint = getString(R.string.LABEL_FIRST_NAME)
        editText_profile_first_name.textSize = 16f

        User.infos?.firstName.let {
            editText_profile_first_name.setText(it)
        }
    }

    private fun initLastNameEditText() {
        editText_profile_last_name.hint = getString(R.string.LABEL_LAST_NAME)
        editText_profile_last_name.textSize = 16f

        User.infos?.lastName.let {
            editText_profile_last_name.setText(it)
        }
    }


    private fun initAddressEditText() {
        editText_profile_address_street.hint = getString(R.string.LABEL_ADDRESS_STREET)
        editText_profile_address_street.textSize = 16f

        User.infos?.street.let {
            editText_profile_address_street.setText(it)
        }

        editText_profile_address_zipcode.hint = getString(R.string.LABEL_ADDRESS_ZIPCODE)
        editText_profile_address_zipcode.textSize = 16f

        User.infos?.zipCode.let {
            editText_profile_address_zipcode.setText(it)
        }

        editText_profile_address_city.hint = getString(R.string.LABEL_ADDRESS_CITY)
        editText_profile_address_city.textSize = 16f

        User.infos?.city.let {
            editText_profile_address_city.setText(it)
        }
    }


    private fun initConnexionInformationsTextView() {
        textView_profile_info_connexion.text = getString(R.string.LABEL_CONNECTION_INFORMATIONS)
        textView_profile_info_connexion.textSize = 18f
    }

    private fun initUsernameEditText() {
        editText_profile_username.hint = getString(R.string.LABEL_USERNAME)
        editText_profile_username.textSize = 16f
        editText_profile_username.isEnabled = false

        User.infos?.username.let {
            editText_profile_username.setText(it)
        }
    }

    private fun initEmailEditText() {
        editText_profile_email.hint = getString(R.string.LABEL_AUTHENTICATION_EMAIL)
        editText_profile_email.textSize = 16f

        User.infos?.email.let {
            editText_profile_email.setText(it)
        }
    }


    private fun initChangePasswordButton() {
        button_profile_password.hint = getString(R.string.ACTION_CHANGE_PASSWORD)
        button_profile_password.textSize = 16f
    }


    private fun initSaveButton() {
        initSaveTextView()
        hideSaveProgressBar()
        manageOnClickSaveButton()
    }

    private fun initBecomeArtistButton() {
        textView_wanna_become_artist.text = getString(R.string.LABEL_WANNA_BECOME_ARTIST)

        textView_become_artist.text = getString(R.string.ACTION_BECOME_ARTIST)
        textView_become_artist.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        manageOnClickBecomeArtistTextView()

    }

    private fun initSaveTextView(){
        textView_profile_save.text = getString(R.string.ACTION_SAVE)
        textView_profile_save.textSize = 16f
        textView_profile_save.isAllCaps = true
        showSaveTextView()
    }


    private fun showLoadingSaveButton(){
        showSaveProgressBar()
        hideSaveTextView()
    }

    private fun showSaveProgressBar() {
        progressBar_profile_save.show()
    }

    private fun hideSaveProgressBar() {
        progressBar_profile_save.hide()
    }

    private fun showSaveTextView() {
        textView_profile_save.show()
    }

    private fun hideSaveTextView() {
        textView_profile_save.hide()
    }


    private fun formIsValid(): Boolean {
        return checkEmptyFields() && checkErrorFields()
    }



    private fun checkEmptyFields(): Boolean {
        if ("${editText_profile_first_name.text}".isEmpty()) {
            editText_profile_first_name.error = getString(R.string.TEXT_FIRST_NAME_EMPTY)
            return false
        }

        if ("${editText_profile_last_name.text}".isEmpty()) {
            editText_profile_last_name.error = getString(R.string.TEXT_LAST_NAME_EMPTY)
            return false
        }

        if ("${editText_profile_address_street.text}".isEmpty()) {
            editText_profile_address_street.error = getString(R.string.TEXT_ADDRESS_STREET_EMPTY)
            return false
        }

        if ("${editText_profile_address_zipcode.text}".isEmpty()) {
            editText_profile_address_zipcode.error = getString(R.string.TEXT_ADDRESS_ZIPCODE_EMPTY)
            return false
        }

        if ("${editText_profile_address_city.text}".isEmpty()) {
            editText_profile_address_city.error = getString(R.string.TEXT_ADDRESS_CITY_EMPTY)
            return false
        }


        if ("${editText_profile_email.text}".isEmpty()) {
            editText_profile_email.error = getString(R.string.TEXT_EMAIL_EMPTY)
            return false
        }

        return true
    }



    private fun checkErrorFields(): Boolean {
        /*    if (!"${editText_first_name.text}".containsAlphaOnly()) {
                editText_first_name.error = getString(R.string.TEXT_FIRST_NAME_FORMAT_ERROR)
                return false
            }

            if (!"${editText_last_name.text}".containsAlphaOnly()) {
                editText_last_name.error = getString(R.string.TEXT_LAST_NAME_FORMAT_ERROR)
                return false
            }*/


        if (!"${editText_profile_email.text}".isEmail()) {
            editText_profile_email.error = getString(R.string.TEXT_EMAIL_ERROR)
            return false
        }

        return true
    }

    private fun checkChangePasswordForm(): Boolean {
        mOldPasswordEditText = changePasswordDialog.findViewById<EditText>(R.id.editText_old_password)!!
        mNewPasswordEditText = changePasswordDialog.findViewById<EditText>(R.id.editText_new_password)!!
        mConfirmationPasswordEditText = changePasswordDialog.findViewById<EditText>(R.id.editText_new_password_confirmation)!!

        if ("${mOldPasswordEditText?.text}".isEmpty()) {
            mOldPasswordEditText?.error = getString(R.string.TEXT_PASSWORD_EMPTY)
            return false
        }

        if ("${mNewPasswordEditText?.text}".isEmpty()) {
            mNewPasswordEditText?.error = getString(R.string.TEXT_PASSWORD_EMPTY)
            return false
        }

        if ("${mConfirmationPasswordEditText?.text}".isEmpty()) {
            mConfirmationPasswordEditText?.error = getString(R.string.TEXT_PASSWORD_EMPTY)
            return false
        }

        if ("${mNewPasswordEditText?.text}".length < 7 || "${mNewPasswordEditText?.text}".length > 20) {
            mNewPasswordEditText?.error = getString(R.string.TEXT_PASSWORD_LENGTH_ERROR)
            return false
        }


        if (("${mConfirmationPasswordEditText?.text}" != ("${mNewPasswordEditText?.text}"))) {
            mConfirmationPasswordEditText?.error = getString(R.string.TEXT_PASSWORD_CONFIRMATION_ERROR)
            return false
        }

        return true
    }


    private fun changePasswordFormIsValid(): Boolean {
        return checkChangePasswordForm()
    }


    private fun manageOnClickChangePasswordButton() {
        button_profile_password.setOnClickListener {
            changePasswordDialog = AlertDialog.Builder(this)
                .setView(R.layout.dialog_change_password)
                .setPositiveButton(R.string.ACTION_SAVE, null)
                .setNegativeButton(R.string.ACTION_CANCEL, null)
                .show()

            val mButtonSave = changePasswordDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            val mOldPasswordTitle = changePasswordDialog.findViewById<TextView>(R.id.textView_old_password_title)
            val mNewPasswordTitle = changePasswordDialog.findViewById<TextView>(R.id.textView_new_password_title)
            val mConfirmationPasswordTitle = changePasswordDialog.findViewById<TextView>(R.id.textView_new_password_confirmation_title)

            mOldPasswordTitle?.text = getString(R.string.LABEL_OLD_PASSWORD)
            mNewPasswordTitle?.text = getString(R.string.LABEL_NEW_PASSWORD)
            mConfirmationPasswordTitle?.text = getString(R.string.LABEL_CONFIRMATION_PASSWORD)


            mButtonSave.setOnClickListener {
               if (changePasswordFormIsValid()) {
                   changePasswordAPICall()
               }

            }
        }
    }


    private fun manageOnClickSaveButton() {
        textView_profile_save.setOnClickListener {
            hideKeyboard()

            if (formIsValid()) {
                //signUpAPICall()
            }
        }
    }


    private fun manageOnClickBecomeArtistTextView(){
        textView_become_artist.setOnClickListener {
            navigateTo(SignInActivity(), false)
        }
    }




    private fun changePasswordAPICall() {
        val changePasswordRequest = ChangePasswordRequest(
            oldPassword = mOldPasswordEditText.text.toString(),
            newPassword = mNewPasswordEditText.text.toString()
        )

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val changePasswordResponse = ArtfeltClient().getApiService(this@ProfileActivity).changePassword(changePasswordRequest)

                if (changePasswordResponse.isSuccessful) {
                    Toolbox.showSuccessDialog(this@ProfileActivity, getString(R.string.TEXT_PASSWORD_CHANGED_SUCCESS))
                    changePasswordDialog.dismiss()
                } else if (changePasswordResponse.code() == 401) {
                    Toolbox.showErrorDialog(this@ProfileActivity, getString(R.string.TEXT_OLD_PASSWORD_ERROR))
                }
            } catch (e: Exception) {
                println(e.message)
                Toolbox.showErrorDialog(this@ProfileActivity, getString(R.string.TEXT_CHANGE_PASSWORD_API_ERROR))
            }
        }
    }


    override fun onClickHeaderLeftIcon() {
        finish()
    }

}