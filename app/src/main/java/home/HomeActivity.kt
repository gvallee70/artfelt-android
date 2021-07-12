package home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import api.models.User
import com.artfelt.artfelt.R
import kotlinx.android.synthetic.main.activity_home.*
import partials.HeaderView

class HomeActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        this.supportActionBar!!.hide()
    }


    override fun onResume() {
        super.onResume()

        initView()
    }

    private fun initView() {
        initHeader()
    }


    private fun initHeader() {
        HeaderView(this, home_parent, User.infos!!)
    }

}