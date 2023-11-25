package hu.bme.aut.android.shortcutstorage.model.work

import androidx.lifecycle.LiveData

class WorkRepository(private val workDao : WorkDao) {
    val works : LiveData<List<Work>> = workDao.getAll()
    val worksByName : LiveData<List<Work>> = workDao.getAllByName()
    val worksByAddress : LiveData<List<Work>> = workDao.getAllByAddress()
    val worksByDate : LiveData<List<Work>> = workDao.getAllByDate()
    val activeWorks : LiveData<List<Work>> = workDao.getAllActive()

    suspend fun insert(work : Work) {
        workDao.insert(work)
    }

    suspend fun update(work : Work) {
        workDao.update(work)
    }

    suspend fun delete(work: Work) {
        workDao.delete(work)
    }

    suspend fun deleteAll() {
        workDao.deleteAll()
    }
}