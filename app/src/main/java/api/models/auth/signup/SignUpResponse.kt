package api.models.auth.signup

import api.models.user.infos.UserInfosResponse
import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("user")
    var user: UserInfosResponse
)
