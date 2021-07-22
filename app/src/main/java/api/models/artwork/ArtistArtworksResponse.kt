package api.models.artwork

import com.google.gson.annotations.SerializedName

data class ArtistArtworksResponse(
        @SerializedName("artworks")
        var artworks: ArrayList<Artwork>,
)
