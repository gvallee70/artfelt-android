package ui.home.artworks.type

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artfelt.artfelt.R

class ArtworkTypeAdapter(
    val context: Context,
    private val selectedArtworkType: ArtworkTypeEnum? = null,
    private var artworkTypes: ArrayList<ArtworkTypeEnum>,
    private val listener: ArtworkTypeDelegate
) : RecyclerView.Adapter<ArtworkTypeViewHolder>() {

    private var artworksTypeList = ArrayList<ArtworkTypeEnum>(artworkTypes)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtworkTypeViewHolder {
        return ArtworkTypeViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_artwork_type, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: ArtworkTypeViewHolder, position: Int) {
        val artworkType = artworksTypeList[position]

        holder.bindView(artworkType)

        selectedArtworkType?.let {
            if (artworkType == ArtworkTypeEnum.ALL) {
                holder.initDefaultTypeButton(artworkType)
            }

            if (it == artworkType) {
                holder.initClickedTypeButton(artworkType)
            }

        }

    }

    override fun getItemCount(): Int {
        return artworksTypeList.size
    }

}