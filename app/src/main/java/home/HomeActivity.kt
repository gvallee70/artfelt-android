package home

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import api.ArtfeltClient
import api.models.artwork.Artwork
import artwork.ArtworkDetailsActivity
import com.artfelt.artfelt.R
import home.artworks.ArtworkAdapter
import home.artworks.ArtworkDelegate
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import managers.session.SessionManager
import common.HeaderDelegate
import common.HeaderView
import profile.ProfileActivity
import signin.SignInActivity
import utils.Toolbox
import utils.hide
import utils.navigateTo
import utils.show
import java.io.Serializable

class HomeActivity: AppCompatActivity(), ArtworkDelegate, HeaderDelegate {

    private lateinit var artworkAdapter: ArtworkAdapter

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

        manageOnClickDecoButton()
    }


    private fun initHeader() {
        HeaderView(this, block_header_home, false, this)
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
                title_artworks_list.hide()
                artworkAdapter.filter.filter(newText)

                if (newText.isNullOrEmpty()) {
                    title_artworks_list.show()
                }
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


    private fun initArtworksRecyclerView(artworks: ArrayList<Artwork>) {
        artworkAdapter = ArtworkAdapter(this, artworks, this)

        recycler_view_artworks.removeAllViews()
        recycler_view_artworks.layoutManager = GridLayoutManager(this, 2)
        recycler_view_artworks.adapter = artworkAdapter
    }



    private fun manageOnClickDecoButton() {
        deconnexion.setOnClickListener {
            SessionManager(this).removeAuthToken()
            navigateTo(SignInActivity(),true)
        }
    }


    private fun getAllArtworksAPICall() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val getArtworksResponse = ArtfeltClient().getApiService(this@HomeActivity).getAllArtworks()

                if (getArtworksResponse.isSuccessful && getArtworksResponse.body() != null) {
                    getArtworksResponse.body()?.let {
                        if (it.isEmpty()) {
                            initViewsIfNoArtworks()
                        } else {
                            initArtworksRecyclerView(it)
                            initViewsIfArtworks()
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
        navigateTo(ArtworkDetailsActivity(), data, false)

    }

    override fun onClickHeaderLeftIcon() {
        navigateTo(ProfileActivity(), false)
    }
}