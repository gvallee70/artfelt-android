package partials

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.artfelt.artfelt.R
import kotlinx.android.synthetic.main.view_header.view.*

class HeaderView(
    private val context: Context,
    parent: ViewGroup,
    val backButton: Boolean,
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
        //if (user.image !== "") {
            //mHeaderProfilePic.setImageResource(User.infos!!.)
        //} else {

        if (backButton) {
            mHeaderLeftIcon.setImageResource(R.drawable.ic_arrow_left)
        } else {
            mHeaderLeftIcon.setImageResource(R.drawable.image_default_profilepic)
        }
        // }
    }

    private fun initHeaderTitle() {
        mHeaderTitle.text = context.getString(R.string.TITLE_APP_NAME)
        mHeaderTitle.textSize = 18f
    }

    private fun initHeaderRightIcon() {

    }


    private fun manageOnClickLeftIcon() {
        mHeaderLeftIcon.setOnClickListener {
            listener.onClickLeftIcon()
        }
    }

}