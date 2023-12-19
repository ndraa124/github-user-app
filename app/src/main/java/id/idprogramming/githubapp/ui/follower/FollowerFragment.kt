package id.idprogramming.githubapp.ui.follower

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.idprogramming.githubapp.data.map.userResponseToModel
import id.idprogramming.githubapp.data.model.UserModel
import id.idprogramming.githubapp.databinding.FragmentFollowerBinding
import id.idprogramming.githubapp.ui.adapter.MainAdapter
import id.idprogramming.githubapp.ui.base.BaseFragment
import id.idprogramming.githubapp.util.hide
import id.idprogramming.githubapp.util.show

class FollowerFragment : BaseFragment<FragmentFollowerBinding>() {

    private val mainAdapter: MainAdapter by lazy { MainAdapter() }
    private val viewModel: FollowerViewModel by viewModels()

    private var position: Int = 0
    private var username: String? = ""

    override fun createLayout(inflater: LayoutInflater, container: ViewGroup?): FragmentFollowerBinding =
        FragmentFollowerBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setArgument()
        setOnClickListener()
        setRecyclerView()
        setViewModelProvider()
        setViewModelObserve()
    }

    private fun setArgument() {
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
    }

    private fun setOnClickListener() {
        bind.layoutError.btnTryAgain.setOnClickListener {
            viewModel.getAllFollower(username!!)
        }
    }

    private fun setRecyclerView() {
        bind.rvFollower.layoutManager = LinearLayoutManager(requireContext())
        bind.rvFollower.apply {
            layoutManager = layoutManager
            adapter = mainAdapter
        }

        mainAdapter.setOnItemClickCallback(object : MainAdapter.ActionAdapter {
            override fun onItemClick(data: UserModel) {
                Toast.makeText(requireContext(), data.username, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setViewModelProvider() {
        if (position == 1) {
            viewModel.getAllFollower(username!!)
        } else {
            viewModel.getAllFollowing(username!!)
        }
    }

    private fun setViewModelObserve() {
        viewModel.listFollowers.observe(viewLifecycleOwner) { user ->
            setUserData(user?.map { userResponseToModel(it) })
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.message.observe(viewLifecycleOwner) { message ->
            message.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isError.observe(viewLifecycleOwner) { isError ->
            showError(isError)
        }
    }

    private fun setUserData(user: List<UserModel>?) {
        mainAdapter.addList(user)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            bind.rvFollower.hide()
            bind.progressBar.show()
        } else {
            bind.rvFollower.show()
            bind.progressBar.hide()
        }
    }

    private fun showError(isError: Boolean) {
        if (isError) {
            bind.layoutError.btnTryAgain.show()
        } else {
            bind.layoutError.btnTryAgain.hide()
        }
    }

    companion object {
        var ARG_POSITION = "1"
        var ARG_USERNAME = ""
    }
}