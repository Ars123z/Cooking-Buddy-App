package com.example.cookingbuddynew.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class ThumbnailItem(
    val url: String,
    val width: Int,
    val height: Int
)


@Serializable
data class Id(
    val kind: String,
    val videoId: String
)

@Serializable
data class Thumbnails(
    val default: ThumbnailItem,
    val medium: ThumbnailItem,
    val high: ThumbnailItem
)

@Serializable
data class Snippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: Thumbnails,
    val channelTitle: String,
    val liveBroadcastContent: String,
    val publishTime: String
)

@Serializable
data class ResultItem(
    val kind: String,
    val etag: String,
    val id: Id,
    val snippet: Snippet
)

@Serializable
data class ApiResponse(
    val results: List<ResultItem>
)

@Parcelize
data class Video(
    val id: String,
    val title: String,
    val thumbnailUrl: String
): Parcelable

