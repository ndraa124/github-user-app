package id.idprogramming.githubapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetailModel(
    val id: Int,
    val name: String?,
    val username: String,
    val email: String?,
    val urlAvatar: String,
    val followers: Int,
    val followings: Int
) : Parcelable
