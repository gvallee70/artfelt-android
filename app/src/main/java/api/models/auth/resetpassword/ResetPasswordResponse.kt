package api.models.auth.resetpassword

import com.google.gson.annotations.SerializedName

data class ResetPasswordResponse(
        @SerializedName("username")
        var username: String,
)
