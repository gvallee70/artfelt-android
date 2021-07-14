package home.artworks

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import api.models.artwork.Artwork
import com.artfelt.artfelt.R

class ArtworkAdapter(val context: Context, private var artworks: ArrayList<Artwork>): RecyclerView.Adapter<ArtworkViewHolder>() {
    var artworksList = ArrayList<Artwork>(artworks)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtworkViewHolder {
        return ArtworkViewHolder(LayoutInflater.from(context).inflate(R.layout.item_artwork, parent, false))
    }

    override fun onBindViewHolder(holder: ArtworkViewHolder, position: Int) {
        holder.bindView(artworksList[position])
    }

    override fun getItemCount(): Int {
        return artworksList.size
    }

}