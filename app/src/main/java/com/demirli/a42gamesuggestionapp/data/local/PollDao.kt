package com.demirli.a42gamesuggestionapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.demirli.a42gamesuggestionapp.model.Poll

@Dao
interface PollDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPoll(poll: Poll?)

    @Query("SELECT * FROM polls")
    fun getAllPolls(): LiveData<List<Poll>>

    @Query("SELECT * FROM polls WHERE pollId= :pollId")
    fun getSinglePoll(pollId: Int?): LiveData<Poll>

}
