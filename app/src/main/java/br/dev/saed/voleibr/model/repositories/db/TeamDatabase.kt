package br.dev.saed.voleibr.model.repositories.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TeamEntity::class], version = 1)
abstract class TeamDatabase: RoomDatabase() {
    abstract fun teamDAO(): TeamDao
}