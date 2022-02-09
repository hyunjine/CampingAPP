package com.example.camping.data.retrofit

import android.util.Log
import com.example.camping.util.data.LOG.TAG
import com.example.camping.util.data.ParameterType
import com.example.camping.util.data.SERVICE.BASE_REQUEST_SIZE
import com.example.camping.util.data.SERVICE.BODY
import com.example.camping.util.data.SERVICE.CODE
import com.example.camping.util.data.SERVICE.EMPTY_JSON_ARRAY
import com.example.camping.util.data.SERVICE.HEADER
import com.example.camping.util.data.SERVICE.ITEM
import com.example.camping.util.data.SERVICE.ITEMS
import com.example.camping.util.data.SERVICE.LONGER_REQUEST_SIZE
import com.example.camping.util.data.SERVICE.MESSAGE
import com.example.camping.util.data.SERVICE.OK_CODE
import com.example.camping.util.data.SERVICE.RECYCLER_VIEW_SIZE
import com.example.camping.util.data.SERVICE.RESPONSE
import com.example.camping.util.data.SERVICE.TOTAL_COUNT
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CustomInterceptor(
    private val param: ParameterType,
    private var pageNo: Int?,
    private val area: String?,
    private val falName: String?,
    private val type: String?
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val result = JSONObject()
            val items = JSONArray()

            var totalSize: Int = 0
            var currentSize: Int = 0
            while (result.length() < RECYCLER_VIEW_SIZE) {
                if (pageNo != null)
                    pageNo =  pageNo!! + 1

                val response = if (param == ParameterType.FAL_NAME) chain.proceed(getNewRequest(chain, true)) else chain.proceed(getNewRequest(chain, false))
                val responseJson = response.extractResponseJson()

                val responseCode = responseJson.getJSONObject(RESPONSE).getJSONObject(HEADER).get(CODE).toString()
                val responseMessage = responseJson.getJSONObject(RESPONSE).getJSONObject(HEADER).get(MESSAGE).toString()
                // 서버 응답 에러코드
                if (responseCode != OK_CODE)
                    throw IllegalArgumentException("Network Error Code: $responseCode - $responseMessage")

                val customResponse = responseJson.getJSONObject(RESPONSE).getJSONObject(BODY).getJSONObject(ITEMS).getJSONArray(ITEM)
                // 서버 응답 결과 없음
                if (customResponse.toString().isEmpty()) {
                    return response.newBuilder()
                        .body(EMPTY_JSON_ARRAY.toResponseBody())
                        .build()
                }
                if (pageNo != null) {
                    totalSize = responseJson.getJSONObject(RESPONSE).getJSONObject(BODY).get(TOTAL_COUNT).toString().toInt()
                    currentSize = if (param == ParameterType.FAL_NAME) pageNo!! * LONGER_REQUEST_SIZE.toInt() else pageNo!! * BASE_REQUEST_SIZE.toInt()
                }

                when (param) {
                    ParameterType.KEY_WORD, ParameterType.AROUND, ParameterType.RANDOM_LIST -> setResultArray(customResponse, items)
                    ParameterType.IMAGE_LIST -> setImageArray(customResponse, items)
                    ParameterType.AREA -> setResultArray(customResponse, items, "addr1", area!!)
                    ParameterType.FAL_NAME -> setResultArray(customResponse, items, "facltNm", falName!!)
                    ParameterType.TYPE -> setResultArray(customResponse, items, "addr1", area!!, "induty", type!!)
                    ParameterType.PET, ParameterType.INIT_PET,-> setResultPetArray(customResponse, items, "addr1", area!!,"animalCmgCl", "불가능")
                }

                if (param == ParameterType.IMAGE_LIST || param == ParameterType.RANDOM_LIST || param == ParameterType.INIT_PET) {
                    return response.newBuilder()
                        .body(items.toString().toResponseBody())
                        .build()

                }
                result.put("pageNo", pageNo)
                result.put("items", items)
                // 필요한 데이터 개수 도달
                if (result.length() >= RECYCLER_VIEW_SIZE) {
                    return response.newBuilder()
                        .body(result.toString().toResponseBody())
                        .build()
                }
                // 서버 모든 응답 처리 도달
                if (currentSize > totalSize) {
                    return response.newBuilder()
                    .body(result.toString().toResponseBody())
                    .build()
                }
            }

            throw IllegalArgumentException("Unknown Error")

        } catch (e: Exception) {
            throw IllegalArgumentException(e.message)
        }
    }

    private fun setResultArray(customResponse: JSONArray, items: JSONArray) {
        for (i in 0 until customResponse.length()) {
            try {
                val item = customResponse.getJSONObject(i)
                if (item.get("firstImageUrl") != null) {
                    Log.d(TAG, "setResultArray: $item")
                    items.put(item)
                }

            } catch (e :JSONException) {}
        }
    }

    private fun setImageArray(customResponse: JSONArray, items: JSONArray) {
        for (i in 0 until customResponse.length()) {
            try {
                val item = customResponse.getJSONObject(i)
                items.put(item.get("imageUrl"))

            } catch (e :JSONException) {}
        }
    }

    private fun setResultArray(customResponse: JSONArray, items: JSONArray, paramName: String, paramValue: String) {
        for (i in 0 until customResponse.length()) {
            try {
                val item = customResponse.getJSONObject(i)
                if (item.get(paramName).toString().contains(paramValue) && item.get("firstImageUrl") != null)
                    items.put(item)
            } catch (e :JSONException) {}
        }
    }

    private fun setResultArray(customResponse: JSONArray, items : JSONArray, paramName_1 : String, paramValue_1 : String, paramName_2 : String, paramValue_2 : String) {
        for (i in 0 until customResponse.length()) {
            try {
                val item = customResponse.getJSONObject(i)
                if ((item.get(paramName_1).toString().contains(paramValue_1) && item.get("firstImageUrl") != null) && item.get(paramName_2).toString().contains(paramValue_2))
                    items.put(item)

            } catch (e :JSONException) {}
        }
    }

    private fun setResultPetArray(customResponse: JSONArray, items : JSONArray, paramName_1 : String, paramValue_1 : String, paramName_2 : String, paramValue_2 : String) {
        for (i in 0 until customResponse.length()) {            
            try {
                val item = customResponse.getJSONObject(i)
                if ((item.get(paramName_1).toString().contains(paramValue_1) && item.get("firstImageUrl") != null) && !item.get(paramName_2).toString().contains(paramValue_2))
                    items.put(item)

            } catch (e :JSONException) {}
        }
    }

    private fun getNewRequest(chain: Interceptor.Chain, isLongRequest: Boolean): Request {
        val url = if (isLongRequest) {
            chain.request()
                .url
                .newBuilder()
                .addQueryParameter("pageNo", "$pageNo")
                .addQueryParameter("numOfRows", LONGER_REQUEST_SIZE)
                .build()
        } else {
            chain.request()
                .url
                .newBuilder()
                .addQueryParameter("pageNo", "$pageNo")
                .addQueryParameter("numOfRows", BASE_REQUEST_SIZE)
                .build()
        }

        return chain.request()
            .newBuilder()
            .url(url)
            .build()
    }

    private fun Response.extractResponseJson(): JSONObject = JSONObject(this.body?.string() ?: EMPTY_JSON_ARRAY)

    class Builder(private val param: ParameterType) {
        private var pageNo: Int? = null
        private var area: String? = null
        private var falName: String? = null
        private var type: String? = null

        fun pageNo(pageNo: Int?): Builder {
            this.pageNo = pageNo
            return this
        }

        fun area(area: String?): Builder {
            this.area = area
            return this
        }

        fun falName(falName: String?): Builder {
            this.falName = falName
            return this
        }

        fun type(type: String?): Builder {
            this.type = type
            return this
        }

        fun build() = CustomInterceptor(param, pageNo, area, falName, type)
    }
}