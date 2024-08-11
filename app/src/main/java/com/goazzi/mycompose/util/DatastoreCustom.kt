package com.goazzi.mycompose.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create a DataStore instance
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

// Define keys for preferences
object PreferencesKeys {
    val SHOW_LOC_PERM_REQ = booleanPreferencesKey("show_loc_perm_req")
    val DARK_MODE = booleanPreferencesKey("dark_mode")
    val USERNAME = stringPreferencesKey("username")
}

//SHOW_LOC_PERM_REQ
fun Context.getShowLockPermReq(): Flow<Boolean> {
    return dataStore.data.map { preferences ->
        preferences[PreferencesKeys.SHOW_LOC_PERM_REQ] ?: false
    }
}

suspend fun Context.setShowLockPermReq(isEnabled: Boolean) {
    dataStore.edit { preferences ->
        preferences[PreferencesKeys.SHOW_LOC_PERM_REQ] = isEnabled
    }
}

//sample
fun Context.getDarkMode(): Flow<Boolean> {
    return dataStore.data.map { preferences ->
        preferences[PreferencesKeys.DARK_MODE] ?: false
    }
}

fun Context.getUsername(): Flow<String> {
    return dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USERNAME] ?: "Guest"
    }
}

suspend fun Context.setDarkMode(isEnabled: Boolean) {
    dataStore.edit { preferences ->
        preferences[PreferencesKeys.DARK_MODE] = isEnabled
    }
}

suspend fun Context.setUsername(username: String) {
    dataStore.edit { preferences ->
        preferences[PreferencesKeys.USERNAME] = username
    }
}