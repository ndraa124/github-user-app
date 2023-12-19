package id.idprogramming.githubapp.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.idprogramming.githubapp.ui.detail.DetailViewModel

class ViewModelFactory(private val username: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(username) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}