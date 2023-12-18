package com.example.job.model

data class GetAllBookmarksResponse(
    val bookmarks: List<Bookmark>,
    val status: Boolean
)