package home

import android.graphics.Color
import android.graphics.ColorFilter
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import api.ArtfeltClient
import api.models.artwork.Artwork
import api.models.user.User
import com.artfelt.artfelt.R
import home.artworks.ArtworkAdapter
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import managers.session.SessionManager
import partials.HeaderView
import signin.SignInActivity
import signup.SignUpActivity
import utils.Toolbox
import utils.navigateTo

class HomeActivity: AppCompatActivity() {

    private lateinit var artworkAdapter: ArtworkAdapter

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
        initSearchView()
        initArtworksTitle()

        manageOnClickDecoButton()
    }


    private fun initHeader() {
        HeaderView(this, block_header_home, User.infos!!)
    }


    private fun initSearchView() {
        initSearchViewUI()
        initSearchViewUX()
    }

    private fun initSearchViewUI() {
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
                artworkAdapter.filter.filter(newText)
                return false
            }
        })
    }


    private fun initArtworksTitle() {
        title_all_artworks.textSize = 22f
        title_all_artworks.text = getString(R.string.LABEL_ALL_ARTWORKS)
    }


    private fun initArtworksRecyclerView(artworks: ArrayList<Artwork>) {
        artworkAdapter = ArtworkAdapter(this, artworks)

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
        var userToken = SessionManager(this).fetchAuthToken().toString()

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val getArtworksResponse = ArtfeltClient.apiService.getAllArtworks(userToken)

                if (getArtworksResponse.isSuccessful && getArtworksResponse.body() != null) {
                    getArtworksResponse.body()?.let {
                        initArtworksRecyclerView(it)
                    }
                } else {
                    Toolbox.showErrorDialog(this@HomeActivity, getString(R.string.TEXT_ERROR_GET_ARTWORKS))
                }

            } catch (e: Exception) {
                println(e.message)
                Toolbox.showErrorDialog(this@HomeActivity, getString(R.string.TEXT_ERROR_GET_ARTWORKS))
            }
        }
    }
}