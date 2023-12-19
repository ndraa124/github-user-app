package id.idprogramming.githubapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.idprogramming.githubapp.data.network.ApiConfig
import id.idprogramming.githubapp.data.response.UserResponse
import id.idprogramming.githubapp.data.response.UserSearchResponse
import id.idprogramming.githubapp.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _listUser = MutableLiveData<List<UserResponse>?>()
    val listUser: LiveData<List<UserResponse>?> = _listUser

    private val _listSearchUser = MutableLiveData<List<UserResponse>?>()
    val listSearchUser: LiveData<List<UserResponse>?> = _listSearchUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<Event<String?>>()
    val message: LiveData<Event<String?>> = _message

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    init {
        getAllUser()
    }

    fun getAllUser() {
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().getAllUsers()
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(call: Call<List<UserResponse>>, response: Response<List<UserResponse>>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _isError.value = false

                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listUser.value = responseBody
                    }
                } else {
                    _isError.value = true
                    _message.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun getSearchUser(keyword: String) {
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().getSearchUser(keyword)
        client.enqueue(object : Callback<UserSearchResponse> {
            override fun onResponse(call: Call<UserSearchResponse>, response: Response<UserSearchResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _isError.value = false

                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listSearchUser.value = responseBody.items
                    }
                } else {
                    if (response.code() == 422) {
                        _message.value = Event("Keyword is empty")
                    } else {
                        _isError.value = true
                        _message.value = Event(response.message())
                    }
                }
            }

            override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    private fun onFailure(t: Throwable) {
        _isLoading.value = false
        _isError.value = true
        _message.value = Event(t.localizedMessage)
    }
}
