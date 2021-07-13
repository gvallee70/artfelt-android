package api.models.user.infos

import com.google.gson.annotations.SerializedName

data class UserInfosResponse(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("address_street")
    val street: String,

    @SerializedName("address_zip_code")
    val zipCode: String,

    @SerializedName("address_city")
    val city: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("mail")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("role")
    val role: String? = null,

    @SerializedName("avatar_url")
    val avatarUrl: String? = null

)
