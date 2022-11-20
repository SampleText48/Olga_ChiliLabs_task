package com.example.olga_chililabs_task.network

import android.content.*
import com.android.volley.*
import com.android.volley.toolbox.*
import com.google.gson.*

//code based on https://discover.hubpages.com/technology/Working-With-Android-JetPack-Paging-Library

//handles sending search requests to Giphy API and receiving responses
object NetworkHandler {
    private lateinit var mRequestQueue: RequestQueue
    private lateinit var mJsonParser: Gson

    private var isInitialized: Boolean = false

    fun initialize(context: Context) {
        if (!isInitialized) {
            mRequestQueue = Volley.newRequestQueue(context)
            mJsonParser = GsonBuilder().create()
            isInitialized = true
        }
    }

    fun searchGiphy(query: String,
                    successCallback: (GiphySearchImagePoko) -> Unit,
                    failureCallback: (VolleyError) -> Unit){
        mRequestQueue.add(StringRequest(
            query,
            {
                val result = mJsonParser.fromJson(it, GiphySearchImagePoko::class.java)
                successCallback(result)
            },
            failureCallback
        ))
    }

    data class QueryResultEventObject(val query: String,
                                      val isSuccess: Boolean,
                                      val maxPages: Int,
                                      val currentPage: Int,
                                      val length: Int)
}