package api.requests.auth.resetpassword

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
        @SerializedName("mail")
        var email: String? = null,

        @SerializedName("token")
        var confirmationCode: String? = null,

        @SerializedName("password")
        var password: String? = null,

        @SerializedName("confirm_password")
        var passwordConfirmation: String? = null,
)
