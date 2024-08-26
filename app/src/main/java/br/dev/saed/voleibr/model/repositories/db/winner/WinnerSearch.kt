package br.dev.saed.voleibr.model.repositories.db.winner

import androidx.room.ColumnInfo

data class WinnerSearch(
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "wins") val wins: Int?
)
