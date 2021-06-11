package api.models

import com.google.gson.annotations.SerializedName

data class UserAddress(
        @SerializedName("address_street") val street: String? = null,
        @SerializedName("address_zip_code") val zipCode: Number? = null,
        @SerializedName("address_city") val city: String? = null,
)
