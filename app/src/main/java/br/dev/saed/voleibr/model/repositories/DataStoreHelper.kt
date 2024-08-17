package br.dev.saed.voleibr.model.repositories

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
        val TEAM_2 = stringPreferencesKey("team_2")
        val VAI_A_2 = booleanPreferencesKey("vai_a_2")
    }

    val maxPointsFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[MAX_POINTS] ?: 12
    }

    val team1Flow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[TEAM_1] ?: "Time 1"
    }

    val team2Flow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[TEAM_2] ?: "Time 2"
    }

    val vaiA2Flow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[VAI_A_2] ?: true
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

    suspend fun saveTeam2(team2: String) {
        context.dataStore.edit { preferences ->
            preferences[TEAM_2] = team2
        }
    }

    suspend fun saveVaiA2(vaiA2: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[VAI_A_2] = vaiA2
        }
    }
}


