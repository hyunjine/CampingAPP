package com.example.camping.data.retrofit

import com.example.camping.data.vo.Item
import com.example.camping.data.vo.Response
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CampingService {
    // 1. 기본 정보 목록 조회
    // numOfRows / pageNo
    @GET("basedList")
    fun getBaseList(): Single<Response>

    // 2. 위치기반 정보 목록 조회
    // numOfRows / pageNo / mapX / mapY / radius(거리 반경)
    @GET("locationBasedList")
    fun getAroundList(
        @Query("mapX") mapX: String,
        @Query("mapY") mapY: String,
        @Query("radius") radius: String
    ): Single<Response>

    // 3. 이미지정보 목록 조회
    // contentId
    @GET("imageList")
    fun getImageList(@Query("contentId") contentId: String): Single<ArrayList<String>>

    // 4. 키워드 검색 목록 조회
    // numOfRows / pageNo / keyword
    @GET("searchList")
    fun getKeyWordList(@Query("keyword", encoded = true) keyWord: String): Single<Response>

    // 5. 랜덤 키워드 검색 목록 조회
    // numOfRows / pageNo / keyword
    @GET("searchList")
    fun getRandomKeyWordList(
        @Query("keyword", encoded = true) keyWord: String,
        @Query("numOfRows") numOfRows: String,
        @Query("pageNo") pageNo: String
    ): Single<ArrayList<Item>>

    // 6. 메인 페이지 반려동물 검색 목록 조회
    // numOfRows / pageNo / keyword
    @GET("basedList")
    fun getInitPetList(
        @Query("numOfRows") numOfRows: String,
        @Query("pageNo") pageNo: String
    ): Single<ArrayList<Item>>
}