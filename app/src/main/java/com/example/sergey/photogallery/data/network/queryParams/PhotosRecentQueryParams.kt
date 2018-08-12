package com.example.sergey.photogallery.data.network.queryParams

object PhotosRecentQueryParams : QueryParams() {
    fun create(): Map<String, String> {
        addParam("method", "flickr.photos.getRecent")
        addParam("&api_key", "b1ea2013ed9473b68a272ad45cd32fd8")
        addParam("format", "json")
        addParam("nojsoncallback", "1")
        addParam("extras", "url_s")
        return params
    }
}