package ui.shopcart.artworks

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artfelt.artfelt.R
import models.order.ItemShopCart

class ShopcartArtworkAdapter(
    val context: Context,
    private var artworks: ArrayList<ItemShopCart>,
    private val listener: PlusOrMinusDelegate
) : RecyclerView.Adapter<ShopcartArtworkViewHolder>() {

    var artworksList = ArrayList<ItemShopCart>(artworks)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopcartArtworkViewHolder {
        return ShopcartArtworkViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_artwork_shopcart, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: ShopcartArtworkViewHolder, position: Int) {
        val artwork = artworksList[position]

        holder.bindView(artwork)
    }

    override fun getItemCount(): Int {
        return artworksList.size
    }

    fun updateShopCartList(updatedShopCartList: ArrayList<ItemShopCart>) {
        artworksList = ArrayList(updatedShopCartList)

        this.notifyDataSetChanged();

    }

}