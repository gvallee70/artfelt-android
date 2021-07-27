package api.responses.user.artistinfo

import models.association.Association
import com.google.gson.annotations.SerializedName
import models.artwork.Artwork
import java.util.*
import kotlin.collections.ArrayList

data class ArtistInfoResponse(
        @SerializedName("id")
        var id: String? = null,

        @SerializedName("first_name")
        var firstName: String? = null,

        @SerializedName("last_name")
        var lastName: String? = null,

        @SerializedName("address_city")
        var city: String? = null,

        @SerializedName("address_zip_code")
        var zipCode: String? = null,

        @SerializedName("percentage")
        var percentage: Int? = null,

        @SerializedName("avatar_url")
        var avatarUrl: String? = null,

        @SerializedName("creation_date")
        var creationDate: Date? = null,

        @SerializedName("artworks")
        var artworks: ArrayList<Artwork>,

        @SerializedName("association")
        var association: Association? = null,
)
