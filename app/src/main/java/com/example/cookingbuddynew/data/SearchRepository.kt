package com.example.cookingbuddynew.data

import com.example.cookingbuddynew.api.ApiResponse
import com.example.cookingbuddynew.api.HistoryUpdateRequest
import com.example.cookingbuddynew.api.HistoryUpdateResponse
import com.example.cookingbuddynew.network.CookingBuddyApiService
import com.example.cookingbuddynew.api.ThumbnailItem
import com.example.cookingbuddynew.api.Id
import com.example.cookingbuddynew.api.LabelApi
import com.example.cookingbuddynew.api.ResultItem
import com.example.cookingbuddynew.api.Snippet
import com.example.cookingbuddynew.api.Thumbnails


interface SearchRepository {
    suspend fun getRecipes(query: String): ApiResponse
    suspend fun updateHistory(request: HistoryUpdateRequest): HistoryUpdateResponse
    suspend fun fetchLabels(region: String): List<LabelApi>
}

class SearchRepositoryImp(
    private val searchService: CookingBuddyApiService
): SearchRepository {
    override suspend fun getRecipes(query: String): ApiResponse {
        return searchService.getRecipes(query)
    }
    override suspend fun updateHistory(request: HistoryUpdateRequest): HistoryUpdateResponse {
        return searchService.updateHistory(request)
    }
    override suspend fun fetchLabels(region: String): List<LabelApi> {
        return searchService.fetchLabels(region)
    }
}

