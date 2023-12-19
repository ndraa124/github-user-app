package id.idprogramming.githubapp.ui.detail

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import id.idprogramming.githubapp.R
import id.idprogramming.githubapp.data.map.userDetailResponseToModel
import id.idprogramming.githubapp.data.model.UserDetailModel
import id.idprogramming.githubapp.databinding.ActivityDetailBinding
import id.idprogramming.githubapp.ui.adapter.TabViewAdapter
import id.idprogramming.githubapp.ui.base.BaseActivity
import id.idprogramming.githubapp.util.ViewModelFactory
import id.idprogramming.githubapp.util.show

class DetailActivity : BaseActivity<ActivityDetailBinding>() {

    private val tabAdapter: TabViewAdapter by lazy {
        TabViewAdapter(this@DetailActivity)
    }
    private val detailViewModel: DetailViewModel by viewModels(factoryProducer = {
        ViewModelFactory(intent.getStringExtra(USERNAME)!!)
    })

    override fun onCreateBind(): ActivityDetailBinding =
        ActivityDetailBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        setOnClickListener()

        setViewModelObserve()
    }

    private fun setToolbarActionBar() {
        setSupportActionBar(bind.layoutToolbar.toolbar)
        supportActionBar?.setTitle(R.string.user_detail_page)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setOnClickListener() {
        bind.layoutToolbar.toolbar.setNavigationOnClickListener {
            finish()
        }

        bind.layoutError.btnTryAgain.setOnClickListener {
            detailViewModel.getDetailUser()
        }
    }

    private fun setTabView() {
        tabAdapter.username = intent.getStringExtra(USERNAME)!!

        bind.viewPager.adapter = tabAdapter
        TabLayoutMediator(bind.tabs, bind.viewPager) { tab, i ->
            val total: Int = if (i == 0) TOT_FOLLOWER else TOT_FOLLOWING
            tab.text = resources.getString(TAB_TITLES[i], total)
        }.attach()

        val hViewPager = bind.viewPager.layoutParams
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            hViewPager.height = 0
        } else {
            hViewPager.height = 420
        }
    }

    private fun setViewModelObserve() {
        detailViewModel.detailUser.observe(this) { user ->
            if (user != null) {
                setUserData(userDetailResponseToModel(user))
            }
        }

        detailViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        detailViewModel.message.observe(this) { message ->
            message.getContentIfNotHandled()?.let {
                Toast.makeText(this@DetailActivity, it, Toast.LENGTH_SHORT).show()
            }
        }

        detailViewModel.isError.observe(this) { isError ->
            showError(isError)
        }
    }

    private fun setUserData(data: UserDetailModel) {
        Glide.with(this).load(data.urlAvatar).into(bind.ivProfile)

        TOT_FOLLOWER = data.followers
        TOT_FOLLOWING = data.followings

        bind.apply {
            tvName.text = data.name ?: "-"
            tvUsername.text = data.username
        }

        showDetail()
        setTabView()
    }

    private fun showLoading(isLoading: Boolean) {
        bind.progressBar.isVisible = isLoading
    }

    private fun showError(isError: Boolean) {
        bind.layoutError.btnTryAgain.isVisible = isError
    }

    private fun showDetail() {
        bind.apply {
            ivProfile.show()
            tvUsername.show()
            tvName.show()
            tabs.show()
            viewPager.show()
        }
    }

    companion object {
        const val USERNAME = "username"
        var TOT_FOLLOWER = 0
        var TOT_FOLLOWING = 0

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers, R.string.tab_following
        )
    }
}
