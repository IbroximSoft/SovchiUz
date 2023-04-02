package uz.ibrohim.sovchiuz.more_page.profile.edit_quest

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import es.dmoral.toasty.Toasty
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.databinding.ActivityWomanEditQuesBinding
import uz.ibrohim.sovchiuz.more_page.profile.ProfileActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class WomanEditQuesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWomanEditQuesBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference
    private var db = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWomanEditQuesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentUserID = auth.currentUser?.uid.toString()
        reference = FirebaseDatabase.getInstance().reference.child("users")

        binding.womanYear.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val yearSelected = calendar.get(Calendar.YEAR)
            val monthSelected = calendar.get(Calendar.MONTH)
            val dialogFragment: MonthYearPickerDialogFragment = MonthYearPickerDialogFragment
                .getInstance(monthSelected, yearSelected)
            dialogFragment.show(supportFragmentManager, null)
            dialogFragment.setOnDateSetListener { year, _ ->
                binding.womanYear.text = year.toString()
            }
        }

        val ref = db.collection("all_anketa")
        ref.document(currentUserID).get()
            .addOnSuccessListener {
                val year = it.data?.get("year").toString()
                val nation = it.data?.get("nation").toString()
                val tall = it.data?.get("tall").toString()
                val weight = it.data?.get("weight").toString()
                val health = it.data?.get("health").toString()
                val marriage = it.data?.get("marriage").toString()
                val prayer = it.data?.get("prayer").toString()
                val profession = it.data?.get("profession").toString()
                val condition = it.data?.get("condition").toString()

                binding.womanYear.text = year
                binding.womanNation.setText(nation)
                binding.womanTall.setText(tall)
                binding.womanWeight.setText(weight)
                binding.womanHealth.setText(health)
                binding.womanMarriage.setText(marriage)
                binding.womanPrayer.setText(prayer)
                binding.womanProfession.setText(profession)
                binding.womanCondition.setText(condition)
            }
        binding.btnOn.setOnClickListener {
            checkData()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkData() {
        val year = binding.womanYear.text.toString().trim()
        val province = binding.womanProvince.text.toString().trim()
        val nation = binding.womanNation.text.toString().trim()
        val tall = binding.womanTall.text.toString().trim()
        val weight = binding.womanWeight.text.toString().trim()
        val health = binding.womanHealth.text.toString().trim()
        val marriage = binding.womanMarriage.text.toString().trim()
        val prayer = binding.womanPrayer.text.toString().trim()
        val profession = binding.womanProfession.text.toString().trim()
        val condition = binding.womanCondition.text.toString().trim()
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        if (year == getString(R.string.yilingizni_kiriting) || province == getString(R.string.yashash_joy) ||
            nation.isEmpty() || tall.isEmpty() || weight.isEmpty() || health.isEmpty() ||
            marriage.isEmpty() || prayer.isEmpty() || profession.isEmpty() || condition.isEmpty()
        ) {
            Toasty.warning(
                this, getString(R.string.you_must_pay_in_full),
                Toasty.LENGTH_SHORT, true
            ).show()
        } else {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val currentDate = LocalDateTime.now().format(formatter)
            val time = DateTimeFormatter.ofPattern("HH:mm")
            val currentTime = LocalDateTime.now().format(time)

            binding.btnOn.visibility = View.GONE
            binding.linearBtn.visibility = View.VISIBLE
            val dataMap = hashMapOf(
                "year" to year,
                "province" to province,
                "nation" to nation,
                "tall" to tall,
                "weight" to weight,
                "health" to health,
                "marriage" to marriage,
                "prayer" to prayer,
                "profession" to profession,
                "condition" to condition,
                "image" to "https://firebasestorage.googleapis.com/v0/b/sovchiuz-be26b.appspot.com/o/woman.png?alt=media&token=510dfad1-ba96-432e-8474-117f47ee2871",
                year to province,
                "uid" to userID,
                "gender" to "woman",
                "date" to currentDate,
                "time" to currentTime
            )
            db.collection("woman_anketa").document(userID).set(dataMap)
                .addOnSuccessListener {
                    db.collection("all_anketa").document(userID).set(dataMap)
                    reference.child(userID).child("status").setValue("wait")
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                    binding.linearBtn.visibility = View.GONE
                    binding.btnOn.visibility = View.VISIBLE
                }
        }
    }

    override fun onResume() {
        super.onResume()
        val province = intent.getStringExtra("province")
        binding.womanProvince.setText(province)
        val language = resources.getStringArray(R.array.Province)
        val arrayAdapter = ArrayAdapter(this, R.layout.language_item, language)
        binding.womanProvince.setAdapter(arrayAdapter)
    }
}