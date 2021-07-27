package models.order

import com.google.gson.annotations.SerializedName

data class ItemShopCart(
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("price")
    var price: Int? = null,

    @SerializedName("quantity")
    var quantity: Int? = null,

    @SerializedName("maxQuantity")
    var maxQuantity: Int? = null,

    @SerializedName("image_url")
    var imageUrl: String? = null,
)