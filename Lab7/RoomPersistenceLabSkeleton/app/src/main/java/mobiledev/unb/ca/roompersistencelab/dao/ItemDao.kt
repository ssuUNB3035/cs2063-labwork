package mobiledev.unb.ca.roompersistencelab.dao

import androidx.core.view.WindowInsetsCompat
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import mobiledev.unb.ca.roompersistencelab.entity.Item
import java.util.concurrent.Callable

/**
 * This DAO object validates the SQL at compile-time and associates it with a method
 */
@Dao
interface ItemDao {
    // TODO
    //  Add app specific queries in here
    //  Additional details can be found at https://developer.android.com/reference/android/arch/persistence/room/Dao

    //get name (get by primaryKey)
    @Query("SELECT * FROM item_table WHERE name = :nameIn")
    fun getItemsByName(nameIn:String):Array<Item>

    @Insert
    fun insertNewItem(item: Item)

    //abstract fun call(function: () -> Unit): Array<Item>?


}