class MockRepository: SearchRepository {
    override suspend fun updateHistory(request: HistoryUpdateRequest): HistoryUpdateResponse {
        return HistoryUpdateResponse(
            message = "success"
        )
    }
    override suspend fun fetchLabels(region: String): List<LabelApi> {
        return listOf(
            LabelApi(
                id = 1,
                name = "test",
                videos = listOf(1, 2, 3),
                region = "test",
                last_updated = "test"
            )
        )
    }
    override suspend fun getRecipes(query: String): ApiResponse {
        return ApiResponse(
            results = listOf(
                ResultItem(
                    kind = "youtube#searchResult",
                    etag = "J61iBqscoMs84cGVq3wEKUwgJVM",
                    id = Id(kind = "youtube#video", videoId = "OurrYxgvry4"),
                    snippet = Snippet(
                        publishedAt = "2023-04-18T07:12:51Z",
                        channelId = "UCipSU-s77LQLycThKjKpqSw",
                        title = "Easy And Best Mutton Biryani Recipe | Mutton Biryani ❤️ | 1 Kg Mutton Biryani | Eid Special Biryani",
                        description = "mutton #muttonrecipe #muttonbiryani #muttonbiryaniincooker #muttonbiriyani #biryani #spicybiryani #chatpatibiryani Ingredients: ...",
                        thumbnails = Thumbnails(
                            default = ThumbnailItem(
                                url = "https://i.ytimg.com/vi/OurrYxgvry4/default.jpg",
                                width = 120,
                                height = 90
                            ),
                            medium = ThumbnailItem(
                                url = "https://i.ytimg.com/vi/OurrYxgvry4/mqdefault.jpg",
                                width = 320,
                                height = 180
                            ),
                            high = ThumbnailItem(
                                url = "https://i.ytimg.com/vi/OurrYxgvry4/hqdefault.jpg",
                                width = 480,
                                height = 360
                            )
                        ),
                        channelTitle = "Cook with Lubna",
                        liveBroadcastContent = "none",
                        publishTime = "2023-04-18T07:12:51Z"
                    )
                ),
                ResultItem(
                    kind = "youtube#searchResult",
                    etag = "J61iBqscoMs84cGVq3wEKUwgJVM",
                    id = Id(kind = "youtube#video", videoId = "OurrYxgvry4"),
                    snippet = Snippet(
                        publishedAt = "2023-04-18T07:12:51Z",
                        channelId = "UCipSU-s77LQLycThKjKpqSw",
                        title = "Easy And Best Mutton Biryani Recipe | Mutton Biryani ❤️ | 1 Kg Mutton Biryani | Eid Special Biryani",
                        description = "mutton #muttonrecipe #muttonbiryani #muttonbiryaniincooker #muttonbiriyani #biryani #spicybiryani #chatpatibiryani Ingredients: ...",
                        thumbnails = Thumbnails(
                            default = ThumbnailItem(
                                url = "https://i.ytimg.com/vi/OurrYxgvry4/default.jpg",
                                width = 120,
                                height = 90
                            ),
                            medium = ThumbnailItem(
                                url = "https://i.ytimg.com/vi/OurrYxgvry4/mqdefault.jpg",
                                width = 320,
                                height = 180
                            ),
                            high = ThumbnailItem(
                                url = "https://i.ytimg.com/vi/OurrYxgvry4/hqdefault.jpg",
                                width = 480,
                                height = 360
                            )
                        ),
                        channelTitle = "Cook with Lubna",
                        liveBroadcastContent = "none",
                        publishTime = "2023-04-18T07:12:51Z"
                    )
                ),
                ResultItem(
                    kind = "youtube#searchResult",
                    etag = "J61iBqscoMs84cGVq3wEKUwgJVM",
                    id = Id(kind = "youtube#video", videoId = "OurrYxgvry4"),
                    snippet = Snippet(
                        publishedAt = "2023-04-18T07:12:51Z",
                        channelId = "UCipSU-s77LQLycThKjKpqSw",
                        title = "Easy And Best Mutton Biryani Recipe | Mutton Biryani ❤️ | 1 Kg Mutton Biryani | Eid Special Biryani",
                        description = "mutton #muttonrecipe #muttonbiryani #muttonbiryaniincooker #muttonbiriyani #biryani #spicybiryani #chatpatibiryani Ingredients: ...",
                        thumbnails = Thumbnails(
                            default = ThumbnailItem(
                                url = "https://i.ytimg.com/vi/OurrYxgvry4/default.jpg",
                                width = 120,
                                height = 90
                            ),
                            medium = ThumbnailItem(
                                url = "https://i.ytimg.com/vi/OurrYxgvry4/mqdefault.jpg",
                                width = 320,
                                height = 180
                            ),
                            high = ThumbnailItem(
                                url = "https://i.ytimg.com/vi/OurrYxgvry4/hqdefault.jpg",
                                width = 480,
                                height = 360
                            )
                        ),
                        channelTitle = "Cook with Lubna",
                        liveBroadcastContent = "none",
                        publishTime = "2023-04-18T07:12:51Z"
                    )
                ),
                ResultItem(
                    kind = "youtube#searchResult",
                    etag = "J61iBqscoMs84cGVq3wEKUwgJVM",
                    id = Id(kind = "youtube#video", videoId = "OurrYxgvry4"),
                    snippet = Snippet(
                        publishedAt = "2023-04-18T07:12:51Z",
                        channelId = "UCipSU-s77LQLycThKjKpqSw",
                        title = "Easy And Best Mutton Biryani Recipe | Mutton Biryani ❤️ | 1 Kg Mutton Biryani | Eid Special Biryani",
                        description = "mutton #muttonrecipe #muttonbiryani #muttonbiryaniincooker #muttonbiriyani #biryani #spicybiryani #chatpatibiryani Ingredients: ...",
                        thumbnails = Thumbnails(
                            default = ThumbnailItem(
                                url = "https://i.ytimg.com/vi/OurrYxgvry4/default.jpg",
                                width = 120,
                                height = 90
                            ),
                            medium = ThumbnailItem(
                                url = "https://i.ytimg.com/vi/OurrYxgvry4/mqdefault.jpg",
                                width = 320,
                                height = 180
                            ),
                            high = ThumbnailItem(
                                url = "https://i.ytimg.com/vi/OurrYxgvry4/hqdefault.jpg",
                                width = 480,
                                height = 360
                            )
                        ),
                        channelTitle = "Cook with Lubna",
                        liveBroadcastContent = "none",
                        publishTime = "2023-04-18T07:12:51Z"
                    )
                ),
                ResultItem(
                    kind = "youtube#searchResult",
                    etag = "J61iBqscoMs84cGVq3wEKUwgJVM",
                    id = Id(kind = "youtube#video", videoId = "OurrYxgvry4"),
                    snippet = Snippet(
                        publishedAt = "2023-04-18T07:12:51Z",
                        channelId = "UCipSU-s77LQLycThKjKpqSw",
                        title = "Easy And Best Mutton Biryani Recipe | Mutton Biryani ❤️ | 1 Kg Mutton Biryani | Eid Special Biryani",
                        description = "mutton #muttonrecipe #muttonbiryani #muttonbiryaniincooker #muttonbiriyani #biryani #spicybiryani #chatpatibiryani Ingredients: ...",
                        thumbnails = Thumbnails(
                            default = ThumbnailItem(
                                url = "https://i.ytimg.com/vi/OurrYxgvry4/default.jpg",
                                width = 120,
                                height = 90
                            ),
                            medium = ThumbnailItem(
                                url = "https://i.ytimg.com/vi/OurrYxgvry4/mqdefault.jpg",
                                width = 320,
                                height = 180
                            ),
                            high = ThumbnailItem(
                                url = "https://i.ytimg.com/vi/OurrYxgvry4/hqdefault.jpg",
                                width = 480,
                                height = 360
                            )
                        ),
                        channelTitle = "Cook with Lubna",
                        liveBroadcastContent = "none",
                        publishTime = "2023-04-18T07:12:51Z"
                    )
                )
            )
        )
    }
}
