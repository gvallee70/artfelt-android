package home.artworks

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import api.models.artwork.Artwork
import com.artfelt.artfelt.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_artwork.view.*
import utils.setImageURL
import java.io.InputStream
import java.net.URLConnection
import java.net.URLStreamHandler

class ArtworkViewHolder(
    val container: View,
    private val listener: ArtworkDelegate
    ) : RecyclerView.ViewHolder(container) {

    private var mImage: ImageView = container.imageView_artwork
    private var mTitle: TextView = container.textView_title_artwork
    private var mPrice: TextView = container.textView_price_artwork


    fun bindView(artwork: Artwork) {
        initView(artwork)

        manageOnClickArtwork(artwork)
    }


    private fun initView(artwork: Artwork) {
        initImage(artwork)
        initTitle(artwork)
        initPrice(artwork)
    }


    private fun initImage(artwork: Artwork) {
        mImage.clipToOutline = true

        if(artwork.imageUrl.isNullOrEmpty()) {
            mImage.setImageResource(R.drawable.ic_image_placeholder)
        } else {
            mImage.setImageURL(artwork.imageUrl!!)
        }
    }

    private fun initTitle(artwork: Artwork) {
        mTitle.text = artwork.title
    }

    private fun initPrice(artwork: Artwork) {
        if(artwork.quantity == 0) {
            mPrice.text = container.context.getString(R.string.LABEL_SOLD_OUT)
            mPrice.setTextColor(container.context.resources.getColorStateList(R.color.errorColor))
            mPrice.typeface = Typeface.DEFAULT
            mPrice.textSize = 14f
        } else {
            mPrice.text = container.context.getString(R.string.LABEL_ARTWORK_PRICE).format(artwork.price)
        }
    }


    private fun manageOnClickArtwork(artwork: Artwork) {
        container.setOnClickListener {
            listener.onClickItem(artwork)
        }
    }

}