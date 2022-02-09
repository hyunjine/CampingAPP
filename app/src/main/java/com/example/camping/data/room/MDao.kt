package com.example.camping.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.camping.data.vo.Item
import io.reactivex.Single

@Dao
interface MDao {
    @Insert
    fun insert(data: Item)

    @Query("SELECT * from FavoriteItem")
    fun getAll(): Single<List<Item>>

    @Query("SELECT contentId from FavoriteItem WHERE contentId = :contentId")
    fun isExist(contentId: Int): Int?

    @Query("DELETE FROM FavoriteItem WHERE contentId = :contentId")
    fun delete(contentId: Int)

    @Query("SELECT COUNT(contentId) FROM FavoriteItem")
    fun getSize(): Int?
}