package artwork

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import api.models.artwork.Artwork
import api.models.user.User
import com.artfelt.artfelt.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_artwork_details.*
import managers.session.SessionManager
import partials.HeaderDelegate
import partials.HeaderView
import utils.EXTRA_HASHMAP
import utils.getExtraPassedData
import java.io.Serializable

class ArtworkDetailsActivity: AppCompatActivity(), HeaderDelegate {

    private lateinit var artwork: Artwork

    companion object {
        const val ARTWORK = "artwork"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artwork_details)

        if(intent.hasExtra(EXTRA_HASHMAP)) {
            val data = intent.getExtraPassedData()
            artwork = data[ARTWORK] as Artwork
        }

        this.supportActionBar!!.hide()
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
        initArtworkType()
        initArtworkDate()

    }

    private fun initHeader() {
        HeaderView(this, block_header_artwork_details, true, this)
    }

    private fun initArtworkImageView() {
        imageView_artwork_details.clipToOutline = true

        if(artwork.imageUrl.isNullOrEmpty()) {
            imageView_artwork_details.setImageResource(R.drawable.ic_image_placeholder)
        } else {
            Picasso.get().load(artwork.imageUrl).into(imageView_artwork_details)
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
        //val donatedPrice = (artwork.price/100) * artwork.artist.pourcentage
        val donatedPrice = artwork.price!!/4
        textView_association_artwork_details.text = getString(R.string.LABEL_ASSOCIATION_DONATED_PRICE).format(donatedPrice, "Restos du coeur" /*,artwork.artist.association */)
        textView_association_artwork_details.textSize = 12f
    }

    private fun initArtworkDescription() {
        textView_description_artwork_details.text = artwork.description
        textView_description_artwork_details.textSize = 16f
    }

    private fun initArtworkType() {
        textView_type_title_artwork_details.text = getString(R.string.LABEL_ARTWORK_TYPE_TITLE)
        textView_type_artwork_details.text = artwork.type
    }

    private fun initArtworkDate() {
        textView_date_title_artwork_details.text = getString(R.string.LABEL_ARTWORK_DATE_TITLE)
        //textView_date_artwork_details.text = artwork.date
    }

    override fun onClickLeftIcon() {
        finish()
    }

}