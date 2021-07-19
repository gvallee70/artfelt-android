package api.models.request

import com.google.gson.annotations.SerializedName

data class BecomeArtistRequest(
        @SerializedName("type")
        val type: String = "ARTIST_REQUEST",

        @SerializedName("message")
        var message: String
)
