package it.polito.mad.livedatatest.model

import android.app.Application
import androidx.lifecycle.LiveData

class ItemRepository(application: Application) {
    private val itemDao = ItemDatabase.getDatabase(application).itemDao()

    //need to be protected either here or in the ViewModel
    fun add(name: String){
        val i = Item().also {it.name = name}
        itemDao.addItem(i)
    }

    fun sub(name: String){
        itemDao.removeItemsWithName(name)
    }

    fun clear(){
        itemDao.removeAll()
    }

    fun count(): LiveData<Int> = itemDao.count()

    fun items(): LiveData<List<Item>> = itemDao.findAll()
}