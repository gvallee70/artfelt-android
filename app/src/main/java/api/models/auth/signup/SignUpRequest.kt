package api.models.auth.signup

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
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

)
