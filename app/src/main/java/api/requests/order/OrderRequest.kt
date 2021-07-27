package api.requests.order

import com.google.gson.annotations.SerializedName
import models.order.ItemShopCart

data class OrderRequest(
    @SerializedName("items")
    var items: ArrayList<ItemShopCart>? = null,
)