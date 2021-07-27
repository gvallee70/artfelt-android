package api.models.association

import com.google.gson.annotations.SerializedName
import home.artworks.type.ArtworkTypeEnum
import java.io.Serializable

data class Association(
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("name")
    var name: String? = null,
): Serializable
