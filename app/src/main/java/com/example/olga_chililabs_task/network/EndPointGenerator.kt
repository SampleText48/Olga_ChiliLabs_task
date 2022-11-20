package com.example.olga_chililabs_task.network

import java.util.*

//code based on https://discover.hubpages.com/technology/Working-With-Android-JetPack-Paging-Library

//connects with the Giphy API image search endpoint
object EndpointGenerator{
    fun searchLiveImages(
        pageNumber: Int,
        perPage: Int = 10,
        searchTerm: String
    ): String {
        return "$SEARCH_GIPHY?limit=$perPage&q=$searchTerm&offset=$pageNumber&api_key=$GIPHY_API_KEY&lang=${
            Locale.getDefault().language}"
    }
}