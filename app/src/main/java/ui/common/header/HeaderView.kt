package ui.common.header

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import models.user.User
import com.artfelt.artfelt.R
import kotlinx.android.synthetic.main.view_header.view.*
import ui.common.headericon.HeaderLeftIconEnum
import ui.common.headericon.HeaderRightIconEnum
import utils.setImageURL

class HeaderView(
    private val context: Context,
    parent: ViewGroup,
    private var leftIcon: HeaderLeftIconEnum,
    private var rightIcon: HeaderRightIconEnum,
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
        manageOnClickRightIcon()
    }


    private fun initHeaderLeftIcon() {
        mHeaderLeftIcon.clipToOutline = true

        when(leftIcon) {
            HeaderLeftIconEnum.BACK -> mHeaderLeftIcon.setImageResource(R.drawable.ic_arrow_left)
            HeaderLeftIconEnum.CLOSE -> mHeaderLeftIcon.setImageResource(R.drawable.ic_close)
            HeaderLeftIconEnum.PROFILE -> {
                if (User.info?.avatarUrl.isNullOrEmpty()) {
                    mHeaderLeftIcon.setImageResource(R.drawable.ic_user_default)
                } else {
                    mHeaderLeftIcon.setImageURL(User.info?.avatarUrl!!)
                }
            }
        }
    }

    private fun initHeaderTitle() {
        mHeaderTitle.text = context.getString(R.string.TITLE_APP_NAME)
        mHeaderTitle.textSize = 22f
    }

    private fun initHeaderRightIcon() {
        mHeaderRightIcon.clipToOutline = true

        when(rightIcon) {
            HeaderRightIconEnum.TRASH -> mHeaderRightIcon.setImageResource(R.drawable.ic_trash)
            HeaderRightIconEnum.SHOPCART -> mHeaderRightIcon.setImageResource(R.drawable.ic_shopcart)
        }
    }


    private fun manageOnClickLeftIcon() {
        mHeaderLeftIcon.setOnClickListener {
            listener.onClickHeaderLeftIcon()
        }
    }


    private fun manageOnClickRightIcon() {
        mHeaderRightIcon.setOnClickListener {
            listener.onClickHeaderRightIcon()
        }
    }

}