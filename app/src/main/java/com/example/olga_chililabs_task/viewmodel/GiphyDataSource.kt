package com.example.olga_chililabs_task.viewmodel

import android.util.Log
import androidx.paging.*
import com.example.olga_chililabs_task.network.*
import org.greenrobot.eventbus.EventBus

//code based on https://discover.hubpages.com/technology/Working-With-Android-JetPack-Paging-Library

//handles data received from the network handler, loads initial set of images and handles loading others gradually
class GiphyDataSource(private val query: String): PositionalDataSource<GiphySearchImagePoko.GiphySearchDatum>(){

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<GiphySearchImagePoko.GiphySearchDatum>) {

        NetworkHandler.searchGiphy(
            EndpointGenerator.searchLiveImages(
                searchTerm = query,
                pageNumber = params.startPosition,
                perPage = params.loadSize
            ), {
                callback.onResult(it.data)
            },{
                EventBus.getDefault()
                    .post(it)
            })
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<GiphySearchImagePoko.GiphySearchDatum>) {

        NetworkHandler.searchGiphy(EndpointGenerator.searchLiveImages(
            searchTerm = query,
            pageNumber = params.requestedStartPosition,
            perPage = params.pageSize
        ), {
            Log.e("LOAD", "${it.pageInformation.count}")
            EventBus.getDefault()
                .post(NetworkHandler.QueryResultEventObject(query = query,
                    isSuccess = true,
                    maxPages = it.pageInformation.totalCount /
                            if (it.pageInformation.count == 0) 1 else it.pageInformation.count,
                    currentPage = it.pageInformation.offset,
                    length = it.pageInformation.count))

            callback.onResult(it.data, params.requestedStartPosition)
        },{
            Log.e("LOAD", "Error!!")
            EventBus.getDefault()
                .post(it)
        })
    }

}