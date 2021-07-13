package home.artworks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.artfelt.artfelt.R
import kotlinx.android.synthetic.main.item_artwork.view.*

class ArtworkAdapter: BaseAdapter {
    var artworks: ArrayList<Artwork> = ArrayList()
    var context: Context? = null

    constructor(context: Context, artworks: ArrayList<Artwork>) : super() {
        this.context = context
        this.artworks = artworks
    }

    override fun getCount(): Int {
        return artworks.size
    }

    override fun getItem(position: Int): Any {
        return artworks[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val artwork = this.artworks[position]
        var artworkView = LayoutInflater.from(context).inflate(R.layout.item_artwork, null)

        artworkView.imageView_artwork.setImageResource(artwork.image!!)
        artworkView.textView_title_artwork.text = artwork.title
        artworkView.textView_price_artwork.text = artwork.price


        return artworkView
    }
}