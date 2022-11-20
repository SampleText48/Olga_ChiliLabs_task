package com.example.olga_chililabs_task.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.example.olga_chililabs_task.network.GiphySearchImagePoko

//code based on https://discover.hubpages.com/technology/Working-With-Android-JetPack-Paging-Library

//creates datasource objects for SearchViewModel class
class DataSourceFactory(query: String): DataSource.Factory<Int, GiphySearchImagePoko.GiphySearchDatum>(){
    val data = MutableLiveData<GiphyDataSource>()
    var dataSource = GiphyDataSource(query)

    override fun create(): DataSource<Int, GiphySearchImagePoko.GiphySearchDatum> {
        data.postValue(dataSource)
        return dataSource
    }
}