package hu.bme.aut.android.shortcutstorage.model.work

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.bme.aut.android.shortcutstorage.model.storage.StorageItem

@Entity(tableName = "works_table")
data class Work(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "materials") val materials: List<StorageItem>,
    @ColumnInfo(name = "done") var done : Boolean = false
)