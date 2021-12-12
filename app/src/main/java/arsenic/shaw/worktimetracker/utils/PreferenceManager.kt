package arsenic.shaw.worktimetracker.utils

import android.content.Context
import android.content.SharedPreferences
import android.provider.SyncStateContract
import java.util.*

class PreferenceManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(Constant.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE)


    fun putBoolean(key: String, value: Boolean) = sharedPreferences.edit().putBoolean(key, value).apply()
    fun getBoolean(key: String): Boolean = sharedPreferences.getBoolean(key, false)

    fun putString(key: String, value: String) = sharedPreferences.edit().putString(key, value).apply()
    fun getString(key: String): String? = sharedPreferences.getString(key, "")

    fun putLong(key: String, value: Long) = sharedPreferences.edit().putLong(key, value).apply()
    fun getLong(key: String): Long = sharedPreferences.getLong(key, Calendar.getInstance().timeInMillis)

    fun clear() = sharedPreferences.edit().clear().apply()
}