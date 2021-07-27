package ui.home.artworks.type

import android.content.Context
import com.artfelt.artfelt.R

enum class ArtworkTypeEnum(val value: Int) {
    ALL(R.string.LABEL_ARTWORK_TYPE_ALL),
    PAINTING(R.string.LABEL_ARTWORK_TYPE_PAINTING),
    PHOTO(R.string.LABEL_ARTWORK_TYPE_PHOTO),
    LITERATURE(R.string.LABEL_ARTWORK_TYPE_LITERATURE),
    SCULPTURE(R.string.LABEL_ARTWORK_TYPE_SCULPTURE),
}