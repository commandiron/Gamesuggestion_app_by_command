package com.demirli.a42gamesuggestionapp.ui

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demirli.a42gamesuggestionapp.data.local.PollDao
import com.demirli.a42gamesuggestionapp.data.local.PollDatabase
import com.demirli.a42gamesuggestionapp.data.remote.ApiClient
import com.demirli.a42gamesuggestionapp.data.remote.ApiService
import com.demirli.a42gamesuggestionapp.model.Poll
import com.demirli.a42gamesuggestionapp.model.SearchResults
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository(context: Context) {

    private val apiService: ApiService by lazy { ApiClient.getApiService() }

    private val db: PollDatabase by lazy { PollDatabase.getInstance(context) }
    private val dao: PollDao by lazy { db.pollDao() }

    fun getGames(searchInfo: RequestBody): LiveData<List<SearchResults>>? {
        var gameLiveData: MutableLiveData<List<SearchResults>> = MutableLiveData()

        apiService.getGames(searchInfo).enqueue(object: Callback<List<SearchResults>>{
            override fun onFailure(call: Call<List<SearchResults>>, t: Throwable) {
                Log.e("getGames", t.message)
            }
            override fun onResponse(
                call: Call<List<SearchResults>>,
                response: Response<List<SearchResults>>
            ) {
                gameLiveData.value =response.body()
            }
        })
        return gameLiveData
    }

    fun insertPoll(poll: Poll) = RepoAsyncTask(dao).execute(poll)
    fun getAllPolls() = dao.getAllPolls()
    fun getSinglePoll(pollId: Int) = dao.getSinglePoll(pollId)

    private class RepoAsyncTask(val dao: PollDao): AsyncTask<Poll, Void, Void>(){
        override fun doInBackground(vararg params: Poll?): Void? {
            dao.insertPoll(params[0])
            return null
        }
    }


}