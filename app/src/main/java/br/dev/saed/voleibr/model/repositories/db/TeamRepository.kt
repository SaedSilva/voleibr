package br.dev.saed.voleibr.model.repositories.db

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

public class TeamRepository(
    private val dao: TeamDAO
) {
    val queue get() = dao.getAll()

    suspend fun addTeam(team: TeamEntity) = withContext(IO) {
        dao.insert(team)
    }

    suspend fun removeTeam(team: TeamEntity) = withContext(IO) {
        dao.delete(team)
    }
}