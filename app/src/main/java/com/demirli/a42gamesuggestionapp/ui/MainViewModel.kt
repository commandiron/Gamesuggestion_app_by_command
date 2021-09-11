package com.demirli.a42gamesuggestionapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.demirli.a42gamesuggestionapp.model.Poll
import com.demirli.a42gamesuggestionapp.model.SearchResults
import okhttp3.RequestBody

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository: MainRepository by lazy { MainRepository(application.applicationContext) }

    fun getGames(searchInfo: RequestBody): LiveData<List<SearchResults>>? = repository.getGames(searchInfo)

    fun insertPoll(poll: Poll) = repository.insertPoll(poll)

    fun getAllPolls() = repository.getAllPolls()

    fun getSinglePoll(pollId: Int) = repository.getSinglePoll(pollId)

}