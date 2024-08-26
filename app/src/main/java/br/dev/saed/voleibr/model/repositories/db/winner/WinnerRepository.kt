package br.dev.saed.voleibr.model.repositories.db.winner

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class WinnerRepository(
    private val dao: WinnerDao
) {
    val winners get() = dao.getWinners()

    suspend fun addWinner(winnerEntity: WinnerEntity) = withContext(IO) {
        dao.insert(winnerEntity)
    }
}