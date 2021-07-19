package common

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import api.models.user.infos.User
import com.artfelt.artfelt.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_header.view.*

class HeaderView(
    private val context: Context,
    parent: ViewGroup,
    private val backButton: Boolean,
    private val listener: HeaderDelegate
){

    private val view =
        LayoutInflater.from(context).inflate(R.layout.view_header, parent, false)

    var mHeaderLeftIcon = view.header_left_icon
    var mHeaderTitle = view.header_title
    var mHeaderRightIcon = view.header_right_icon

    init {
        parent.removeAllViews()
        parent.addView(view)
        initView()
    }

    private fun initView() {
        initHeaderLeftIcon()
        initHeaderTitle()
        initHeaderRightIcon()

        manageOnClickLeftIcon()
    }


    private fun initHeaderLeftIcon() {
        mHeaderLeftIcon.clipToOutline = true

        if (backButton) {
            mHeaderLeftIcon.setImageResource(R.drawable.ic_arrow_left)
        } else {
            if (User.info?.avatarUrl.isNullOrEmpty()) {
                mHeaderLeftIcon.setImageResource(R.drawable.ic_user_default)
            } else {
                Picasso.get().load(User.info?.avatarUrl).into(mHeaderLeftIcon)
            }
        }
    }

    private fun initHeaderTitle() {
        mHeaderTitle.text = context.getString(R.string.TITLE_APP_NAME)
        mHeaderTitle.textSize = 22f
    }

    private fun initHeaderRightIcon() {

    }


    private fun manageOnClickLeftIcon() {
        mHeaderLeftIcon.setOnClickListener {
            listener.onClickHeaderLeftIcon()
        }
    }

}