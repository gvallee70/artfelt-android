package home

import android.os.Bundle
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import api.ArtfeltClient
import api.models.artwork.Artwork
import artwork.ArtworkDetailsActivity
import com.artfelt.artfelt.R
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import home.artworks.ArtworkAdapter
import home.artworks.ArtworkDelegate
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import common.HeaderDelegate
import common.HeaderLeftIconEnum
import common.HeaderView
import home.artworks.type.ArtworkTypeAdapter
import home.artworks.type.ArtworkTypeDelegate
import home.artworks.type.ArtworkTypeEnum
import profile.ProfileActivity
import utils.Toolbox
import utils.hide
import utils.navigateTo
import utils.show
import utils.transition.TransitionEnum
import java.io.Serializable

class HomeActivity: AppCompatActivity(), HeaderDelegate, ArtworkTypeDelegate, ArtworkDelegate {

    private lateinit var artworkAdapter: ArtworkAdapter
    private lateinit var artworkTypeAdapter: ArtworkTypeAdapter

    var artworkTypes: ArrayList<ArtworkTypeEnum> = arrayListOf()

    companion object {
        const val ARTWORK = "artwork"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        this.supportActionBar!!.hide()

    }

    override fun onResume() {
        super.onResume()

        getAllArtworksAPICall()
        initView()
    }


    private fun initView() {
        initHeader()
    }


    private fun initHeader() {
        HeaderView(this, block_header_home, HeaderLeftIconEnum.PROFILE, this)
    }

    private fun initViewsIfArtworks() {
        initSearchView()
        initAllArtworksTitle()
    }

    private fun initViewsIfNoArtworks() {
        search_view_home.hide()
        initNoArtworksTitle()
    }

    private fun initSearchView() {
        initSearchViewUI()
        initSearchViewUX()
    }

    private fun initSearchViewUI() {
        search_view_home.show()

        search_view_home.queryHint = getString(R.string.LABEL_ARTWORK_SEARCH)

        val searchIcon = search_view_home.findViewById<ImageView>(R.id.search_mag_icon)
        searchIcon.setImageResource(R.drawable.ic_search)

        val cancelIcon = search_view_home.findViewById<ImageView>(R.id.search_close_btn)
        cancelIcon.setImageResource(R.drawable.ic_cancel)

        val textView = search_view_home.findViewById<TextView>(R.id.search_src_text)
        textView.setTextColor(resources.getColor(R.color.primaryColor))
        textView.textSize = 16f

    }


    private fun initSearchViewUX(){
        search_view_home.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrEmpty()) {
                    artworkAdapter.filter.filter(newText)
                    title_artworks_list.text = getString(R.string.LABEL_ALL_ARTWORKS)
                } else {
                    artworkAdapter.filter.filter(newText, Filter.FilterListener {
                        title_artworks_list.text = getString(R.string.LABEL_RESULTS_ARTWORKS_SEARCH).format(artworkAdapter.itemCount)
                    })
                }
            //TODO("ameliorer la recherche car quand on remove des caractere ca annule")x
                return false
            }
        })
    }


    private fun initAllArtworksTitle() {
        title_artworks_list.textSize = 22f
        title_artworks_list.text = getString(R.string.LABEL_ALL_ARTWORKS)
    }

    private fun initNoArtworksTitle() {
        title_artworks_list.textSize = 22f
        title_artworks_list.text = getString(R.string.LABEL_NO_ARTWORKS)
    }


    private fun initArtworkTypesList(artworks: ArrayList<Artwork>) {
        artworkTypes.removeAll(ArtworkTypeEnum.values())

        artworkTypes.add(ArtworkTypeEnum.ALL)

        artworks.forEach {
            it.type?.let { type ->
                if (!artworkTypes.contains(type)) {
                    artworkTypes.add(type)
                }
            }
        }
    }


    private fun initArtworkTypesRecyclerView(artworks: ArrayList<Artwork>) {
        initArtworkTypesList(artworks)

        artworkTypeAdapter = ArtworkTypeAdapter(this, artworkTypes =  artworkTypes, listener = this)

        recycler_view_artworks_type.removeAllViews()
        recycler_view_artworks_type.layoutManager = FlexboxLayoutManager(this, FlexDirection.ROW)
        recycler_view_artworks_type.adapter = artworkTypeAdapter
    }


    private fun initArtworksRecyclerView(artworks: ArrayList<Artwork>) {
        artworkAdapter = ArtworkAdapter(this, artworks, this)

        recycler_view_artworks.removeAllViews()
        recycler_view_artworks.layoutManager = GridLayoutManager(this, 2)
        recycler_view_artworks.adapter = artworkAdapter
    }


    private fun initLoadingArtworksView() {
        textView_artworks_loading.text = getString(R.string.LABEL_LOADING_ARTWORKS)
    }

    private fun hideLoadingArtworksView() {
        progressBar_artworks_loading.hide()
        textView_artworks_loading.hide()
    }


    private fun getAllArtworksAPICall() {
        initLoadingArtworksView()

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val getArtworksResponse = ArtfeltClient().getApiService(this@HomeActivity).getAllArtworks()

                if (getArtworksResponse.isSuccessful && getArtworksResponse.body() != null) {
                    hideLoadingArtworksView()

                    getArtworksResponse.body()?.let {
                        if(it.isEmpty()) {
                            initViewsIfNoArtworks()
                        } else {
                            initViewsIfArtworks()
                            initArtworkTypesRecyclerView(it)
                            initArtworksRecyclerView(it)
                        }
                    }
                } else {
                    Toolbox.showErrorDialog(this@HomeActivity, getString(R.string.TEXT_GET_ARTWORKS_API_ERROR))
                }

            } catch (e: Exception) {
                println(e.message)
                Toolbox.showErrorDialog(this@HomeActivity, getString(R.string.TEXT_GET_ARTWORKS_API_ERROR))
            }
        }
    }

    override fun onClickItem(artwork: Artwork) {
        val data = HashMap<String, Any>()
        data[ARTWORK] = artwork as Serializable
        navigateTo(ArtworkDetailsActivity(), data)

    }

    override fun onClickHeaderLeftIcon() {
        navigateTo(ProfileActivity(), transition = TransitionEnum.TOP)
    }

    override fun onClickArtworkType(artworkType: ArtworkTypeEnum) {
        recycler_view_artworks_type.adapter = ArtworkTypeAdapter(this, artworkType, artworkTypes, this)

        artworkAdapter.filterArtworksByType(artworkType)

        when(artworkType) {
            ArtworkTypeEnum.ALL -> title_artworks_list.text = getString(R.string.LABEL_ALL_ARTWORKS)
            else -> title_artworks_list.text = getString(R.string.LABEL_RESULTS_ARTWORKS_SEARCH).format(artworkAdapter.itemCount)
        }

    }

}