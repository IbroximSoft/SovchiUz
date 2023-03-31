package uz.ibrohim.sovchiuz.read_data

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.navigation.findNavController
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.databinding.ActivityInfoAllBinding
import uz.ibrohim.sovchiuz.language.AppCompat

class InfoAllActivity : AppCompat() {

    private lateinit var binding: ActivityInfoAllBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoAllBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreference: SharedPreferences =
            this.getSharedPreferences("user_status", Context.MODE_PRIVATE)
        val balance = sharedPreference.getString("balance", null)
        binding.txtSumma.text = balance

        val uid = intent.getStringExtra("uid").toString()
        val gender = intent.getStringExtra("gender").toString()

        val bundle = Bundle()
        bundle.putString("uid", uid)
        if (gender == "male") {
            val fragment = MaleFragment()
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.info_frame, fragment).commit()
        } else if (gender == "woman") {
            val fragment = WomanFragment()
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.info_frame, fragment).commit()
        }
    }
}