package com.example.scribesoul

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

interface AppContainer {

}

class DefaultAppContainer(
    private val userDataStore: DataStore<Preferences>
): AppContainer {


}