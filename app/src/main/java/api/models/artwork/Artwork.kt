package api.models.artwork

import com.google.gson.annotations.SerializedName

data class Artwork(
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("title")
    var title: String,

    @SerializedName("type")
    var type: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("price")
    var price: Int,

    @SerializedName("quantity")
    var quantity: Int,

    @SerializedName("image_url")
    var imageUrl: String? = null,

    @SerializedName("artistId")
    var artistID: String? = null,
)

