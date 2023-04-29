package it.polito.mad.livedatatest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.mad.livedatatest.model.Item
import it.polito.mad.livedatatest.model.ItemRepository
import kotlin.concurrent.thread

class MainViewModel(application: Application): AndroidViewModel(application) {
    val repo = ItemRepository(application)
    val counter = repo.count()
    val items: LiveData<List<Item>> = repo.items()

    fun increment() {
        thread { repo.add("item${counter.value}") }
    }
    fun decrement() {
        thread { repo.sub("item${counter.value}") }
    }
    fun clear(){
        thread { repo.clear() }
    }
//    fun delay() {
//        val t = Thread(){
//            Thread.sleep(3000)
//            _counter.postValue( (_counter.value ?: 0) +1 )
//        }
//        t.start()
//    }
}