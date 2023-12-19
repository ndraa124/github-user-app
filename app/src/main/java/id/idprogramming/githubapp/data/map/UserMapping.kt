package id.idprogramming.githubapp.data.map

import id.idprogramming.githubapp.data.model.UserDetailModel
import id.idprogramming.githubapp.data.model.UserModel
import id.idprogramming.githubapp.data.response.UserDetailResponse
import id.idprogramming.githubapp.data.response.UserResponse

fun userResponseToModel(data: UserResponse): UserModel =
    UserModel(
        id = data.id,
        username = data.login,
        urlAvatar = data.avatarUrl
    )

fun userDetailResponseToModel(data: UserDetailResponse): UserDetailModel =
    UserDetailModel(
        id = data.id,
        name = data.name,
        username = data.login,
        email = data.email,
        urlAvatar = data.avatarUrl,
        followers = data.followers,
        followings = data.following
    )