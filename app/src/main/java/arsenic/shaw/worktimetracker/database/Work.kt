package arsenic.shaw.worktimetracker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "work_table")
data class Work(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var startTime: Long,
    var endTime: Long,

)
