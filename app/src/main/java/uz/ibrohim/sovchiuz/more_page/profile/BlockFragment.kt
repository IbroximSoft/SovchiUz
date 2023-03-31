package uz.ibrohim.sovchiuz.more_page.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.databinding.FragmentBlockBinding

class BlockFragment : Fragment() {

    private var _binding: FragmentBlockBinding? = null
    private val binding get() = _binding!!
    private var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBlockBinding.inflate(inflater, container, false)
        val userID = FirebaseAuth.getInstance().currentUser!!.uid

        binding.btnBlock.setOnClickListener {
            dataRemove(userID)
        }

        return binding.root
    }

    private fun dataRemove(userID: String) {
        val ref:DocumentReference = db.collection("all_anketa").document(userID)
        ref.delete()
        val refM:DocumentReference = db.collection("male_anketa").document(userID)
        refM.delete()
        val refW:DocumentReference = db.collection("woman_anketa").document(userID)
        refW.delete()
        val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
        reference.child("users").child(userID).child("status").setValue("no")
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.account_frame, InformationFragment())?.commit()
    }
}