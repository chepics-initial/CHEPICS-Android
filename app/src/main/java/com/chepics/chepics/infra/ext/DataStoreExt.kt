package com.chepics.chepics.infra.ext

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

fun <T> DataStore<Preferences>.getStream(
    key: Preferences.Key<T>,
    defaultValue: T,
    shouldDistinctUntilChanged: Boolean = true
): Flow<T> {
    return data.map { preferences: Preferences ->
        preferences[key] ?: defaultValue
    }.run { if (shouldDistinctUntilChanged) distinctUntilChanged() else this }
}

suspend fun <T> DataStore<Preferences>.save(key: Preferences.Key<T>, value: T) {
    edit { settings ->
        settings[key] = value
    }
}