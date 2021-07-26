package shopcart

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import api.models.artwork.Artwork
import com.artfelt.artfelt.R
import home.artworks.ArtworkDelegate
import kotlinx.android.synthetic.main.item_artwork.view.*
import kotlinx.android.synthetic.main.item_artwork_shopcart.view.*
import managers.shopcart.ItemShopCart
import utils.setImageURL

class ShopcartArtworkViewHolder(
    val container: View,
    //private val listener: ArtworkDelegate
) : RecyclerView.ViewHolder(container) {

    private var mImage: ImageView = container.imageView_artwork_shopcart
    private var mTitle: TextView = container.textView_artwork_title_shopcart
    private var mPrice: TextView = container.textView_artwork_price_shopcart

    fun bindView(item: ItemShopCart) {
        initView(item)

        //manageOnClickArtwork(artwork)

    }

    private fun initView(item: ItemShopCart) {
        initImage(item)
        initTitle(item)
        initPrice(item)
    }


    private fun initImage(item: ItemShopCart) {
        mImage.clipToOutline = true

        if(item.imageUrl.isNullOrEmpty()) {
            mImage.setImageResource(R.drawable.ic_image_placeholder)
        } else {
            mImage.setImageURL(item.imageUrl!!)
        }
    }

    private fun initTitle(item: ItemShopCart) {
        mTitle.text = item.title
    }

    private fun initPrice(item: ItemShopCart) {
        mPrice.text = container.context.getString(R.string.LABEL_ARTWORK_PRICE).format(item.price)
    }


    /* fun manageOnClickArtwork(artwork: Artwork) {
         container.setOnClickListener {
             listener.onClickItem(artwork)
         }
     }*/

}