package uz.ibrohim.sovchiuz.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import uz.ibrohim.sovchiuz.HomeActivity
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.TermsActivity
import uz.ibrohim.sovchiuz.databinding.ActivityScreenBinding
import uz.ibrohim.sovchiuz.language.AppCompat
import uz.ibrohim.sovchiuz.language.LanguageManager
import kotlin.system.exitProcess

class ScreenActivity : AppCompat() {

    private lateinit var binding: ActivityScreenBinding
    private var screenAdapter: ScreenAdapter? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreference: SharedPreferences =
            this.getSharedPreferences("user_status", Context.MODE_PRIVATE)
        val login: String? = sharedPreference.getString("status", null)
        if (login != null) {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            finish()
        }

        screenAdapter = ScreenAdapter(this)
        binding.screenPage.adapter = screenAdapter
        binding.screenPage.setPageTransformer(false, ScreenPageTransformer())

        when (binding.screenText.text.toString()) {
            "Next" -> {
                binding.screenLanguage.setText("Uzbekcha")
            }
            "Кейинги" -> {
                binding.screenLanguage.setText("Узбекча")
            }
            "Следующий" -> {
                binding.screenLanguage.setText("Русский")
            }
        }
        val lang = LanguageManager(this)
        binding.screenLanguage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when (binding.screenLanguage.text.toString()) {
                    "Uzbekcha" -> {
                        lang.updateResource("en")
                        intents()
                    }
                    "Узбекча" -> {
                        lang.updateResource("uz")
                        intents()
                    }
                    "Русский" -> {
                        lang.updateResource("ru")
                        intents()
                    }
                }
            }
        })
    }

    private fun intents() {
        val intent = Intent(this@ScreenActivity, ScreenActivity::class.java)
        overridePendingTransition(0, 0)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun nextPage(view: View) {
        if (view.id == R.id.screen_next) {
            if (binding.screenPage.currentItem < screenAdapter?.count!! - 1) {
                binding.screenPage.setCurrentItem(binding.screenPage.currentItem + 1, true)
            }
        }
    }

    fun nextPage4(view: View) {
        if (view.id == R.id.screen_finish) {
            val intent = Intent(applicationContext, TermsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val language = resources.getStringArray(R.array.language)
        val arrayAdapter = ArrayAdapter(this, R.layout.language_item, language)
        binding.screenLanguage.setAdapter(arrayAdapter)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        exitProcess(0)
    }
}