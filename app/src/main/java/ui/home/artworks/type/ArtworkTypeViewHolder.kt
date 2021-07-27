package ui.home.artworks.type

import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.artfelt.artfelt.R
import kotlinx.android.synthetic.main.item_artwork_type.view.*

class ArtworkTypeViewHolder(
    val container: View,
    private val listener: ArtworkTypeDelegate
    ) : RecyclerView.ViewHolder(container) {

    private var mTypeButton: Button = container.button_artwork_type

    fun bindView(artworkType: ArtworkTypeEnum) {
        if(artworkType == ArtworkTypeEnum.ALL) {
            initClickedTypeButton(artworkType)
        } else {
            initDefaultTypeButton(artworkType)
        }

        manageOnClickArtworkType(artworkType)
    }

    fun initDefaultTypeButton(artworkType: ArtworkTypeEnum) {
        mTypeButton.setTextColor(container.context.resources.getColor(R.color.primaryColor))
        mTypeButton.setBackgroundResource(R.drawable.background_primary_radius_white)
        mTypeButton.text = container.context.getString(artworkType.value)
    }

    fun initClickedTypeButton(artworkType: ArtworkTypeEnum) {
        mTypeButton.setTextColor(container.context.resources.getColor(R.color.white))
        mTypeButton.setBackgroundResource(R.drawable.background_radius_primary)
        mTypeButton.text = container.context.getString(artworkType.value)
    }


    private fun manageOnClickArtworkType(artworkType: ArtworkTypeEnum) {
        container.setOnClickListener {
            listener.onClickArtworkType(artworkType)
        }
    }
}

