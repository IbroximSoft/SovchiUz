package uz.ibrohim.sovchiuz.language

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class AppCompat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val languageManager = LanguageManager(this)
        languageManager.lang?.let { languageManager.updateResource(it) }
    }
}