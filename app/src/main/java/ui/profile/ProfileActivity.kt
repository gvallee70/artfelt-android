package ui.profile

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Paint
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import api.ArtfeltClient
import api.requests.auth.changepassword.ChangePasswordRequest
import api.requests.request.BecomeArtistRequest
import models.user.User
import models.user.role.UserRoleEnum
import com.artfelt.artfelt.R
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.gson.Gson
import com.google.gson.JsonObject
import ui.common.header.HeaderDelegate
import ui.common.headericon.HeaderLeftIconEnum
import ui.common.headericon.HeaderRightIconEnum
import ui.common.header.HeaderView
import ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.dialog_change_password.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import managers.session.SessionManager
import org.json.JSONObject
import org.json.JSONTokener
import ui.profile.textwatcher.CustomProfileTextWatcher
import ui.profile.textwatcher.EditTextWatcherDelegate
import ui.shopcart.ShopcartActivity
import ui.signin.SignInActivity
import utils.*
import utils.transition.TransitionEnum
import java.io.OutputStreamWriter
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class ProfileActivity: AppCompatActivity(), HeaderDelegate, EditTextWatcherDelegate {
    private lateinit var becomeArtistRequestDialog: AlertDialog
    private lateinit var mExplainWhyEditText: EditText

    private lateinit var changePasswordDialog: AlertDialog
    private lateinit var mOldPasswordEditText: EditText
    private lateinit var mNewPasswordEditText: EditText
    private lateinit var mConfirmationPasswordEditText: EditText

    private lateinit var selectedProfilePicture : Bitmap
    private var newProfilePictureUrl: String? = null
    private var hasChangedProfilePicture : Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        this.supportActionBar!!.hide()
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== Activity.RESULT_OK && requestCode== ImagePicker.REQUEST_CODE) {
            selectedProfilePicture = MediaStore.Images.Media.getBitmap(contentResolver, data?.data)
            hasChangedProfilePicture = true
        }
    }




    /*---------------------------------------------------*/
    /* --------------------- VIEWS ----------------------*/
    /*---------------------------------------------------*/

    private fun initView() {
        initHeader()
        initModifyProfileForms()
        initProfilePicture()
        initBecomeArtistButton()
        initLogOutButton()

        manageOnClickLogOutButton()
    }

    private fun initHeader() {
        HeaderView(this, block_header_profile, HeaderLeftIconEnum.CLOSE, HeaderRightIconEnum.SHOPCART, this)
    }

    private fun initProfilePicture() {
        imageView_profile_profile_pic.clipToOutline = true

        if(hasChangedProfilePicture) {
            imageView_profile_profile_pic.setImageBitmap(selectedProfilePicture)
            enableSaveButton()
        } else {
            if (User.info?.avatarUrl.isNullOrEmpty()) {
                imageView_profile_profile_pic.setImageResource(R.drawable.ic_add_user_picture)
            } else {
                imageView_profile_profile_pic.setImageURL(User.info?.avatarUrl!!)
            }
        }

        manageOnClickProfilePicture()
    }



    private fun initModifyProfileForms() {
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
        User.info?.firstName.let {
            editText_profile_first_name.setText(it)
        }
        editText_profile_first_name.hint = getString(R.string.LABEL_FIRST_NAME)
        editText_profile_first_name.textSize = 16f
        editText_profile_first_name.addTextChangedListener(CustomProfileTextWatcher(editText_profile_first_name, editText_profile_first_name.text.toString(), this))

    }

    private fun initLastNameEditText() {
        User.info?.lastName.let {
            editText_profile_last_name.setText(it)
        }
        editText_profile_last_name.hint = getString(R.string.LABEL_LAST_NAME)
        editText_profile_last_name.textSize = 16f
        editText_profile_last_name.addTextChangedListener(CustomProfileTextWatcher(editText_profile_last_name, editText_profile_last_name.text.toString(), this))

    }


    private fun initAddressEditText() {
        User.info?.street.let {
            editText_profile_address_street.setText(it)
        }
        editText_profile_address_street.hint = getString(R.string.LABEL_ADDRESS_STREET)
        editText_profile_address_street.textSize = 16f
        editText_profile_address_street.addTextChangedListener(CustomProfileTextWatcher(editText_profile_address_street, editText_profile_address_street.text.toString(), this))


        User.info?.zipCode.let {
            editText_profile_address_zipcode.setText(it)
        }
        editText_profile_address_zipcode.hint = getString(R.string.LABEL_ADDRESS_ZIPCODE)
        editText_profile_address_zipcode.textSize = 16f
        editText_profile_address_zipcode.addTextChangedListener(CustomProfileTextWatcher(editText_profile_address_zipcode, editText_profile_address_zipcode.text.toString(), this))


        User.info?.city.let {
            editText_profile_address_city.setText(it)
        }
        editText_profile_address_city.hint = getString(R.string.LABEL_ADDRESS_CITY)
        editText_profile_address_city.textSize = 16f
        editText_profile_address_city.addTextChangedListener(CustomProfileTextWatcher(editText_profile_address_city, editText_profile_address_city.text.toString(), this))

    }


    private fun initConnexionInformationsTextView() {
        textView_profile_info_connexion.text = getString(R.string.LABEL_CONNECTION_INFORMATIONS)
        textView_profile_info_connexion.textSize = 18f
    }

    private fun initUsernameEditText() {
        User.info?.username.let {
            editText_profile_username.setText(it)
        }
        editText_profile_username.hint = getString(R.string.LABEL_USERNAME)
        editText_profile_username.textSize = 16f
        editText_profile_username.addTextChangedListener(CustomProfileTextWatcher(editText_profile_username, editText_profile_username.text.toString(), this))

    }

    private fun initEmailEditText() {
        User.info?.email.let {
            editText_profile_email.setText(it)
        }
        editText_profile_email.hint = getString(R.string.LABEL_AUTHENTICATION_EMAIL)
        editText_profile_email.textSize = 16f
        editText_profile_email.addTextChangedListener(CustomProfileTextWatcher(editText_profile_email, editText_profile_email.text.toString(), this))

    }



    private fun initSaveButton() {
        disableSaveButton()
        initSaveTextView()
        hideSaveProgressBar()
        manageOnClickSaveButton()
    }



    private fun initChangePasswordButton() {
        button_profile_password.text = getString(R.string.ACTION_CHANGE_PASSWORD)
        button_profile_password.textSize = 16f
    }




    private fun initBecomeArtistButton() {
        if(User.info?.role == UserRoleEnum.CUSTOMER) {
            textView_wanna_become_artist.show()
            textView_wanna_become_artist.text = getString(R.string.LABEL_WANNA_BECOME_ARTIST)

            button_profile_become_artist.show()
            button_profile_become_artist.text = getString(R.string.ACTION_BECOME_ARTIST)
            button_profile_become_artist.textSize = 16f

            manageOnClickBecomeArtistButton()
        } else {
            textView_wanna_become_artist.hide()
            button_profile_become_artist.hide()
        }
    }


    private fun initLogOutButton() {
        textView_logout.text = getString(R.string.ACTION_LOG_OUT)
        textView_logout.paintFlags = Paint.UNDERLINE_TEXT_FLAG
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



    /*---------------------------------------------------*/
    /* ---------------- FORMS VALIDATOR -----------------*/
    /*---------------------------------------------------*/

    private fun formIsValid(): Boolean {
        return checkEmptyFields() && checkErrorFields()
    }

    private fun changePasswordFormIsValid(): Boolean {
        return checkChangePasswordForm()
    }

    private fun becomeArtistRequestFormIsValid(): Boolean {
        return checkBecomeArtistRequestForm()
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

        if ("${editText_profile_username.text}".isEmpty()) {
            editText_profile_username.error = getString(R.string.TEXT_USERNAME_EMPTY)
            return false
        }

        if ("${editText_profile_email.text}".isEmpty()) {
            editText_profile_email.error = getString(R.string.TEXT_EMAIL_EMPTY)
            return false
        }

        return true
    }



    private fun checkErrorFields(): Boolean {
        if ("${editText_profile_username.text}".containsSpecialCharacters()) {
            editText_profile_username.error = getString(R.string.TEXT_USERNAME_FORMAT_ERROR)
            return false
        }

        if ("${editText_profile_username.text}".length < 7 || "${editText_profile_username.text}".length > 16) {
            editText_username.error = getString(R.string.TEXT_USERNAME_LENGTH_ERROR)
            return false
        }

        if (!"${editText_profile_email.text}".isEmail()) {
            editText_profile_email.error = getString(R.string.TEXT_EMAIL_ERROR)
            return false
        }

        return true
    }



    private fun checkChangePasswordForm(): Boolean {
        mOldPasswordEditText = changePasswordDialog.findViewById(R.id.editText_old_password)!!
        mNewPasswordEditText = changePasswordDialog.findViewById(R.id.editText_new_password)!!
        mConfirmationPasswordEditText = changePasswordDialog.findViewById(R.id.editText_new_password_confirmation)!!

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



    private fun checkBecomeArtistRequestForm(): Boolean {
        mExplainWhyEditText = becomeArtistRequestDialog.findViewById(R.id.editText_explain_why)!!


        if ("${mExplainWhyEditText?.text}".isEmpty()) {
            mExplainWhyEditText?.error = getString(R.string.TEXT_EXPLAIN_WHY_EMPTY)
            return false
        }

        return true
    }




    /*---------------------------------------------------*/
    /* --------------------- ACTIONS --------------------*/
    /*---------------------------------------------------*/

    private fun manageOnClickProfilePicture(){
        imageView_profile_profile_pic.setOnClickListener {
            ImagePicker.with(this).provider(ImageProvider.BOTH).galleryMimeTypes(arrayOf("image/*")).crop().start()
        }
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
                showLoadingSaveButton()
                if (hasChangedProfilePicture) {
                    uploadImageToImgurAPICall(selectedProfilePicture, complete = {
                        updateUserInfoAPICall()
                    })
                } else {
                    updateUserInfoAPICall()
                }
            }
        }
    }


    private fun manageOnClickBecomeArtistButton(){
        button_profile_become_artist.setOnClickListener {
            becomeArtistRequestDialog = AlertDialog.Builder(this)
                .setView(R.layout.dialog_become_artist)
                .setPositiveButton(R.string.ACTION_SEND, null)
                .setNegativeButton(R.string.ACTION_CANCEL, null)
                .show()

            val mButtonSend = becomeArtistRequestDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            val mDialogTitle = becomeArtistRequestDialog.findViewById<TextView>(R.id.textView_wanna_become_artist_title)
            val mExplainWhyTitle = becomeArtistRequestDialog.findViewById<TextView>(R.id.textView_explain_why_title)

            mDialogTitle?.text = getString(R.string.LABEL_WANNA_BECOME_ARTIST)
            mExplainWhyTitle?.text = getString(R.string.LABEL_EXPLAIN_WHY)


            mButtonSend.setOnClickListener {
                if (becomeArtistRequestFormIsValid()) {
                    becomeArtistRequestAPICall()
                }

            }

        }
    }


    private fun manageOnClickLogOutButton() {
        textView_logout.setOnClickListener {
            SessionManager(this).removeAuthToken()
            navigateTo(SignInActivity(),true)

            finishAffinity() //finish all activities
        }
    }



    /*---------------------------------------------------*/
    /* -------------------- API CALLS -------------------*/
    /*---------------------------------------------------*/

    private fun becomeArtistRequestAPICall() {
        val becomeArtistRequest = BecomeArtistRequest(
            message = "${mExplainWhyEditText.text}"
        )

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val becomeArtistResponse = ArtfeltClient().getApiService(this@ProfileActivity).becomeArtistRequest(becomeArtistRequest)

                if (becomeArtistResponse.isSuccessful) {
                    Toolbox.showSuccessDialog(this@ProfileActivity, getString(R.string.TEXT_BECOME_ARTIST_SUCCESS))
                    becomeArtistRequestDialog.dismiss()
                }
            } catch (e: Exception) {
                Toolbox.showErrorDialog(this@ProfileActivity, getString(R.string.TEXT_BECOME_ARTIST_API_ERROR))
            }
        }
    }



    private fun changePasswordAPICall() {
        val changePasswordRequest = ChangePasswordRequest(
            oldPassword = "${mOldPasswordEditText.text}",
            newPassword = "${mNewPasswordEditText.text}"
        )

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val changePasswordResponse = ArtfeltClient().getApiService(this@ProfileActivity).changePassword(changePasswordRequest)

                if (changePasswordResponse.isSuccessful) {
                    Toolbox.showSuccessDialog(this@ProfileActivity, getString(R.string.TEXT_PASSWORD_CHANGED_SUCCESS))
                    changePasswordDialog.dismiss()
                } else if (changePasswordResponse.code() == 401) {
                    mOldPasswordEditText.error = getString(R.string.TEXT_OLD_PASSWORD_ERROR)
                }
            } catch (e: Exception) {
                Toolbox.showErrorDialog(this@ProfileActivity, getString(R.string.TEXT_CHANGE_PASSWORD_API_ERROR))
            }
        }
    }



    private fun updateUserInfoAPICall() {
        var pictureUrl: String
        if(newProfilePictureUrl.isNullOrEmpty() && !(User.info?.avatarUrl.isNullOrEmpty())) {
            pictureUrl = User.info?.avatarUrl!!
        } else {
            pictureUrl = newProfilePictureUrl!!
        }

        val updateUserInfoRequest = User(
            firstName = "${editText_profile_first_name.text}",
            lastName = "${editText_profile_last_name.text}",
            street = "${editText_profile_address_street.text}",
            zipCode = "${editText_profile_address_zipcode.text}",
            city = "${editText_profile_address_city.text}",
            username = "${editText_profile_username.text}",
            email = "${editText_profile_email.text}",
            avatarUrl = pictureUrl
        )

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val updateUserInfoResponse = ArtfeltClient().getApiService(this@ProfileActivity).updateUserInfo(User.info?.id!!, updateUserInfoRequest)

                if (updateUserInfoResponse.isSuccessful && updateUserInfoResponse.body() != null) {
                    Toolbox.showSuccessDialog(this@ProfileActivity, getString(R.string.TEXT_USER_INFO_CHANGED_SUCCESS))

                    updateUserInfoResponse.body().let {
                        val role = User.info?.role
                        User.info = it?.updatedUser
                        User.info?.role = role
                    }
                } else {
                    val errorBody: String = updateUserInfoResponse.errorBody()!!.string()
                    val jsonObject: JsonObject = Gson().fromJson(errorBody, JsonObject::class.java)

                    if (jsonObject != null && jsonObject.has("message") && !jsonObject["message"].isJsonNull) {
                        if(jsonObject["message"].toString().contains("username")) {
                            editText_profile_username.error = getString(R.string.TEXT_USERNAME_ALREADY_USE)
                        } else if(jsonObject["message"].toString().contains("email")) {
                            editText_profile_email.error = getString(R.string.TEXT_EMAIL_ALREADY_USE)
                        }
                    }
                }
                initSaveButton()
            } catch (e: Exception) {
                initSaveButton()
                Toolbox.showErrorDialog(this@ProfileActivity, getString(R.string.TEXT_USER_INFO_API_ERROR))
            }
        }
    }


    private fun uploadImageToImgurAPICall(image: Bitmap, complete: (String) -> Unit) {
        val IMGUR_CLIENT_ID = "71ed2e9f9ddad16"

        Toolbox.getBase64Image(image, complete = { base64Image ->
            GlobalScope.launch(Dispatchers.Default) {
                val url = URL("https://api.imgur.com/3/image")

                val boundary = "Boundary-${System.currentTimeMillis()}"

                val httpsURLConnection = withContext(Dispatchers.IO) {
                    url.openConnection() as HttpsURLConnection
                }

                httpsURLConnection.setRequestProperty("Authorization", "Client-ID $IMGUR_CLIENT_ID")
                httpsURLConnection.setRequestProperty(
                    "Content-Type",
                    "multipart/form-data; boundary=$boundary"
                )

                httpsURLConnection.requestMethod = "POST"
                httpsURLConnection.doInput = true
                httpsURLConnection.doOutput = true

                var body = ""
                body += "--$boundary\r\n"
                body += "Content-Disposition:form-data; name=\"image\""
                body += "\r\n\r\n$base64Image\r\n"
                body += "--$boundary--\r\n"


                val outputStreamWriter = OutputStreamWriter(httpsURLConnection.outputStream)
                withContext(Dispatchers.IO) {
                    outputStreamWriter.write(body)
                    outputStreamWriter.flush()
                }


                val response = httpsURLConnection.inputStream.bufferedReader().use {
                    it.readText()
                }

                val jsonObject = JSONTokener(response).nextValue() as JSONObject

                val data = jsonObject.getJSONObject("data")

                Log.d("TAG", "Link is : ${data.getString("link")}")

                data.getString("link")?.let {
                    newProfilePictureUrl = it
                    complete(newProfilePictureUrl!!) //we wait to achieve before passing to the next instruction
                }
            }
        })


    }


    /*---------------------------------------------------*/
    /* -------------------- DELEGATE -------------------*/
    /*---------------------------------------------------*/

    /***** HeaderDelegate *****/
    override fun onClickHeaderLeftIcon() {
        navigateTo(HomeActivity(), true, transition = TransitionEnum.BOTTOM)
    }

    override fun onClickHeaderRightIcon() {
        navigateTo(ShopcartActivity(), false, transition = TransitionEnum.TOP)
    }


    /***** EditTextWatcherDelegate *****/
    override fun enableSaveButton() {
        button_profile_save.isEnabled = true
        textView_profile_save.isEnabled = true
        button_profile_save.setCardBackgroundColor(ContextCompat.getColorStateList(this, R.color.primaryColor))
    }

    override fun disableSaveButton() {
        button_profile_save.isEnabled = false
        textView_profile_save.isEnabled = false
        button_profile_save.setCardBackgroundColor(ContextCompat.getColorStateList(this, R.color.gray))
    }



}
