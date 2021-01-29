package com.me.daggersample.ui.SourcesListing

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

object SourcesMockServer {

    fun getSuccessSourcesList(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200).setBody(
                    "{\n" +
                            "   \"status\":\"ok\",\n" +
                            "   \"sources\":[\n" +
                            "      {\n" +
                            "         \"id\":\"abc-news\",\n" +
                            "         \"name\":\"ABC News\",\n" +
                            "         \"description\":\"Your trusted source for breaking news, analysis, exclusive interviews, headlines, and videos at ABCNews.com.\",\n" +
                            "         \"url\":\"https://abcnews.go.com\",\n" +
                            "         \"category\":\"general\",\n" +
                            "         \"language\":\"en\",\n" +
                            "         \"country\":\"us\"\n" +
                            "      }," +
                            "       {\n" +
                            "         \"id\":\"abc-news-au\",\n" +
                            "         \"name\":\"ABC News (AU)\",\n" +
                            "         \"description\":\"Australia's most trusted source of local, national and world news. Comprehensive, independent, in-depth analysis, the latest business, sport, weather and more.\",\n" +
                            "         \"url\":\"http://www.abc.net.au/news\",\n" +
                            "         \"category\":\"general\",\n" +
                            "         \"language\":\"en\",\n" +
                            "         \"country\":\"au\"\n" +
                            "      }" +
                            "]" +
                            "}"
                )
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

    fun getUnauthorizedError(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(401).setBody(
                    "{\n" +
                            "\"status\": \"error\",\n" +
                            "\"code\": \"apiKeyMissing\",\n" +
                            "\"message\": \"Your API key is missing. Append this to the URL with the apiKey param, or use the x-api-key HTTP header.\"\n" +
                            "}"
                )
            }
        }
    }
}