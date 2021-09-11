package com.demirli.a42gamesuggestionapp.data.remote

import com.demirli.a42gamesuggestionapp.util.Constants
import okhttp3.*
import org.json.JSONObject

class RequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url()

        val url = originalHttpUrl.newBuilder().build()

        val request = originalRequest.newBuilder().url(url)
            .addHeader("user-key", Constants.API_KEY)
            .build()

        return chain.proceed(request)
    }
}
