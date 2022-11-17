package mobiledev.unb.ca.roompersistencelab.repository

import mobiledev.unb.ca.roompersistencelab.db.AppDatabase.Companion.getDatabase
import android.app.Application
import android.util.Log
import mobiledev.unb.ca.roompersistencelab.dao.ItemDao
import mobiledev.unb.ca.roompersistencelab.db.AppDatabase.Companion.databaseWriterExecutor
import mobiledev.unb.ca.roompersistencelab.entity.Item
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class ItemRepository(application: Application) {
    private val itemDao: ItemDao? = getDatabase(application).itemDao()

    // TODO Add query specific methods
    //  HINT 1:
    //   The insert operation will use the Runnable interface as there are no return values
    //  HINT 2:
    //   The search operation will use the Callable interface with Future as there are return values
    //  HINT 3:
    //    LiveData does not work as well for this; consider using an object list to return the search results
    //  See the example project file at
    //  https://github.com/hpowell20/cs2063-fall-2022-examples/blob/master/Lecture7/RoomPersistenceLibraryDemo/app/src/main/java/mobiledev/unb/ca/roompersistencetest/repository/ItemRepository.kt
    //  to see examples of how to work with the Executor Service along with Runnables and Callables

    fun insert(item: Item){
        databaseWriterExecutor.execute{
            itemDao?.insertNewItem(item)
        }
    }

    fun search(searchTerm:String): Array<Item>? {
        //Future ~= js promise
        val itemFuture: Future<Array<Item>> = databaseWriterExecutor.submit ( //submit returns
            Callable {
                itemDao?.getItemsByName(searchTerm)
            }
        )
        return try{
            while(!itemFuture.isDone){
                TimeUnit.SECONDS.sleep(1)
            }
            itemFuture.get()
        }
        catch(e: ExecutionException){
            e.printStackTrace()
            null
        }
        catch(e: InterruptedException){
            e.printStackTrace()
            null
        }

        //Log.i("ITEM REPO",items[0].itemName)
    }

}