package api.models.auth.signup

import api.models.user.infos.User
import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("message")
    var message: String? = null,

    @SerializedName("user")
    var user: User
)
