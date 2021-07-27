package artwork

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import api.ArtfeltClient
import api.models.artwork.Artwork
import api.models.user.Artist
import api.models.user.User
import com.artfelt.artfelt.R
import kotlinx.android.synthetic.main.activity_artwork_details.*
import common.HeaderDelegate
import common.HeaderLeftIconEnum
import common.HeaderRightIconEnum
import common.HeaderView
import home.HomeActivity
import home.artworks.ArtworkAdapter
import home.artworks.ArtworkDelegate
import home.artworks.type.ArtworkTypeEnum
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import managers.shopcart.ItemShopCart
import managers.shopcart.ShopCartManager
import shopcart.ShopcartActivity
import utils.*
import utils.transition.TransitionEnum
import java.io.Serializable

class ArtworkDetailsActivity: AppCompatActivity(), HeaderDelegate, ArtworkDelegate {

    private lateinit var artwork: Artwork
    private lateinit var artworkAdapter: ArtworkAdapter


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
        initArtworkDescription()
        initArtworkQuantity()
        initArtworkType()
        initArtworkDate()
        initArtistView()
        initAddShopCartButton()
    }

    private fun getArtworkDetailsFromIntent() {
        if(intent.hasExtra(EXTRA_HASHMAP)) {
            val data = intent.getExtraPassedData()
            artwork = data[ARTWORK] as Artwork
        }
    }

    private fun initHeader() {
        HeaderView(this, block_header_artwork_details, HeaderLeftIconEnum.BACK, HeaderRightIconEnum.SHOPCART,this)
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
            val donatedPrice = ((artwork.price!!/100.toFloat()) * it!!)
            artwork.artist?.association?.name.let {
                textView_association_artwork_details.text = getString(R.string.LABEL_ASSOCIATION_DONATED_PRICE).format(donatedPrice, it)
                textView_association_artwork_details.textSize = 12f
            }
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
        artwork.type?.let {
            textView_type_title_artwork_details.text = getString(R.string.LABEL_ARTWORK_TYPE_TITLE)
            textView_type_artwork_details.text = getString(it.value)
        }
    }

    private fun initArtworkDate() {
        textView_date_title_artwork_details.text = getString(R.string.LABEL_ARTWORK_DATE_TITLE)
        textView_date_artwork_details.text = artwork.creationDate.formatddMMMMYYYY()
    }


    private fun initArtistView() {
        artwork.artist?.let {
            ArtistView(this, block_artwork_artist_view, artwork.artist!!)

            getArtistArtworksAPICall()
        }
    }

    private fun initArtistArtworksRecyclerView(artworks: ArrayList<Artwork>) {
        artworkAdapter = ArtworkAdapter(this, artworks, this)

        recycler_view_artist_artworks.removeAllViews()
        recycler_view_artist_artworks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_artist_artworks.adapter = artworkAdapter
    }


    private fun initAddShopCartButton() {
        if (artwork.quantity == 0 || (User.info?.id == artwork.artist?.id)) {
            fab_add_to_shopcart.hide()
        } else {
            fab_add_to_shopcart.show()
            manageOnClickAddShopCartButton()
        }
    }


    private fun manageOnClickAddShopCartButton() {
        fab_add_to_shopcart.setOnClickListener {
            val itemShopcart = ItemShopCart(
                id = artwork.id,
                title = artwork.title,
                price = artwork.price,
                quantity = 1,
                maxQuantity = artwork.quantity,
                imageUrl = artwork.imageUrl
            )
            ShopCartManager(this).saveToShopCart(itemShopcart)
            navigateTo(ShopcartActivity(), transition = TransitionEnum.TOP)
        }
    }



    private fun getArtistArtworksAPICall() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val getArtworksResponse = ArtfeltClient().getApiService(this@ArtworkDetailsActivity).getArtistArtworks(artwork.artist?.id!!)

                if (getArtworksResponse.isSuccessful && getArtworksResponse.body() != null) {
                    getArtworksResponse.body()?.let { response ->
                        var artworks = response.artworks
                        artworks.forEach {
                            var artist = Artist(
                                id = response.id,
                                firstName = response.firstName,
                                lastName = response.lastName,
                                city = response.city,
                                zipCode = response.zipCode,
                                percentage = response.percentage,
                                avatarUrl = response.avatarUrl,
                                creationDate = response.creationDate,
                                association = response.association
                            )
                            if (it.id == artwork.id) {
                                artwork = it
                            }
                            it.artist = artist
                        }
                        artworks.remove(artwork)
                        initArtworkAssociationDonatedPrice()
                        initArtistArtworksRecyclerView(artworks)
                    }
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }




    override fun onClickHeaderLeftIcon() {
        navigateTo(HomeActivity(), finish = true, transition = TransitionEnum.LEFT)
    }

    override fun onClickHeaderRightIcon() {
        navigateTo(ShopcartActivity(), transition = TransitionEnum.TOP)
    }


    override fun onClickItem(artwork: Artwork) {
        val data = HashMap<String, Any>()
        data[HomeActivity.ARTWORK] = artwork as Serializable
        navigateTo(this, finish = true, data = data)
    }

}