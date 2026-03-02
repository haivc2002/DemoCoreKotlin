package com.example.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ## Singleton implementation of [DataStorageInterface] using DataStore Preferences.
 *
 * Data is stored in file: `base_storage.preferences_pb`
 *
 * Supported types: [String], [Int], [Long], [Boolean], [Float].
 *
 * > **Note:** This class must remain `@Singleton` to avoid
 * > "multiple DataStores active for the same file" error.
 *
 * @see DataStorageInterface for usage examples.
 */
@Singleton
class DataStorage @Inject constructor(
    @ApplicationContext val context: Context
) : DataStorageInterface {

    val Context.dataStore by preferencesDataStore("base_storage")

    override suspend fun <T : Any> write(key: String, value: T) {
        context.dataStore.edit { prefs ->
            when (value) {
                is Int -> prefs[intPreferencesKey(key)] = value
                is Long -> prefs[longPreferencesKey(key)] = value
                is String -> prefs[stringPreferencesKey(key)] = value
                is Boolean -> prefs[booleanPreferencesKey(key)] = value
                is Float -> prefs[floatPreferencesKey(key)] = value
                else -> throw IllegalArgumentException("Unsupported type: ${value::class}")
            }
        }
    }

    override suspend fun read(key: String): Any? {
        val prefs = context.dataStore.data.first()

        return prefs[stringPreferencesKey(key)]
            ?: prefs[intPreferencesKey(key)]
            ?: prefs[longPreferencesKey(key)]
            ?: prefs[booleanPreferencesKey(key)]
            ?: prefs[floatPreferencesKey(key)]
    }

    override suspend fun remove(key: String) {
        context.dataStore.edit { prefs ->
            prefs.remove(stringPreferencesKey(key))
            prefs.remove(intPreferencesKey(key))
            prefs.remove(longPreferencesKey(key))
            prefs.remove(booleanPreferencesKey(key))
            prefs.remove(floatPreferencesKey(key))
        }
    }

    override suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}

