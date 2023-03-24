package uz.ibrohim.sovchiuz.more_page.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        val currentUserID = auth.currentUser?.uid.toString()

        val sharedPreference: SharedPreferences =
            activity?.applicationContext?.getSharedPreferences(
                "user_status",
                Context.MODE_PRIVATE
            )!!
        val name = sharedPreference.getString("name", null)
        binding.name.text = name.toString()

        val ref = db.collection("all_anketa")
        ref.document(currentUserID).get()
            .addOnSuccessListener {
                val year = it.data?.get("year").toString() + getString(R.string.year_info)
                val province = it.data?.get("province").toString()
                val nation = it.data?.get("nation").toString()
                val tall = it.data?.get("tall").toString() + " sm"
                val weight = it.data?.get("weight").toString() + " kg"
                val health = it.data?.get("health").toString()
                val marriage = it.data?.get("marriage").toString()
                val prayer = it.data?.get("prayer").toString()
                val profession = it.data?.get("profession").toString()
                val condition = it.data?.get("condition").toString()

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
        return binding.root
    }
}