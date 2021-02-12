package com.me.daggersample.network

import com.me.daggersample.utils.getStringJson
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

object SourcesMockServer {

    fun getSuccessSourcesList(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200)
                    .setBody(getStringJson("SourcesList.json"))
            }
        }
    }

    fun getEmptySourceList(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200).setBody(
                    "{\n" +
                        "   \"status\":\"ok\",\n" +
                        "   \"sources\":[\n" +
                        "]" +
                        "}"
                )
            }
        }
    }
}
