package com.example.job.model

data class SearchResponse(
    val searchResults: List<SearchResult>,
    val status: Boolean
)