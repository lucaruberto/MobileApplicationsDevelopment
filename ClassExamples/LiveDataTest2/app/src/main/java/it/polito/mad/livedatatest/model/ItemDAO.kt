package it.polito.mad.livedatatest.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemDAO {
    @Query("SELECT * FROM items")
    fun findAll(): LiveData<List<Item>>

    @Query("SELECT count() FROM items")
    fun count(): LiveData<Int>

    @Insert
    fun addItem(item: Item)

    @Query("DELETE FROM items WHERE name = :name")
    fun removeItemsWithName(name: String)

    @Query("DELETE FROM items")
    fun removeAll()

}