package home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import api.models.user.User
import com.artfelt.artfelt.R
import home.artworks.Artwork
import home.artworks.ArtworkAdapter
import kotlinx.android.synthetic.main.activity_home.*
import managers.session.SessionManager
import partials.HeaderView

class HomeActivity: AppCompatActivity() {

    var artworkAdapter: ArtworkAdapter? = null
    var artworks: ArrayList<Artwork> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        this.supportActionBar!!.hide()
    }


    override fun onResume() {
        super.onResume()

        initView()
    }

    private fun initView() {
        initHeader()
        initSearchView()
        initArtworksTitle()

        artworks.add(Artwork(R.drawable.image_default_profilepic, "Coeur de pirate", "3$"))
        artworks.add(Artwork(R.drawable.image_default_profilepic, "Toto", "3$"))
        artworks.add(Artwork(R.drawable.image_default_profilepic, "Je m'apelle paul grossier trop lol", "3$"))
        artworks.add(Artwork(R.drawable.image_default_profilepic, "A", "3$"))
        artworks.add(Artwork(R.drawable.image_default_profilepic, "Todvbaknldnaldnaldnalndnadnanlnlto", "3$"))

        artworkAdapter = ArtworkAdapter(this, artworks)

        grid_view_artworks.adapter = artworkAdapter
        grid_view_artworks.isVerticalScrollBarEnabled = false
        grid_view_artworks.isHorizontalScrollBarEnabled = false

    }


    private fun initHeader() {
        HeaderView(this, block_header_home, User.infos!!)
        println(SessionManager(this).fetchAuthToken())
        println(User.infos)
    }

    private fun initSearchView(){
        grid_view_artworks.visibility = View.VISIBLE
    }

    private fun initArtworksTitle() {
        title_all_artworks.textSize = 22f
        title_all_artworks.text = getString(R.string.LABEL_ALL_ARTWORKS)
    }

}