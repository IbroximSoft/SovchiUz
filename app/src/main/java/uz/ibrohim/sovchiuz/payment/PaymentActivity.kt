package uz.ibrohim.sovchiuz.payment

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.databinding.ActivityPaymentBinding
import uz.ibrohim.sovchiuz.payment.pay_category.HumoFragment
import uz.ibrohim.sovchiuz.payment.pay_category.UzcardFragment
import uz.ibrohim.sovchiuz.payment.pay_category.VisaFragment

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences: SharedPreferences =
            getSharedPreferences("user_card", Context.MODE_PRIVATE)
        if (sharedPreferences.getString("number", null) != null) {
            when (sharedPreferences.getString("choose", null)) {
                "8600" -> {
                    replaceFragment(UzcardFragment())
                }
                "9860" -> {
                    replaceFragment(HumoFragment())
                }
                "4231" -> {
                    replaceFragment(VisaFragment())
                }
            }
        }else{
            replaceFragment(UzcardFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.pay_fragment, fragment)
        fragmentTransaction.commit()
    }
}