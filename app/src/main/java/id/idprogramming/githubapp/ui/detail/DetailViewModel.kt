package id.idprogramming.githubapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.idprogramming.githubapp.data.network.ApiConfig
import id.idprogramming.githubapp.data.response.UserDetailResponse
import id.idprogramming.githubapp.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val username: String) : ViewModel() {
    private val _detailUser = MutableLiveData<UserDetailResponse?>()
    val detailUser: LiveData<UserDetailResponse?> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<Event<String?>>()
    val message: LiveData<Event<String?>> = _message

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    init {
        getDetailUser()
    }

    fun getDetailUser() {
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(call: Call<UserDetailResponse>, response: Response<UserDetailResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _isError.value = false

                    val responseBody = response.body()
                    if (responseBody != null) {
                        _detailUser.value = responseBody
                    }
                } else {
                    _isError.value = true
                    _message.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
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
