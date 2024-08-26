package br.dev.saed.voleibr.model.repositories.db.winner

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WinnerDao {
    @Query("SELECT * FROM winner")
    fun getAll(): Flow<List<WinnerEntity>>

    @Query("SELECT id, name, COUNT(*) as wins " +
            "FROM winner " +
            "GROUP BY name " +
            "ORDER BY wins DESC")
    fun getWinners(): Flow<List<WinnerSearch>>

    @Query("DELETE FROM winner WHERE name = :team")
    suspend fun deleteTeam(team: String)

    @Insert
    suspend fun insert(winner: WinnerEntity)
}