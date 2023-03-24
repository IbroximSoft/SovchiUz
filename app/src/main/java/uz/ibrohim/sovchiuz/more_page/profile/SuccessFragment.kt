package uz.ibrohim.sovchiuz.more_page.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.databinding.FragmentSuccessBinding

class SuccessFragment : Fragment() {

    private var _binding: FragmentSuccessBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuccessBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        reference = FirebaseDatabase.getInstance().reference

        binding.btnSuccess.setOnClickListener {
            val currentUserID = auth.currentUser?.uid
            reference.child("users").child(currentUserID!!).child("status").setValue("yes")
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.account_frame, ProfileFragment())?.commit()
        }

        return binding.root
    }
}