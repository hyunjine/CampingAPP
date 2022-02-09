package com.example.camping.util.data

object SERVICE {
    const val BASE_URL = "http://api.visitkorea.or.kr/openapi/service/rest/GoCamping/"
    const val API_KEY = "API KEY"
    const val APP_OS = "AND"
    const val APP_NAME = "Camping"
    const val TYPE = "json"
    const val EMPTY_JSON_ARRAY = "[]"

    // response
    const val RESPONSE = "response"
    // header
    const val HEADER = "header"
    const val CODE = "resultCode"
    const val MESSAGE = "resultMsg"
    const val OK_CODE = "0000"
    const val TOTAL_COUNT = "totalCount"

    // body
    const val BODY = "body"
    const val ITEMS = "items"
    const val ITEM = "item"

    // around
    const val RADIUS = "20000"

    // 리사이클러뷰 1회 요청 최대 크기
    const val RECYCLER_VIEW_SIZE = 10
    // 요청 데이터 크기 제한
    const val BASE_REQUEST_SIZE = "200"
    const val LONGER_REQUEST_SIZE = "1000"

    const val MIN_DISTANCE_CHANGE_FOR_UPDATES = 10f
    const val MIN_TIME_BW_UPDATES = 1000 * 60 * 1L
}

object LOG {
    const val TAG = "로그내용"
    const val ERROR = "error"
}

object RECOMMAND {
    val KEY_WORDS = arrayOf("바다", "계곡", "산")
}

object PREPERENCE {
    val P_KEY = "RECENT"
}
