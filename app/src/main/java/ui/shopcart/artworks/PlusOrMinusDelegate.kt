package ui.shopcart.artworks

import models.order.ItemShopCart

interface PlusOrMinusDelegate {
    fun onClickMinus(item: ItemShopCart)
    fun onClickPlus(item: ItemShopCart)
    fun onClickRemove(item: ItemShopCart)
}