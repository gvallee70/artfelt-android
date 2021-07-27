package api.models.order

import com.google.gson.annotations.SerializedName
import managers.shopcart.ItemShopCart

data class OrderRequest(
    @SerializedName("items")
    var items: ArrayList<ItemShopCart>? = null,
)