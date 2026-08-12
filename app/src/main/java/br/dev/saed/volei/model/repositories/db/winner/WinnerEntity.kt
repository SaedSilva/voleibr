package br.dev.saed.volei.model.repositories.db.winner

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.dev.saed.volei.model.entities.Team
import br.dev.saed.volei.model.repositories.db.team.TeamEntity

@Entity(tableName = "winner")
data class WinnerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
) {
    companion object {
        fun fromTeam(entity: Team): WinnerEntity {
            return WinnerEntity(
                0,
                entity.nome
            )
        }
    }
}
