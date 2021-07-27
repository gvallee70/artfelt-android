package api.responses.user.userinfo

import models.user.User
import com.google.gson.annotations.SerializedName

data class UpdateUserInfoResponse(
        @SerializedName("message")
        val message: String? = null,

        @SerializedName("newUser")
        val updatedUser: User? = null,

        )
