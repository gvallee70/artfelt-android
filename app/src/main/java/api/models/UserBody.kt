package api.models

import com.google.gson.annotations.SerializedName

data class UserBody(
        @SerializedName("id") val id: Int? = null,
        @SerializedName("token") var token: String? = null,
        @SerializedName("first_name") val firstName: String? = null,
        @SerializedName("last_name") val lastName: String? = null,
        @SerializedName("address_street") val street: String? = null,
        @SerializedName("address_zip_code") val zipCode: String? = null,
        @SerializedName("address_city") val city: String? = null,
        @SerializedName("username") val username: String? = null,
        @SerializedName("mail") val email: String? = null,
        @SerializedName("password") val password: String? = null,
        @SerializedName("role") val role: String? = "CUSTOMER"

)

