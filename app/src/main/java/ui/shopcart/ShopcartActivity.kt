package ui.shopcart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import api.ArtfeltClient
import api.requests.order.OrderRequest
import com.artfelt.artfelt.R
import ui.common.header.HeaderDelegate
import ui.common.headericon.HeaderLeftIconEnum
import ui.common.headericon.HeaderRightIconEnum
import ui.common.header.HeaderView
import ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_shopcart.*
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.view_header.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import models.order.ItemShopCart
import managers.shopcart.ShopCartManager
import ui.shopcart.artworks.PlusOrMinusDelegate
import ui.shopcart.artworks.ShopcartArtworkAdapter
import utils.Toolbox
import utils.hide
import utils.navigateTo
import utils.show
import utils.transition.TransitionEnum

class ShopcartActivity: AppCompatActivity(), HeaderDelegate, PlusOrMinusDelegate {

    private lateinit var shopcartArtworkAdapter: ShopcartArtworkAdapter
    private lateinit var shopcartArtworks: ArrayList<ItemShopCart>
    private var totalPrice: Int? = null
    private var shopcartQuantity: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopcart)

        this.supportActionBar!!.hide()

        shopcartArtworks = ShopCartManager(this).getShopCartItems()

        initShopcartArtworksRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    override fun onPause() {
        super.onPause()

        shopcartArtworks.forEach {
            if(it.quantity == 0) {
                manageOnRemoveItem(it)
            }
        }
    }

    private fun initView() {
        initHeader()
        initShopCartTitle()
        initOrderButton()
        showOrHideTrashButton()
    }

    private fun initHeader() {
        HeaderView(this, block_header_shopcart, HeaderLeftIconEnum.CLOSE, HeaderRightIconEnum.TRASH,this)
    }

    private fun initShopCartTitle() {
        initQuantityAndTotalPrice()

        title_artworks_shopcart.textSize = 22f

        if(shopcartArtworks.isNotEmpty()) {
            title_artworks_shopcart.text = getString(R.string.LABEL_SHOPCART_TITLE).format(totalPrice)
        } else {
            title_artworks_shopcart.text = getString(R.string.LABEL_SHOPCART_EMPTY)
        }
    }

    private fun initShopcartArtworksRecyclerView() {
        shopcartArtworkAdapter = ShopcartArtworkAdapter(this, shopcartArtworks, this)

        recycler_view_artworks_shopcart.removeAllViews()
        recycler_view_artworks_shopcart.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view_artworks_shopcart.adapter = shopcartArtworkAdapter
    }


    private fun initOrderButton() {
        if (shopcartQuantity!! <= 0) {
            button_shopcart_order.hide()
        } else {
            button_shopcart_order.show()
        }

        textView_shopcart_order.text = getString(R.string.ACTION_ORDER)
        textView_shopcart_order.textSize = 16f
        hideLoadingOrderButton()
        manageOnClickOrderButton()
    }

    private fun initQuantityAndTotalPrice() {
        shopcartArtworks = ShopCartManager(this).getShopCartItems()

        shopcartQuantity = 0
        totalPrice = 0

        shopcartArtworks.forEach {
            if (it.maxQuantity == 0) {
                Toolbox.showSimpleCustomDialog(
                    this,
                    getString(R.string.TEXT_NOT_AVAILABLE_SHOPCART_ITEM).format(it.title),
                    null,
                    getString(R.string.ACTION_OK),
                    validate = {}
                )
                manageOnRemoveItem(it)
            }
            shopcartQuantity = shopcartQuantity?.plus(it.quantity!!)
            totalPrice = totalPrice?.plus((it.price!! * it.quantity!!))
        }
    }

    private fun showLoadingOrderButton() {
        textView_shopcart_order.hide()
        progressBar_order.show()
    }

    private fun hideLoadingOrderButton() {
        textView_shopcart_order.show()
        progressBar_order.hide()
    }


    private fun showOrHideTrashButton() {
        if(shopcartQuantity == 0) {
            block_header_shopcart.header_right_icon.hide()
        } else {
            block_header_shopcart.header_right_icon.show()
        }
    }


    private fun manageOnClickOrderButton() {
        textView_shopcart_order.setOnClickListener {
            createOrderAPICall()
        }
    }

    private fun manageOnRemoveItem(item: ItemShopCart) {
        ShopCartManager(this).removeFromShopCart(item)
        shopcartArtworkAdapter.updateShopCartList(ShopCartManager(this).getShopCartItems())
        initShopCartTitle()
        initOrderButton()
    }

    private fun manageOnResetShopCart() {
        ShopCartManager(this).resetShopCart()
        shopcartArtworkAdapter.updateShopCartList(ShopCartManager(this).getShopCartItems())
        initShopCartTitle()
        initOrderButton()
        showOrHideTrashButton()
        navigateTo(HomeActivity(), finish = true, transition = TransitionEnum.BOTTOM)
    }

    override fun onClickHeaderLeftIcon() {
        finish()
        overridePendingTransition(R.transition.slide_in_down, R.transition.slide_out_down)
    }

    override fun onClickHeaderRightIcon() {
        Toolbox.showSimpleCustomDialog(
            this,
            this.getString(R.string.TEXT_RESET_SHOPCART),
            this.getString(R.string.ACTION_RESET),
            this.getString(R.string.ACTION_CANCEL)
        ) {
            manageOnResetShopCart()
        }
    }

    override fun onClickMinus(item: ItemShopCart) {
        ShopCartManager(this).saveToShopCart(item)
        initShopCartTitle()
        initOrderButton()
        showOrHideTrashButton()
    }

    override fun onClickPlus(item: ItemShopCart) {
        ShopCartManager(this).saveToShopCart(item)
        initShopCartTitle()
        initOrderButton()
        showOrHideTrashButton()
    }

    override fun onClickRemove(item: ItemShopCart) {
        Toolbox.showSimpleCustomDialog(
            this,
            this.getString(R.string.TEXT_REMOVE_SHOPCART_ITEM).format(item.title),
            this.getString(R.string.ACTION_DELETE),
            this.getString(R.string.ACTION_CANCEL)
        ) {
            manageOnRemoveItem(item)
        }

    }


    private fun createOrderAPICall() {
        var createOrderRequest = OrderRequest(
            items = shopcartArtworks
        )

        GlobalScope.launch(Dispatchers.Main) {
            showLoadingOrderButton()

            try {
                val createOrderResponse = ArtfeltClient().getApiService(this@ShopcartActivity).createOrder(createOrderRequest)

                if (createOrderResponse.isSuccessful) {
                    createOrderResponse.body()?.let {
                        Toolbox.showSuccessDialog(this@ShopcartActivity, getString(R.string.TEXT_CREATE_ORDER_SUCCESS))
                        ShopCartManager(this@ShopcartActivity).resetShopCart()
                        navigateTo(HomeActivity(), true, TransitionEnum.BOTTOM)
                    }
                }
            } catch (e: Exception) {
                hideLoadingOrderButton()
                Toolbox.showErrorDialog(this@ShopcartActivity, getString(R.string.TEXT_CREATE_ORDER_API_ERROR))
                navigateTo(HomeActivity(), true, TransitionEnum.BOTTOM)
            }
        }
    }


}