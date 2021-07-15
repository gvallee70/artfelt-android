package api.models.user

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class Artist(
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

    @SerializedName("IBAN")
    val iban: String? = null,

    @SerializedName("BIC")
    val bic: String? = null,

    @SerializedName("creation_date")
    val creationDate: Date,

    @SerializedName("role")
    val role: String,

    @SerializedName("avatar_url")
    val avatarUrl: String? = null
): Serializable
