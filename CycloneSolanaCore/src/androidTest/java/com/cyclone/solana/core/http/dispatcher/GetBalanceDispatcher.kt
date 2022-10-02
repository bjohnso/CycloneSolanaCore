package com.cyclone.solana.core.http.dispatcher

import com.cyclone.solana.core.constants.RPC
import com.cyclone.solana.core.file.FileReader
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.json.JSONObject

object GetBalanceDispatcher: MockDispatcher {
    override fun getSuccessResponse(): Dispatcher {
        return object: Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val body = JSONObject(
                    request.body.readUtf8()
                )

                return when (body.getString("method")) {
                    RPC.RPCMethods.GET_BALANCE -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(
                                FileReader.instance.readJsonFile(
                                    FileReader.FileResource.getBalance
                                )
                            )
                    }
                    else -> MockResponse().setResponseCode(200).setBody("{}")
                }
            }
        }
    }

    override fun getErrorResponse(): Dispatcher {
        return object: Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val body = JSONObject(
                    request.body.readUtf8()
                )

                return when (body.getString("method")) {
                    RPC.RPCMethods.GET_BALANCE -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(
                                FileReader.instance.readJsonFile(
                                    FileReader.FileResource.getBalanceError
                                )
                            )
                    }
                    else -> MockResponse().setResponseCode(200).setBody("{}")
                }
            }
        }
    }

    override fun getMalformedResponse(): Dispatcher {
        return object: Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val body = JSONObject(
                    request.body.readUtf8()
                )

                return when (body.getString("method")) {
                    RPC.RPCMethods.GET_BALANCE -> {
                        MockResponse()
                            .setResponseCode(200)
                            .setBody(
                                FileReader.instance.readJsonFile(
                                    FileReader.FileResource.getLatestBlockHashError
                                )
                            )
                    }
                    else -> MockResponse().setResponseCode(200).setBody("{}")
                }
            }
        }
    }
}