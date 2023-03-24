package uz.ibrohim.sovchiuz.read_data

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.databinding.FragmentWomanBinding

class WomanFragment : Fragment() {

    private var _binding: FragmentWomanBinding? = null
    private val binding get() = _binding!!
    private var db = FirebaseFirestore.getInstance()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWomanBinding.inflate(inflater, container, false)

        val bundle = this.arguments
        val uid = bundle!!.getString("uid").toString()

        val ref = db.collection("all_anketa")
        ref.document(uid).get()
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
                binding.txtYear.text = getString(R.string.yili_info) + year
                binding.txtProvince.text = getString(R.string.yashash) + province
                binding.txtNation.text = getString(R.string.millati) + nation
                binding.txtTall.text = getString(R.string.boyi) + tall
                binding.txtWeight.text = getString(R.string.vazni) + weight
                binding.txtHealth.text = getString(R.string.sogligi) + health
                binding.txtMarriage.text = getString(R.string.turmushi) + marriage
                binding.txtPrayer.text = getString(R.string.ibodatdaligi) + prayer
                binding.txtProfession.text = getString(R.string.kasbi) + profession
                binding.txtCondition.text = getString(R.string.nomzod_sharti) + condition
                binding.txtDate.text = it.data?.get("date").toString()
                binding.txtTime.text = it.data?.get("time").toString()
            }

        return binding.root
    }
}