package com.example.camping.data

import com.example.camping.data.retrofit.CampingService
import com.example.camping.data.room.MDataBase
import com.example.camping.data.vo.Item
import com.example.camping.data.vo.Response
import com.example.camping.util.data.SERVICE.RADIUS
import io.reactivex.Single

class Repository(
    private val service: CampingService?,
    private val mDataBase: MDataBase?
    ) {

    // Retrofit
    fun getDetails(): Single<Response> = service!!.getBaseList()

    fun getKeyWordList(ketWord : String): Single<Response> = service!!.getKeyWordList(ketWord)

    fun getRandomKeyWordList(ketWord : String, numOfRows: String, pageNo: String): Single<ArrayList<Item>> = service!!.getRandomKeyWordList(ketWord, numOfRows, pageNo)

    fun getInitPetList(numOfRows: String, pageNo: String): Single<ArrayList<Item>> = service!!.getInitPetList(numOfRows, pageNo)

    fun getAroundList(mapX : String, mapY : String): Single<Response> = service!!.getAroundList(mapX, mapY, RADIUS)

    fun getImageList(contentId : String): Single<ArrayList<String>> = service!!.getImageList(contentId)

    // Room
    fun isExist(contentId: Int): Boolean {
        return mDataBase!!.favoriteDao().isExist(contentId) != null
    }

    fun getFavoriteSize() = mDataBase!!.favoriteDao().getSize()

    fun insertFavorite(favoriteItem: Item) = mDataBase!!.favoriteDao().insert(favoriteItem)

    fun deleteFavorite(contentId: Int) = mDataBase!!.favoriteDao().delete(contentId)

    fun getAllFavorite() = mDataBase!!.favoriteDao().getAll()
}