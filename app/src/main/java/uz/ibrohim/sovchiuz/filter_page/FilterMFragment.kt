package uz.ibrohim.sovchiuz.filter_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.*
import uz.ibrohim.sovchiuz.databinding.FragmentFilterMBinding
import uz.ibrohim.sovchiuz.home_page.adapters.AnketaItems
import uz.ibrohim.sovchiuz.home_page.adapters.HomeAdapter
import java.util.*
import kotlin.collections.ArrayList


class FilterMFragment : Fragment() {
    private var _binding: FragmentFilterMBinding? = null
    private val binding get() = _binding!!
    private lateinit var booksList: ArrayList<AnketaItems>
    private var db = FirebaseFirestore.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val reference = firestore.collection("male_anketa")
    private var q: Query? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterMBinding.inflate(inflater, container, false)

        val gridLayoutManager = GridLayoutManager(context, 3)
        binding.filMRv.layoutManager = gridLayoutManager

        booksList = arrayListOf()
        dataGet()

        q = firestore.collection("male_anketa")

        binding.searchM.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchUsers(p0)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                searchUsers(p0)
                return false
            }

        })

        return binding.root
    }

    private fun searchUsers(newText: String?) {
        q = reference.orderBy("province").startAt(newText.toString().uppercase())
            .endAt(newText.toString().lowercase() + "\uf8ff")
        q!!.get().addOnCompleteListener { task ->
            val names: MutableList<AnketaItems> = ArrayList()
            if (task.isSuccessful) {
                for (document in task.result) {
                    val model: AnketaItems = document.toObject(AnketaItems::class.java)
                    names.add(model)
                }
                binding.filMRv.adapter = HomeAdapter(names)
            }
        }
    }

    private fun dataGet(){
        db.collection("male_anketa").get()
            .addOnSuccessListener {
                if (!it.isEmpty){
                    for (data in it.documents){
                        val anketaItems: AnketaItems? = data.toObject(AnketaItems::class.java)
                        if (anketaItems != null) {
                            booksList.add(anketaItems)
                        }
                    }
                    binding.filMRv.adapter = HomeAdapter(booksList)
                }
            }
    }
}