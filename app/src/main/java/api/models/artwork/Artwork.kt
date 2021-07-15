package api.models.artwork

import android.os.Parcel
import android.os.Parcelable
import api.models.user.Artist
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Artwork(
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("type")
    var type: String? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("price")
    var price: Int? = null,

    @SerializedName("quantity")
    var quantity: Int? = null,

    @SerializedName("image_url")
    var imageUrl: String? = null,

    @SerializedName("artist")
    var artist: Artist? = null,
): Serializable

