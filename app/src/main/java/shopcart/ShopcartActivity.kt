package shopcart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.artfelt.artfelt.R
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import common.HeaderDelegate
import common.HeaderLeftIconEnum
import common.HeaderRightIconEnum
import common.HeaderView
import home.artworks.type.ArtworkTypeAdapter
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_shopcart.*
import managers.session.SessionManager
import managers.shopcart.ItemShopCart
import managers.shopcart.ShopCartManager
import utils.Toolbox
import utils.navigateTo
import utils.transition.TransitionEnum

class ShopcartActivity: AppCompatActivity(), HeaderDelegate, PlusOrMinusDelegate {

    private lateinit var shopcartArtworkAdapter: ShopcartArtworksAdapter
    private lateinit var shopcartArtworks: ArrayList<ItemShopCart>

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
        initShopCartTitle()
        initShopcartArtworksRecyclerView()
    }

    private fun initHeader() {
        HeaderView(this, block_header_shopcart, HeaderLeftIconEnum.CLOSE, HeaderRightIconEnum.SHOPCART,this)
    }

    private fun initShopCartTitle() {
        title_artworks_shopcart.textSize = 22f

        //shopcartArtworks = ShopCartManager(this).getShopCartItems()

        if(!shopcartArtworks.isEmpty()) {
            var totalPrice = 0
            shopcartArtworks.forEach {
                totalPrice = totalPrice.plus((it.price!! * it.quantity!!))
            }
            title_artworks_shopcart.text = getString(R.string.LABEL_SHOPCART_TITLE).format(totalPrice)
        } else {
            title_artworks_shopcart.text = getString(R.string.LABEL_SHOPCART_EMPTY)
        }
    }

    private fun initShopcartArtworksRecyclerView() {
        shopcartArtworkAdapter = ShopcartArtworksAdapter(this, shopcartArtworks, this)

        println(shopcartArtworks)
        recycler_view_artworks_shopcart.removeAllViews()
        recycler_view_artworks_shopcart.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view_artworks_shopcart.adapter = shopcartArtworkAdapter
    }

    override fun onClickHeaderLeftIcon() {
        finish()
        overridePendingTransition(R.transition.slide_in_down, R.transition.slide_out_down)
    }

    override fun onClickHeaderRightIcon() {
        navigateTo(ShopcartActivity(), transition = TransitionEnum.TOP)
    }

    override fun onClickMinus(item: ItemShopCart) {
        ShopCartManager(this).saveToShopCart(item)
        initShopCartTitle()
    }

    override fun onClickPlus(item: ItemShopCart) {
        ShopCartManager(this).saveToShopCart(item)
        initShopCartTitle()
    }

    override fun onClickRemove(item: ItemShopCart) {
        Toolbox.showSimpleCustomDialog(
            this,
            this.getString(R.string.TEXT_REMOVE_SHOPCART_ITEM).format(item.title),
            this.getString(R.string.ACTION_DELETE),
            this.getString(R.string.ACTION_CANCEL),
            validate = {
                ShopCartManager(this).removeFromShopCart(item)
                shopcartArtworkAdapter.updateShopCartList(ShopCartManager(this).getShopCartItems())
                initShopCartTitle()
            })

    }


}