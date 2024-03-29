import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreManager private constructor(private val dataStore: DataStore<Preferences>) {

    private val THEME_KEY = intPreferencesKey("theme_setting")

    fun getThemeSetting(): Flow<Int?> {
        return dataStore.data.map {
            it[THEME_KEY]
        }
    }

    suspend fun saveThemeSetting(mode: Int) {
        dataStore.edit {
            it[THEME_KEY] = mode
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DataStoreManager? = null

        fun getInstance(dataStore: DataStore<Preferences>): DataStoreManager {
            return INSTANCE ?: synchronized(this) {
                val instance = DataStoreManager(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
