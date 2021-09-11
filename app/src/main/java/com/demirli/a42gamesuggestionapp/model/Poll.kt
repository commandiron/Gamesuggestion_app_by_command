package com.demirli.a42gamesuggestionapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "polls")
data class Poll(

    @PrimaryKey(autoGenerate = true)
    var pollId: Int = 0,
    var question: String,
    val gameListString: String

) {
}