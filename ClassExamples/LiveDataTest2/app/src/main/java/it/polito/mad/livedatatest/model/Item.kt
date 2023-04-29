package it.polito.mad.livedatatest.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "items", indices = [Index("name")])
class Item {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var name: String = ""

    override fun toString(): String {
        return "{id : $id, name: \"$name\"}"
    }
}