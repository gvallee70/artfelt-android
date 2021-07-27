package api.models.user

import api.models.association.Association
import api.models.user.role.UserRoleEnum
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class Artist(
    @SerializedName("id")
    var id: String? = null,

    @SerializedName("first_name")
    var firstName: String? = null,

    @SerializedName("last_name")
    var lastName: String? = null,

    @SerializedName("address_street")
    var street: String? = null,

    @SerializedName("address_zip_code")
    var zipCode: String? = null,

    @SerializedName("address_city")
    var city: String? = null,

    @SerializedName("username")
    var username: String? = null,

    @SerializedName("mail")
    var email: String? = null,

    @SerializedName("IBAN")
    var iban: String? = null,

    @SerializedName("BIC")
    var bic: String? = null,

    @SerializedName("role")
    var role: UserRoleEnum? = null,

    @SerializedName("percentage")
    var percentage: Int? = null,

    @SerializedName("avatar_url")
    var avatarUrl: String? = null,

    @SerializedName("creation_date")
    var creationDate: Date? = null,

    @SerializedName("association")
    var association: Association? = null

) : Serializable
