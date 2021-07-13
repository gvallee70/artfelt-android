package partials

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import api.models.user.infos.UserInfosResponse
import com.artfelt.artfelt.R
import kotlinx.android.synthetic.main.view_header.view.*

class HeaderView(
    private val context: Context,
    parent: ViewGroup,
    user: UserInfosResponse,
){

    private val view =
        LayoutInflater.from(context).inflate(R.layout.view_header, parent, false)

    var mHeaderProfilePic = view.header_profile_pic
    var mHeaderTitle = view.header_title
    var mHeaderShopcart = view.header_shopcart

    init {
        parent.removeAllViews()
        parent.addView(view)
        initView(user)
    }

    private fun initView(user: UserInfosResponse) {
        initHeaderProfilePic(user)
        initHeaderTitle()
        initHeaderShopcart()
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initHeaderProfilePic(user: UserInfosResponse) {
        mHeaderProfilePic.clipToOutline = true
        //if (user.image !== "") {
            //mHeaderProfilePic.setImageResource(user.image)
        //} else {
            mHeaderProfilePic.setImageResource(R.drawable.image_default_profilepic)
        // }
    }

    private fun initHeaderTitle() {
        mHeaderTitle.text = context.getString(R.string.TITLE_APP_NAME)
        mHeaderTitle.textSize = 18f
    }

    private fun initHeaderShopcart() {

    }

}