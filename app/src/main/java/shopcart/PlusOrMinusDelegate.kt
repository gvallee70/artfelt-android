package shopcart

import android.content.ClipData
import managers.shopcart.ItemShopCart

interface PlusOrMinusDelegate {
    fun onClickMinus(item: ItemShopCart)
    fun onClickPlus(item: ItemShopCart)
    fun onClickRemove(item: ItemShopCart)
}