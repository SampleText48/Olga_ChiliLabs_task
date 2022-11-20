package com.example.olga_chililabs_task.viewmodel

import androidx.lifecycle.*
import androidx.paging.*
import com.example.olga_chililabs_task.network.GiphySearchImagePoko

//code based on https://discover.hubpages.com/technology/Working-With-Android-JetPack-Paging-Library

//handles loading the image data from created datasources, contains the initial default query of "chili"
class SearchViewModel : ViewModel() {
    lateinit var imageList: LiveData<PagedList<GiphySearchImagePoko.GiphySearchDatum>>
    var mLatestQuery: String = "chili"
    private var dataSourceFactory = DataSourceFactory("")

    init {
        loadImageData(mLatestQuery)
    }

    fun loadImageData(query: String) {
        mLatestQuery = query
        dataSourceFactory = DataSourceFactory(query)
        imageList = LivePagedListBuilder<Int, GiphySearchImagePoko.GiphySearchDatum>(
            dataSourceFactory,
            PagedList.Config.Builder()
                .setPageSize(10)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build()
        ).build()
    }
}