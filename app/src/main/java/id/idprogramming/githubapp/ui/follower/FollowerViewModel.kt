package id.idprogramming.githubapp.ui.follower

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.idprogramming.githubapp.data.network.ApiConfig
import id.idprogramming.githubapp.data.response.UserResponse
import id.idprogramming.githubapp.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerViewModel : ViewModel() {
    private val _listFollowers = MutableLiveData<List<UserResponse>?>()
    val listFollowers: LiveData<List<UserResponse>?> = _listFollowers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<Event<String?>>()
    val message: LiveData<Event<String?>> = _message

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun getAllFollower(username: String) {
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(call: Call<List<UserResponse>>, response: Response<List<UserResponse>>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _isError.value = false

                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listFollowers.value = responseBody
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

    fun getAllFollowing(username: String) {
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(call: Call<List<UserResponse>>, response: Response<List<UserResponse>>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _isError.value = false

                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listFollowers.value = responseBody
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

    private fun onFailure(t: Throwable) {
        _isLoading.value = false
        _isError.value = true
        _message.value = Event(t.message)
    }
}
