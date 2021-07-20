package api.models.user.update

import api.models.user.User
import com.google.gson.annotations.SerializedName

data class UpdateUserInfoResponse(
        @SerializedName("message")
        val message: String? = null,

        @SerializedName("newUser")
        val updatedUser: User? = null,

        )
