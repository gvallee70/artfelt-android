package models.user.artist

import models.association.Association
import models.user.role.UserRoleEnum
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class Artist(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("first_name")
    val firstName: String? = null,

    @SerializedName("last_name")
    val lastName: String? = null,

    @SerializedName("address_street")
    val street: String? = null,

    @SerializedName("address_zip_code")
    val zipCode: String? = null,

    @SerializedName("address_city")
    val city: String? = null,

    @SerializedName("username")
    val username: String? = null,

    @SerializedName("mail")
    val email: String? = null,

    @SerializedName("IBAN")
    val iban: String? = null,

    @SerializedName("BIC")
    val bic: String? = null,

    @SerializedName("role")
    val role: UserRoleEnum? = null,

    @SerializedName("percentage")
    val percentage: Int? = null,

    @SerializedName("avatar_url")
    val avatarUrl: String? = null,

    @SerializedName("creation_date")
    val creationDate: Date? = null,

    @SerializedName("association")
    val association: Association? = null

) : Serializable
