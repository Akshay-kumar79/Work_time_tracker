package arsenic.shaw.worktimetracker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {

    @Insert
    fun insert(work: Work): Long

    @Query("select * from work_table order by id desc")
    fun getAllWork(): LiveData<List<Work>>

    @Query("delete from work_table where id = :id")
    fun delete(id: Long)

    @Query("delete from work_table")
    fun deleteAll()

}