package uz.ibrohim.sovchiuz

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import es.dmoral.toasty.Toasty
import uz.ibrohim.sovchiuz.databinding.ActivityTermsBinding
import uz.ibrohim.sovchiuz.language.AppCompat
import uz.ibrohim.sovchiuz.login.LoginActivity
import uz.ibrohim.sovchiuz.screens.ScreenActivity

class TermsActivity : AppCompat() {

    private lateinit var binding: ActivityTermsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.termsBack.setOnClickListener{
            val intent = Intent(this@TermsActivity, ScreenActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding.termsCheckBox.setOnCheckedChangeListener{ _: CompoundButton?, b: Boolean ->
            if (b) {
                binding.termsBtnOn.visibility = View.VISIBLE
                binding.termsBtnOff.visibility = View.GONE
            } else {
                binding.termsBtnOff.visibility = View.VISIBLE
                binding.termsBtnOn.visibility = View.GONE
            }
        }

        binding.termsBtnOff.setOnClickListener {
            Toasty.warning(this@TermsActivity, getString(R.string.condition),
                Toasty.LENGTH_SHORT, true).show()
        }

        binding.termsBtnOn.setOnClickListener{
            val intent = Intent(this@TermsActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}