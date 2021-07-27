package api.requests.auth.resetpassword

import com.google.gson.annotations.SerializedName

data class AskResetPasswordRequest(
    @SerializedName("mail")
    var email: String,
)
