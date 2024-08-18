package br.dev.saed.voleibr.model.repositories.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDAO {
    @Query("SELECT * FROM team")
    fun getAll(): Flow<List<TeamEntity>>

    @Insert
    suspend fun insert(team: TeamEntity)

    @Delete
    suspend fun delete(team: TeamEntity)
}