package uz.ibrohim.sovchiuz.more_page.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appuj.customizedalertdialoglib.CustomizedAlertDialog
import com.appuj.customizedalertdialoglib.CustomizedAlertDialogCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import uz.ibrohim.sovchiuz.App
import uz.ibrohim.sovchiuz.HomeActivity
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.databinding.FragmentProfileBinding
import uz.ibrohim.sovchiuz.more_page.offer.CameActivity
import uz.ibrohim.sovchiuz.more_page.offer.SendActivity
import uz.ibrohim.sovchiuz.more_page.profile.edit_quest.MaleEditQuesActivity
import uz.ibrohim.sovchiuz.more_page.profile.edit_quest.WomanEditQuesActivity
import uz.ibrohim.sovchiuz.screens.ScreenActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private var db = FirebaseFirestore.getInstance()
    private lateinit var reference: DatabaseReference
    private var gender: String = ""
    private var province: String = ""
    private var key: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        reference = FirebaseDatabase.getInstance().reference
        val currentUserID = auth.currentUser?.uid.toString()

        val sharedPreference: SharedPreferences =
            activity?.applicationContext?.getSharedPreferences(
                "user_status",
                Context.MODE_PRIVATE
            )!!
        val name = sharedPreference.getString("name", null)
        binding.name.text = name.toString()

        binding.apply {
            linearSend.setOnClickListener {
                val intent = Intent(requireContext(), SendActivity::class.java)
                startActivity(intent)
            }
            linearCame.setOnClickListener {
                val intent = Intent(requireContext(), CameActivity::class.java)
                startActivity(intent)
            }
        }
        val ref = db.collection("all_anketa")
        ref.document(currentUserID).get()
            .addOnSuccessListener {
                val year = it.data?.get("year").toString() + getString(R.string.year_info)
                province = it.data?.get("province").toString()
                val nation = it.data?.get("nation").toString()
                val tall = it.data?.get("tall").toString() + " sm"
                val weight = it.data?.get("weight").toString() + " kg"
                val health = it.data?.get("health").toString()
                val marriage = it.data?.get("marriage").toString()
                val prayer = it.data?.get("prayer").toString()
                val profession = it.data?.get("profession").toString()
                val condition = it.data?.get("condition").toString()
                gender = it.data?.get("gender").toString()

                binding.year.text = year
                binding.province.text = province
                binding.nation.text = nation
                binding.tall.text = tall
                binding.weight.text = weight
                binding.health.text = health
                binding.marriage.text = marriage
                binding.prayer.text = prayer
                binding.profession.text = profession
                binding.condition.text = condition
            }

        binding.editBtn.setOnClickListener {
            if (gender == "male") {
                val intent = Intent(requireContext(), MaleEditQuesActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("province", province)
                startActivity(intent)
            } else if (gender == "woman") {
                val intent = Intent(requireContext(), WomanEditQuesActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("province", province)
                startActivity(intent)
            }
        }

        binding.deleteBtn.setOnClickListener {
            CustomizedAlertDialog.callAlertDialog(requireContext(), "Diqqat!",
                "Siz rostdan ham anketangizni o'chirmoqchimisiz ? Xa tugmasini bossangiz barcha yozishmalar ham o'chib ketadi",
                "Xa", "Yo'q", false,
                object : CustomizedAlertDialogCallback<String> {
                    override fun alertDialogCallback(callback: String) {
                        if (callback == "1") {
                            deleteData(currentUserID)
                        }
                    }
                })
        }

        return binding.root
    }

    private fun deleteData(currentUserID: String) {
        db.collection("all_anketa").document(currentUserID).delete()
        db.collection("male_anketa").document(currentUserID).delete()
        db.collection("woman_anketa").document(currentUserID).delete()
            .addOnSuccessListener {
                reference.child("users").child(currentUserID).child("status").setValue("no")
                reference.child("Chat")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (d in snapshot.children) {
                                if (d.child("uid1").exists() and d.child("uid2").exists() and
                                    (d.child("uid1").value == currentUserID ||
                                            d.child("uid2").value == currentUserID) and
                                    d.child("status").exists() and d.child("chat").exists() and
                                    d.child("key").exists()) {
                                    key = d.child("key").value.toString()
                                    chatDelete()
                                    App.shared.deleteDatas(requireContext())
                                    val intent = Intent(requireContext(), ScreenActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
            }
    }

    private fun chatDelete() {
        for (delete in key){
            reference.child("Chat").child(key).removeValue()
        }
    }
}