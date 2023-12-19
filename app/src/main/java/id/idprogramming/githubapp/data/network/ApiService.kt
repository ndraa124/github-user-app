package id.idprogramming.githubapp.data.network

import id.idprogramming.githubapp.data.response.UserDetailResponse
import id.idprogramming.githubapp.data.response.UserResponse
import id.idprogramming.githubapp.data.response.UserSearchResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    fun getAllUsers(): Call<List<UserResponse>>

    @GET("search/users")
    fun getSearchUser(
        @Query("q") keyword: String,
    ): Call<UserSearchResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String,
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String,
    ): Call<List<UserResponse>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String,
    ): Call<List<UserResponse>>
}
