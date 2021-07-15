package home.artworks

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import api.models.artwork.Artwork
import com.artfelt.artfelt.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_artwork.view.*
import java.io.InputStream
import java.net.URLConnection
import java.net.URLStreamHandler

class ArtworkViewHolder(val container: View) : RecyclerView.ViewHolder(container) {

    private var mImage: ImageView = container.imageView_artwork
    private var mTitle: TextView = container.textView_title_artwork
    private var mPrice: TextView = container.textView_price_artwork


    fun bindView(artwork: Artwork) {
        initImage(artwork)
        initTitle(artwork)
        initPrice(artwork)

    }


    private fun initImage(artwork: Artwork) {
        mImage.clipToOutline = true

        if(artwork.imageUrl.isNullOrEmpty()) {
            mImage.setImageResource(R.drawable.ic_image_placeholder)
        } else {
            Picasso.get().load(artwork.imageUrl).into(mImage)
        }
    }

    private fun initTitle(artwork: Artwork) {
        mTitle.text = artwork.title
    }

    private fun initPrice(artwork: Artwork) {
        mPrice.text = container.context
                               .getString(R.string.LABEL_ARTWORK_PRICE)
                               .format(artwork.price)
    }

}