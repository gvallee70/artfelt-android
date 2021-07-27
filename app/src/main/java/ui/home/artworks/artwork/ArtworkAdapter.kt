package ui.home.artworks.artwork

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import models.artwork.Artwork
import com.artfelt.artfelt.R
import ui.home.artworks.type.ArtworkTypeEnum
import java.util.*
import kotlin.collections.ArrayList

class ArtworkAdapter(
    val context: Context,
    private var artworks: ArrayList<Artwork>,
    private val listener: ArtworkDelegate
) : RecyclerView.Adapter<ArtworkViewHolder>(), Filterable {

    var artworksList = ArrayList<Artwork>(artworks)
    var artworksListFilter = ArrayList<Artwork>(artworksList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtworkViewHolder {
        return ArtworkViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_artwork, parent, false),
            listener
        )
    }


    override fun onBindViewHolder(holder: ArtworkViewHolder, position: Int) {
        val artwork = artworksListFilter[position]

        holder.bindView(artwork)
    }


    override fun getItemCount(): Int {
        return artworksListFilter.size
    }


    fun filterArtworksByType(artworkType: ArtworkTypeEnum) {
        artworksListFilter = artworksList

        when(artworkType) {
            ArtworkTypeEnum.ALL -> artworksListFilter = artworksListFilter
            else -> {
                artworksListFilter = artworksListFilter.filter {
                    it.type == artworkType
                } as ArrayList<Artwork>
            }
        }

        notifyDataSetChanged()
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    artworksListFilter = artworksList
                } else {
                    val resultList = ArrayList<Artwork>()
                    for (artwork in artworksListFilter) {
                        if (artwork.title!!.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(artwork)
                        }
                    }
                    artworksListFilter = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = artworksListFilter
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                artworksListFilter = results?.values as ArrayList<Artwork>
                notifyDataSetChanged()
            }
        }

    }


}