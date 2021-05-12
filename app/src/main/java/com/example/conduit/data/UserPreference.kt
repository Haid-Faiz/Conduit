package com.example.conduit.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_prefs")

class UserPreference(context: Context) {

    private val _dataStore = context.dataStore

    val authToken: Flow<String?>
        get() = _dataStore.data.map {
            it[AUTH_TOKEN_KEY]   // it returns nullable thing
        }

    suspend fun saveAuthToken(token: String) {
        _dataStore.edit {
            it[AUTH_TOKEN_KEY] = token
        }
    }

    suspend fun clearAuthToken() {
        _dataStore.edit {
            it.clear()
        }
    }

    companion object {
        val AUTH_TOKEN_KEY: Preferences.Key<String> = stringPreferencesKey("auth_token")
    }
}
