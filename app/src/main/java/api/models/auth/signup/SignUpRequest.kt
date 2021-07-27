package api.models.auth.signup

import api.models.user.role.UserRoleEnum
import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("first_name")
    val firstName: String? = null,

    @SerializedName("last_name")
    val lastName: String? = null,

    @SerializedName("address_street")
    val street: String?  = null,

    @SerializedName("address_zip_code")
    val zipCode: String? = null,

    @SerializedName("address_city")
    val city: String? = null,

    @SerializedName("username")
    val username: String? = null,

    @SerializedName("mail")
    val email: String? = null,

    @SerializedName("password")
    val password: String? = null,

    @SerializedName("role")
    val role: String = UserRoleEnum.CUSTOMER.value,

    )
