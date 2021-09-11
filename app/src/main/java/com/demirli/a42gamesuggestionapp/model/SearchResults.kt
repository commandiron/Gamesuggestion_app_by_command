package com.demirli.a42gamesuggestionapp.model

import com.google.gson.annotations.SerializedName

class SearchResults(

    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String
) {
}