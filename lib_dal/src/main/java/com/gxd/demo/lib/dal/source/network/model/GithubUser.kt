package com.gxd.demo.lib.dal.source.network.model

import com.google.gson.annotations.SerializedName


data class GithubUser(
    @SerializedName("avatar_url")
    val avatarUrl: String? = null,
    @SerializedName("bio")
    val bio: Any? = null,
    @SerializedName("blog")
    val blog: String? = null,
    @SerializedName("company")
    val company: Any? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("events_url")
    val eventsUrl: String? = null,
    @SerializedName("followers")
    val followers: Int? = null,
    @SerializedName("followers_url")
    val followersUrl: String? = null,
    @SerializedName("following")
    val following: Int? = null,
    @SerializedName("following_url")
    val followingUrl: String? = null,
    @SerializedName("gists_url")
    val gistsUrl: String? = null,
    @SerializedName("gravatar_id")
    val gravatarId: String? = null,
    @SerializedName("hireable")
    val hireable: Any? = null,
    @SerializedName("html_url")
    val htmlUrl: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("location")
    val location: Any? = null,
    @SerializedName("login")
    val login: String? = null,
    @SerializedName("name")
    val name: Any? = null,
    @SerializedName("node_id")
    val nodeId: String? = null,
    @SerializedName("notification_email")
    val notificationEmail: String? = null,
    @SerializedName("organizations_url")
    val organizationsUrl: String? = null,
    @SerializedName("public_gists")
    val publicGists: Int? = null,
    @SerializedName("public_repos")
    val publicRepos: Int? = null,
    @SerializedName("received_events_url")
    val receivedEventsUrl: String? = null,
    @SerializedName("repos_url")
    val reposUrl: String? = null,
    @SerializedName("site_admin")
    val siteAdmin: Boolean? = null,
    @SerializedName("starred_url")
    val starredUrl: String? = null,
    @SerializedName("subscriptions_url")
    val subscriptionsUrl: String? = null,
    @SerializedName("twitter_username")
    val twitterUsername: Any? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("user_view_type")
    val userViewType: String? = null,
)