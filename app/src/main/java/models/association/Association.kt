package models.association

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Association(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("name")
    val name: String? = null,
): Serializable
