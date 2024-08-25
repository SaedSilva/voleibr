package br.dev.saed.voleibr.model.repositories.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreHelper(private val context: Context) {
    companion object {
        val MAX_POINTS = intPreferencesKey("max_points")
        val TEAM_1 = stringPreferencesKey("team_1")
        val POINTS_TEAM_1 = intPreferencesKey("points_team_1")
        val TEAM_1_COLOR = intPreferencesKey("team_1_color")
        val TEAM_2 = stringPreferencesKey("team_2")
        val POINTS_TEAM_2 = intPreferencesKey("points_team_2")
        val TEAM_2_COLOR = intPreferencesKey("team_2_color")
        val VAI_A_2 = booleanPreferencesKey("vai_a_2")
        val VIBRAR = booleanPreferencesKey("vibrar")
    }

    val maxPointsFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[MAX_POINTS] ?: 12
    }

    val team1Flow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[TEAM_1] ?: "Time 1"
    }

    val team1PointsFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[POINTS_TEAM_1] ?: 0
    }

    val team1ColorFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[TEAM_1_COLOR] ?: 0xFFF8E287.toInt()
    }

    val team2Flow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[TEAM_2] ?: "Time 2"
    }

    val team2PointsFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[POINTS_TEAM_2] ?: 0
    }

    val team2ColorFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[TEAM_2_COLOR] ?: 0xFFC5ECCE.toInt()
    }

    val vaiA2Flow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[VAI_A_2] ?: true
    }

    val vibrarFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[VIBRAR] ?: true
    }

    suspend fun saveMaxPoints(maxPoints: Int) {
        context.dataStore.edit { preferences ->
            preferences[MAX_POINTS] = maxPoints
        }
    }

    suspend fun saveTeam1(team1: String) {
        context.dataStore.edit { preferences ->
            preferences[TEAM_1] = team1
        }
    }

    suspend fun savePointsTeam1(pointsTeam1: Int) {
        context.dataStore.edit { preferences ->
            preferences[POINTS_TEAM_1] = pointsTeam1
        }
    }

    suspend fun saveTeam1Color(team1Color: Int) {
        context.dataStore.edit { preferences ->
            preferences[TEAM_1_COLOR] = team1Color
        }
    }

    suspend fun saveTeam2(team2: String) {
        context.dataStore.edit { preferences ->
            preferences[TEAM_2] = team2
        }
    }

    suspend fun savePointsTeam2(pointsTeam2: Int) {
        context.dataStore.edit { preferences ->
            preferences[POINTS_TEAM_2] = pointsTeam2
        }
    }

    suspend fun saveTeam2Color(team2Color: Int) {
        context.dataStore.edit { preferences ->
            preferences[TEAM_2_COLOR] = team2Color
        }
    }

    suspend fun saveVaiA2(vaiA2: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[VAI_A_2] = vaiA2
        }
    }

    suspend fun saveVibrar(vibrar: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[VIBRAR] = vibrar
        }
    }
}


