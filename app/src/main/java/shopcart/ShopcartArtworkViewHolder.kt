package shopcart

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artfelt.artfelt.R
import com.google.android.material.button.MaterialButton
import home.artworks.ArtworkDelegate
import kotlinx.android.synthetic.main.item_artwork.view.*
import kotlinx.android.synthetic.main.item_artwork_shopcart.view.*
import managers.shopcart.ItemShopCart
import managers.shopcart.ShopCartManager
import utils.Toolbox
import utils.setImageURL

class ShopcartArtworkViewHolder(
    val container: View,
    private val listener: PlusOrMinusDelegate
) : RecyclerView.ViewHolder(container) {

    private var mImage: ImageView = container.imageView_artwork_shopcart
    private var mTextViewTitle: TextView = container.textView_artwork_title_shopcart
    private var mTextViewPrice: TextView = container.textView_artwork_price_shopcart
    private var mTextViewQuantity: TextView = container.textView_quantity_artwork_shopcart
    private var mButtonMinus: MaterialButton = container.button_minus_quantity
    private var mButtonPlus: MaterialButton = container.button_plus_quantity


    fun bindView(item: ItemShopCart) {
        initView(item)
    }

    private fun initView(item: ItemShopCart) {
        initImage(item)
        initTitle(item)
        initPrice(item)

        initQuantityPlusMinusView(item)
    }


    private fun initImage(item: ItemShopCart) {
        mImage.clipToOutline = true

        if (item.imageUrl.isNullOrEmpty()) {
            mImage.setImageResource(R.drawable.ic_image_placeholder)
        } else {
            mImage.setImageURL(item.imageUrl!!)
        }
    }

    private fun initTitle(item: ItemShopCart) {
        mTextViewTitle.text = item.title
    }

    private fun initPrice(item: ItemShopCart) {
        mTextViewPrice.text = container.context.getString(R.string.LABEL_ARTWORK_PRICE).format(item.price)
    }

    private fun initQuantityPlusMinusView(item: ItemShopCart) {
        mTextViewQuantity.text = "${item.quantity}"

        togglePlusOrMinusButton(item)

        manageOnClickMinusButton(item)
        manageOnClickPlusButton(item)
    }


    private fun togglePlusOrMinusButton(item: ItemShopCart) {
        toggleMinusButton(item)
        togglePlusButton(item)
    }


    private fun toggleMinusButton(item: ItemShopCart) {
        if (mTextViewQuantity.text == "0") {
            disableMinusButton()
            listener.onClickRemove(item)
        } else {
            enableMinusButton()
        }
    }

    private fun enableMinusButton() {
        mButtonMinus.isEnabled = true
        mButtonMinus.iconTint = container.context.resources.getColorStateList(R.color.primaryColor)
    }

    private fun disableMinusButton() {
        mButtonMinus.isEnabled = false
        mButtonMinus.iconTint = container.context.resources.getColorStateList(R.color.gray)
    }


    private fun togglePlusButton(item: ItemShopCart) {
        if (mTextViewQuantity.text!! == "${item.maxQuantity}") {
            disablePlusButton()
        } else {
            enablePlusButton()
        }


    }

    private fun enablePlusButton() {
        mButtonPlus.isEnabled = true
        mButtonPlus.iconTint = container.context.resources.getColorStateList(R.color.primaryColor)
    }

    private fun disablePlusButton() {
        mButtonPlus.isEnabled = false
        mButtonPlus.iconTint = container.context.resources.getColorStateList(R.color.gray)
    }

    private fun manageOnClickMinusButton(item: ItemShopCart) {
        mButtonMinus.setOnClickListener {
            mTextViewQuantity.text = "${item.quantity?.minus(1)}"
            togglePlusOrMinusButton(item)
            item.quantity = -1

            listener.onClickMinus(item)
        }
    }

    private fun manageOnClickPlusButton(item: ItemShopCart) {
        mButtonPlus.setOnClickListener {
            mTextViewQuantity.text = "${item.quantity?.plus(1)}"
            togglePlusOrMinusButton(item)
            item.quantity = 1

            listener.onClickPlus(item)
        }
    }


    /* fun manageOnClickArtwork(artwork: Artwork) {
         container.setOnClickListener {
             listener.onClickItem(artwork)
         }
     }*/

}