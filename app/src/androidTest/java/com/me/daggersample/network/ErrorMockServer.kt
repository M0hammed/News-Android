package com.me.daggersample.network

import com.me.daggersample.utils.getStringJson
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

object ErrorMockServer {

    fun getUnauthorizedError(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(401)
                    .setBody(getStringJson("UnauthorizedError.json"))
            }
        }
    }
}
