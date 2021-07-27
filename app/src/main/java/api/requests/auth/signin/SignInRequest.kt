package api.requests.auth.signin

import com.google.gson.annotations.SerializedName

data class SignInRequest (
    @SerializedName("username")
    var username: String,

    @SerializedName("password")
    var password: String
)