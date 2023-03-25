package uz.ibrohim.sovchiuz.more_page.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.databinding.ActivityProfileBinding
import uz.ibrohim.sovchiuz.language.AppCompat

class ProfileActivity : AppCompat() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        reference = FirebaseDatabase.getInstance().reference

        val sharedPreference: SharedPreferences =
            this.getSharedPreferences("user_status", Context.MODE_PRIVATE)
        when (sharedPreference.getString("status", null)) {
            "no" -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.account_frame, InformationFragment()).commit()

            }
            "wait" -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.account_frame, WaitFragment()).commit()

            }
            "success" -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.account_frame, SuccessFragment()).commit()

            }
            "block" -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.account_frame, BlockFragment()).commit()

            }
            "yes" -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.account_frame, ProfileFragment()).commit()
            }
        }

        binding.accountBack.setOnClickListener {
            onBackPressed()
        }
    }
}