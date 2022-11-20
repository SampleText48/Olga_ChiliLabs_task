package com.example.olga_chililabs_task.network

import com.google.gson.annotations.*

//code based on https://discover.hubpages.com/technology/Working-With-Android-JetPack-Paging-Library

//JSON parsing to interpret data received from Giphy API
data class GiphySearchImagePoko(
    @Expose
    @SerializedName("data")
    var data: List<GiphySearchDatum>,
    @Expose
    @SerializedName("pagination")
    var pageInformation: PageInformation

) {
    data class PageInformation(
        @Expose
        @SerializedName("total_count")
        var totalCount : Int = 0,
        @Expose
        @SerializedName("count")
        var count: Int = 0,
        @Expose
        @SerializedName("offset")
        var offset: Int = 0
    )

    data class GiphySearchDatum(
        @Expose
        @SerializedName("type")
        var type: String = "",
        @Expose
        @SerializedName("id")
        var id: String = "",
        @Expose
        @SerializedName("url")
        var url: String = "",
        @Expose
        @SerializedName("title")
        var title: String = "",
        @Expose
        @SerializedName("images")
        var images: Images = Images()
    ) {
        data class Images(
            @Expose
            @SerializedName("original")
            var original: Urls = Urls(),
            @Expose
            @SerializedName("fixed_width")
            var fixedWidth: Urls = Urls(),
            @Expose
            @SerializedName("fixed_width_downsampled")
            var downSampledFixedWidth: Urls = Urls(),
            @Expose
            @SerializedName("downsized")
            var downsized: Urls = Urls()
        )

        data class Urls(
            @Expose
            @SerializedName("width")
            var width: String = "",
            @Expose
            @SerializedName("height")
            var height: String = "",
            @Expose
            @SerializedName("url")
            var url: String = "",
            @Expose
            @SerializedName("size")
            var size: String = "",
            @Expose
            @SerializedName("mp4")
            var mp4Url: String = "",
            @Expose
            @SerializedName("mp4_size")
            var mp4Size: String = "",
            @Expose
            @SerializedName("webp")
            var webpUrl: String = "",
            @Expose
            @SerializedName("webp_size")
            var webpSize: String = ""
        )

        data class Url(@Expose @SerializedName("url") var Url: String = "")

    }
}