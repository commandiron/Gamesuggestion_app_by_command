package com.demirli.a42gamesuggestionapp.data.remote

import com.demirli.a42gamesuggestionapp.model.SearchResults
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("games/")
    fun getGames(@Body searchInfo: RequestBody): Call<List<SearchResults>>

}