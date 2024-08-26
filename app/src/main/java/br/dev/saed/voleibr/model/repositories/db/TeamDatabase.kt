package br.dev.saed.voleibr.model.repositories.db

import androidx.room.Database
import androidx.room.RoomDatabase
import br.dev.saed.voleibr.model.repositories.db.team.TeamDao
import br.dev.saed.voleibr.model.repositories.db.team.TeamEntity
import br.dev.saed.voleibr.model.repositories.db.winner.WinnerDao
import br.dev.saed.voleibr.model.repositories.db.winner.WinnerEntity

@Database(entities = [TeamEntity::class, WinnerEntity::class], version = 2)
abstract class TeamDatabase: RoomDatabase() {
    abstract fun teamDAO(): TeamDao
    abstract fun winnerDAO(): WinnerDao
}