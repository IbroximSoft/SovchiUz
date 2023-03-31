package uz.ibrohim.sovchiuz.more_page.profile.quest

import android.content.Intent
import android.graphics.Color
import android.os.Build
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
import uz.ibrohim.sovchiuz.databinding.ActivityMaleQuesBinding
import uz.ibrohim.sovchiuz.language.AppCompat
import uz.ibrohim.sovchiuz.more_page.profile.ProfileActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MaleQuesActivity : AppCompat() {

    private lateinit var binding: ActivityMaleQuesBinding
    private lateinit var reference: DatabaseReference
    private var db = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaleQuesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reference = FirebaseDatabase.getInstance().reference.child("users")
        binding.circularProgress.setColor(Color.RED, Color.MAGENTA, Color.YELLOW, Color.GREEN)
        binding.circularProgress.setBodyColor(R.color.progress_color)
        binding.circularProgress.setRotationSpeeed(25)

        binding.maleYear.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val yearSelected = calendar.get(Calendar.YEAR)
            val monthSelected = calendar.get(Calendar.MONTH)
            val dialogFragment: MonthYearPickerDialogFragment = MonthYearPickerDialogFragment
                .getInstance(monthSelected, yearSelected)
            dialogFragment.show(supportFragmentManager, null)
            dialogFragment.setOnDateSetListener { year, _ ->
                binding.maleYear.text = year.toString()
            }
        }

        binding.btnOn.setOnClickListener {
            checkData()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkData() {
        val year = binding.maleYear.text.toString().trim()
        val province = binding.maleProvince.text.toString().trim()
        val nation = binding.maleNation.text.toString().trim()
        val tall = binding.maleTall.text.toString().trim()
        val weight = binding.maleWeight.text.toString().trim()
        val health = binding.maleHealth.text.toString().trim()
        val marriage = binding.maleMarriage.text.toString().trim()
        val prayer = binding.malePrayer.text.toString().trim()
        val profession = binding.maleProfession.text.toString().trim()
        val condition = binding.maleCondition.text.toString().trim()
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
                "image" to "https://firebasestorage.googleapis.com/v0/b/sovchiuz-be26b.appspot.com/o/male.png?alt=media&token=43e95474-011a-4210-8f12-1de27ee5183f",
                year to province,
                "uid" to userID,
                "gender" to "male",
                "date" to currentDate,
                "time" to currentTime
            )
            db.collection("male_anketa").document(userID).set(dataMap)
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
        val language = resources.getStringArray(R.array.Province)
        val arrayAdapter = ArrayAdapter(this, R.layout.language_item, language)
        binding.maleProvince.setAdapter(arrayAdapter)
    }
}