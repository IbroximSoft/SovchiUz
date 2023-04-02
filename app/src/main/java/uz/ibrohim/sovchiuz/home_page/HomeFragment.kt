package uz.ibrohim.sovchiuz.home_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import uz.ibrohim.sovchiuz.databinding.FragmentHomeBinding
import uz.ibrohim.sovchiuz.home_page.adapters.AnketaItems
import uz.ibrohim.sovchiuz.home_page.adapters.HomeAdapter


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var booksList: ArrayList<AnketaItems>
    private var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val gridLayoutManager = GridLayoutManager(context, 3)
        binding.rvAll.layoutManager = gridLayoutManager

        booksList = arrayListOf()

        db.collection("all_anketa").get()
            .addOnSuccessListener {
                if (!it.isEmpty){
                    for (data in it.documents){
                        val anketaItems: AnketaItems? = data.toObject(AnketaItems::class.java)
                        if (anketaItems != null) {
                            booksList.add(anketaItems)
                        }
                    }
                    binding.rvAll.adapter = HomeAdapter(booksList)
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Hatolik yuz berdi", Toast.LENGTH_SHORT).show()
            }

        return binding.root
    }

    //reference = FirebaseDatabase.getInstance().reference
    //            val userID = FirebaseAuth.getInstance().currentUser!!.uid
    //            reference.child("users").child(userID).child("complaint")
    //                .runTransaction(object : Transaction.Handler {
    //                    override fun doTransaction(currentData: MutableData): Transaction.Result {
    //                        if (currentData.value == null) {
    //                            currentData.value = 1
    //                        } else {
    //                            currentData.value = (currentData.value as Long?)!! + 1
    //                        }
    //                        return Transaction.success(currentData)
    //                    }
    //
    //                    override fun onComplete(
    //                        error: DatabaseError?,
    //                        committed: Boolean,
    //                        currentData: DataSnapshot?) {
    //                    }
    //                })
}