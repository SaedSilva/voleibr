package br.dev.saed.voleibr.model.repositories.db.team

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Dao
interface TeamDao {
    @Query("SELECT * FROM team")
    fun getAll(): Flow<List<TeamEntity>>

    @Insert
    suspend fun insert(team: TeamEntity)

    @Delete
    suspend fun delete(team: TeamEntity)

    @Delete
    suspend fun deleteFirstTeam() {
        val firstTeam = getAll().first()
        if(firstTeam.isNotEmpty()){
            delete(firstTeam.first())
        }
    }

    @Query("DELETE FROM team")
    suspend fun deleteAll()
}