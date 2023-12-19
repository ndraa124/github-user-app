package id.idprogramming.githubapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.idprogramming.githubapp.R
import id.idprogramming.githubapp.data.map.userResponseToModel
import id.idprogramming.githubapp.data.model.UserModel
import id.idprogramming.githubapp.databinding.ActivityMainBinding
import id.idprogramming.githubapp.ui.adapter.MainAdapter
import id.idprogramming.githubapp.ui.base.BaseActivity
import id.idprogramming.githubapp.ui.detail.DetailActivity
import id.idprogramming.githubapp.ui.detail.DetailActivity.Companion.USERNAME
import id.idprogramming.githubapp.util.hide
import id.idprogramming.githubapp.util.show

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val mainAdapter: MainAdapter by lazy { MainAdapter() }
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateBind(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarActionBar()
        setOnClickListener()
        setSearchView()
        setRecyclerView()

        setViewModelObserve()
    }

    private fun setToolbarActionBar() {
        setSupportActionBar(bind.layoutToolbar.toolbar)
        supportActionBar?.setTitle(R.string.user_page)
    }

    private fun setOnClickListener() {
        bind.layoutError.btnTryAgain.setOnClickListener {
            mainViewModel.getAllUser()
        }
    }

    private fun setSearchView() {
        bind.searchView.setupWithSearchBar(bind.searchBar)
        bind.searchView
            .editText
            .setOnEditorActionListener { textView, _, _ ->
                bind.searchBar.setText(textView.text)
                bind.searchView.hide()

                if (textView.text.isNullOrEmpty()) {
                    mainViewModel.getAllUser()
                } else {
                    mainViewModel.getSearchUser(textView.text.toString())
                }

                false
            }
    }

    private fun setRecyclerView() {
        bind.rvUser.layoutManager = LinearLayoutManager(this)
        bind.rvUser.apply {
            layoutManager = layoutManager
            adapter = mainAdapter
        }

        mainAdapter.setOnItemClickCallback(object : MainAdapter.ActionAdapter {
            override fun onItemClick(data: UserModel) {
                toDetailUser(data)
            }
        })
    }

    private fun setViewModelObserve() {
        mainViewModel.listUser.observe(this) { user ->
            setUserData(user?.map { userResponseToModel(it) })
        }

        mainViewModel.listSearchUser.observe(this) { user ->
            setUserData(user?.map { userResponseToModel(it) })
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        mainViewModel.message.observe(this) { message ->
            message.getContentIfNotHandled()?.let {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
            }
        }

        mainViewModel.isError.observe(this) { isError ->
            showError(isError)
        }
    }

    private fun setUserData(user: List<UserModel>?) {
        mainAdapter.addList(user)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            bind.rvUser.hide()
            bind.progressBar.show()
        } else {
            bind.rvUser.show()
            bind.progressBar.hide()
        }
    }

    private fun showError(isError: Boolean) {
        if (isError) {
            bind.rvUser.hide()
            bind.layoutError.btnTryAgain.show()
        } else {
            bind.layoutError.btnTryAgain.hide()
        }
    }

    private fun toDetailUser(data: UserModel) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
            putExtra(USERNAME, data.username)
        }

        startActivity(intent)
    }
}
