package shopcart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.artfelt.artfelt.R
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import common.HeaderDelegate
import common.HeaderLeftIconEnum
import common.HeaderView
import home.artworks.type.ArtworkTypeAdapter
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_shopcart.*
import managers.shopcart.ItemShopCart
import managers.shopcart.ShopCartManager

class ShopcartActivity: AppCompatActivity(), HeaderDelegate {

    private lateinit var shopcartArtworkAdapter: ShopcartArtworksAdapter
    private lateinit var shopcartArtworks: ArrayList<ItemShopCart>

    companion object {
        private const val NEW_USERNAME = "new-username"
        private const val NEW_PASSWORD = "new-password"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopcart)

        shopcartArtworks = ShopCartManager(this).getShopCartItems()

        this.supportActionBar!!.hide()
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        initHeader()

        initShopcartArtworksRecyclerView()
    }

    private fun initHeader() {
        HeaderView(this, block_header_shopcart, HeaderLeftIconEnum.CLOSE, this)
    }


    private fun initShopcartArtworksRecyclerView() {

        shopcartArtworkAdapter = ShopcartArtworksAdapter(this, shopcartArtworks)

        println(shopcartArtworks)
        recycler_view_artworks_shopcart.removeAllViews()
        recycler_view_artworks_shopcart.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view_artworks_shopcart.adapter = shopcartArtworkAdapter
    }

    override fun onClickHeaderLeftIcon() {
        finish()
        overridePendingTransition(R.transition.slide_in_down, R.transition.slide_out_down)
    }



}