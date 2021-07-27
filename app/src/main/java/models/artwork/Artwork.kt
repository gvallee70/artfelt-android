package models.artwork

import models.user.artist.Artist
import com.google.gson.annotations.SerializedName
import ui.home.artworks.type.ArtworkTypeEnum
import java.io.Serializable
import java.util.*

data class Artwork(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("type")
    val type: ArtworkTypeEnum? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("price")
    val price: Int? = null,

    @SerializedName("quantity")
    val quantity: Int? = null,

    @SerializedName("image_url")
    val imageUrl: String? = null,

    @SerializedName("creation_date")
    val creationDate: Date,

    @SerializedName("artist")
    var artist: Artist? = null,
): Serializable

