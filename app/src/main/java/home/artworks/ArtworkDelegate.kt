package home.artworks

import api.models.artwork.Artwork

interface ArtworkDelegate {
    fun onClickItem(artwork: Artwork)
}