package artwork

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import api.models.user.Artist
import com.artfelt.artfelt.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_artist_cell.view.*
import kotlinx.android.synthetic.main.view_header.view.*
import utils.formatddMMMMYYYY
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class ArtistView(
    private val context: Context,
    parent: ViewGroup,
    private val artist: Artist
){

    private val view =
        LayoutInflater.from(context).inflate(R.layout.view_artist_cell, parent, false)

    var mArtistProfilePicture = view.imageView_artist_profile_pic
    var mArtistName = view.textView_artist_name
    var mArtistAddress = view.textView_artist_address
    var mArtistMemberDate = view.textView_artist_member_date


    init {
        parent.removeAllViews()
        parent.addView(view)
        initView()
    }

    private fun initView() {
        initArtistProfilePicture()
        initArtistName()
        initArtistAddress()
        initArtistMemberDate()
    }


    private fun initArtistProfilePicture() {
        mArtistProfilePicture.clipToOutline = true

        if (artist.avatarUrl.isNullOrEmpty()) {
            mArtistProfilePicture.setImageResource(R.drawable.ic_user_default)
        } else {
            Picasso.get().load(artist.avatarUrl).into(mArtistProfilePicture)
        }
    }

    private fun initArtistName() {
        mArtistName.text = "${artist.firstName} ${artist.lastName}"
        mArtistName.textSize = 18f
    }

    private fun initArtistAddress() {
        mArtistAddress.text = "${artist.city.capitalize()} ${artist.zipCode}"
        mArtistAddress.textSize = 12f
    }

    private fun initArtistMemberDate() {
        mArtistMemberDate.text = context.getString(R.string.LABEL_ARTIST_MEMBER_SINCE).format(artist.creationDate.formatddMMMMYYYY())
        mArtistMemberDate.typeface = Typeface.defaultFromStyle(Typeface.ITALIC)
    }


}