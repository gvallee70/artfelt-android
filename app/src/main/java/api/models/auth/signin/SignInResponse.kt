package api.models.auth.signin

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("token")
    var token: String? = null,

)
