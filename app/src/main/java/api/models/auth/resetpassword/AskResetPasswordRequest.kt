package api.models.auth.resetpassword

import com.google.gson.annotations.SerializedName

data class AskResetPasswordRequest(
    @SerializedName("mail")
    var email: String,
)
