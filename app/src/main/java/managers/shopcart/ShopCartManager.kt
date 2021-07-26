package managers.shopcart

import android.content.Context
import android.content.SharedPreferences
import api.models.user.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import utils.copyAndAdd
import utils.copyAndRemove


/**
 * ShopCart manager to save and fetch data to user ShopCart
 */

class ShopCartManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME.format(User.info?.id), Context.MODE_PRIVATE)
    private var mShopCartItems: ArrayList<ItemShopCart> = getShopCartItems()

    companion object {
        const val PREFS_NAME = "artfelt-shopcart-%s"
        const val SHOPCART_ITEMS = "user-shopcart-items"
    }


    fun saveToShopCart(item: ItemShopCart) {
        var newItem = item
        //remove item if the item is already in shopcart to avoid duplicates and only add quantity
        mShopCartItems.forEach {
            if(it.id == item.id) {
                newItem.quantity = newItem.quantity?.plus(it.quantity!!)
                mShopCartItems = mShopCartItems.copyAndRemove(it)
            }
        }

        mShopCartItems = mShopCartItems.copyAndAdd(newItem)

        val stringItemsList = Gson().toJson(mShopCartItems)

        prefs.edit()
            .putString(SHOPCART_ITEMS, stringItemsList)
            .commit()
    }


    fun resetShopCart() {
        prefs.edit()
            .remove(SHOPCART_ITEMS)
            .commit()
    }


    fun getShopCartItems(): ArrayList<ItemShopCart> {
        val storedShopCartItems = prefs.getString(SHOPCART_ITEMS, null)

        //return stored artworks or return empty list
        storedShopCartItems?.let {
            val type = object : TypeToken<ArrayList<ItemShopCart>>() {}.type
            return Gson().fromJson(it, type)
        }
        return ArrayList()

    }

}