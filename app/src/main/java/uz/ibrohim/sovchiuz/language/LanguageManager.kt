package uz.ibrohim.sovchiuz.language

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class LanguageManager(private val ct: Context) {
    private val sharedPreferences: SharedPreferences = ct.getSharedPreferences("LANG", Context.MODE_PRIVATE)
    fun updateResource(code: String) {
        val locale = Locale(code)
        Locale.setDefault(locale)
        val resources = ct.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        lang = code
    }

    var lang: String?
        get() = sharedPreferences.getString("lang", "eng")
        set(code) {
            val editor = sharedPreferences.edit()
            editor.putString("lang", code)
            editor.apply()
        }

}