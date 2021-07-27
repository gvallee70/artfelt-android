package ui.home.artworks.artwork

import models.artwork.Artwork

interface ArtworkDelegate {
    fun onClickItem(artwork: Artwork)
}