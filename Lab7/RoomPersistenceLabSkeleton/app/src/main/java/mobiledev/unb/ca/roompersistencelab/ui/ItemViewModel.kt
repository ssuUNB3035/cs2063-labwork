package mobiledev.unb.ca.roompersistencelab.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import mobiledev.unb.ca.roompersistencelab.entity.Item
import mobiledev.unb.ca.roompersistencelab.repository.ItemRepository

class ItemViewModel(application: Application) : AndroidViewModel(application) {
    private val itemRepository: ItemRepository = ItemRepository(application)
    var id = 0
    // TODO
    //  Add mapping calls between the UI and Database
    fun addItem(name:String,count:Int){ //insert
        val item = Item()
        item.itemName = name
        item.itemNumber = count
        itemRepository.insert(item)
    }
    fun getItems(search:String):Array<Item>?{
        return itemRepository.search(search)
    }

}