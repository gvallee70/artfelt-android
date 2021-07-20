package artwork

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import api.models.artwork.Artwork
import com.artfelt.artfelt.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_artwork_details.*
import common.HeaderDelegate
import common.HeaderLeftIcon
import common.HeaderView
import utils.EXTRA_HASHMAP
import utils.formatddMMMMYYYY
import utils.getExtraPassedData
import utils.setImageURL

class ArtworkDetailsActivity: AppCompatActivity(), HeaderDelegate {

    private lateinit var artwork: Artwork

    companion object {
        const val ARTWORK = "artwork"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artwork_details)

        this.supportActionBar!!.hide()

        getArtworkDetailsFromIntent()
    }


    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        initHeader()
        initArtworkImageView()
        initArtworkTitle()
        initArtworkPrice()
        initArtworkAssociationDonatedPrice()
        initArtworkDescription()
        initArtworkQuantity()
        initArtworkType()
        initArtworkDate()
        initArtistView()

    }

    private fun getArtworkDetailsFromIntent() {
        if(intent.hasExtra(EXTRA_HASHMAP)) {
            val data = intent.getExtraPassedData()
            artwork = data[ARTWORK] as Artwork
        }
    }

    private fun initHeader() {
        HeaderView(this, block_header_artwork_details, HeaderLeftIcon.BACK, this)
    }


    private fun initArtworkImageView() {
        imageView_artwork_details.clipToOutline = true

        if(artwork.imageUrl.isNullOrEmpty()) {
            imageView_artwork_details.setImageResource(R.drawable.ic_image_placeholder)
        } else {
            imageView_artwork_details.setImageURL(artwork.imageUrl!!)
        }
    }

    private fun initArtworkTitle() {
        textView_title_artwork_details.text = artwork.title
        textView_title_artwork_details.textSize = 26f
    }

    private fun initArtworkPrice() {
        textView_price_artwork_details.text = getString(R.string.LABEL_ARTWORK_PRICE).format(artwork.price)
        textView_price_artwork_details.textSize = 22f
    }

    private fun initArtworkAssociationDonatedPrice() {
        artwork.artist?.percentage?.let {
            val donatedPrice = (artwork.price!!/100) * it!!
            textView_association_artwork_details.text = getString(R.string.LABEL_ASSOCIATION_DONATED_PRICE).format(donatedPrice, "Restos du coeur" /*,artwork.artist.association */)
            textView_association_artwork_details.textSize = 12f
        }
    }

    private fun initArtworkDescription() {
        textView_description_artwork_details.text = artwork.description
        textView_description_artwork_details.textSize = 16f
    }

    private fun initArtworkQuantity() {
        textView_quantity_title_artwork_details.text = getString(R.string.LABEL_ARTWORK_QUANTITY_TITLE)
        textView_quantity_artwork_details.text = artwork.quantity.toString()
    }

    private fun initArtworkType() {
        textView_type_title_artwork_details.text = getString(R.string.LABEL_ARTWORK_TYPE_TITLE)

        when(artwork.type?.toLowerCase()) {
            "painting" -> textView_type_artwork_details.text = getString(R.string.LABEL_ARTWORK_TYPE_PAINTING)
            "literature" -> textView_type_artwork_details.text = getString(R.string.LABEL_ARTWORK_TYPE_LITERATURE)
            "sculpture" -> textView_type_artwork_details.text = getString(R.string.LABEL_ARTWORK_TYPE_SCULPTURE)
            "photo" -> textView_type_artwork_details.text = getString(R.string.LABEL_ARTWORK_TYPE_PHOTO)
            else -> textView_type_artwork_details.text = artwork.type
        }
    }

    private fun initArtworkDate() {
        textView_date_title_artwork_details.text = getString(R.string.LABEL_ARTWORK_DATE_TITLE)
        textView_date_artwork_details.text = artwork.creationDate.formatddMMMMYYYY()
    }


    private fun initArtistView() {
        artwork.artist?.let {
            ArtistView(this, block_artwork_artist_view, artwork.artist!!)
        }
    }

    override fun onClickHeaderLeftIcon() {
        finish()
    }

}