package api.responses.auth.signup

import models.user.User
import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("message")
    var message: String? = null,

    @SerializedName("user")
    var user: User? = null
)
