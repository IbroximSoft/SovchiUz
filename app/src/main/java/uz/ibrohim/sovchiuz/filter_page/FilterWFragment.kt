package uz.ibrohim.sovchiuz.filter_page

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.GridLayoutManager
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.databinding.FragmentFilterWBinding
import uz.ibrohim.sovchiuz.home_page.adapters.AnketaItems
import uz.ibrohim.sovchiuz.home_page.adapters.HomeAdapter
import java.util.*
import kotlin.collections.ArrayList

class FilterWFragment : Fragment() {

    private var _binding: FragmentFilterWBinding? = null
    private val binding get() = _binding!!

    private lateinit var booksList: ArrayList<AnketaItems>
    private lateinit var booksList2: ArrayList<AnketaItems>
    private var db = FirebaseFirestore.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private var q: Query? = null
    lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterWBinding.inflate(inflater, container, false)

        val gridLayoutManager = GridLayoutManager(context, 3)
        binding.filMRv.layoutManager = gridLayoutManager

        booksList = arrayListOf()
        booksList2= arrayListOf()
        dataGet()
        adapter = HomeAdapter(booksList)

        q = firestore.collection("woman_anketa")

        binding.searchM.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                searchUsers(p0)
                return true
            }

        })

        binding.filterM.setOnClickListener {
            showFilterDialog()
        }

        binding.clearFilter.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.home_fr, FilterWFragment()).commit()
        }
        return binding.root
    }

    private fun searchUsers(newText: String?) {

        adapter.filter.filter(newText)
    }

    private fun dataGet() {
        db.collection("woman_anketa").get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    for (data in it.documents) {
                        val anketaItems: AnketaItems? = data.toObject(AnketaItems::class.java)
                        if (anketaItems != null) {
                            booksList.add(anketaItems)
                        }
                    }
                    adapter = HomeAdapter(booksList)
                    binding.filMRv.adapter = adapter
                }
            }
    }

    private fun showFilterDialog(){
        val builder= AlertDialog.Builder(requireContext()).create()
        val view = layoutInflater.inflate(R.layout.filter_item,null)
        val  cancelBtn = view.findViewById<Button>(R.id.cancel)
        val  filterBtn = view.findViewById<Button>(R.id.filter)
        val  yearTxt = view.findViewById<TextView>(R.id.year)
        val  provinceTxt = view.findViewById<AutoCompleteTextView>(R.id.province)
        builder.setView(view)
        cancelBtn.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)

        filterBtn.setOnClickListener {
            if (yearTxt.text.isDigitsOnly()  && provinceTxt.text.isNotEmpty()){
                db.collection("woman_anketa").get()
                    .addOnSuccessListener {
                        if (!it.isEmpty) {
                            for (data in it.documents) {
                                val anketaItems: AnketaItems? = data.toObject(AnketaItems::class.java)
                                if (anketaItems != null) {
                                    if (anketaItems.province == provinceTxt.text.toString() && anketaItems.year == yearTxt.text.toString()) {
                                        booksList2.add(anketaItems)
                                    }
                                }
                            }
                            binding.filMRv.adapter = HomeAdapter(booksList2)
                            binding.textNoData.visibility=View.INVISIBLE
                            if (booksList2.isEmpty()) {
                                binding.textNoData.visibility=View.VISIBLE
                            }
                        }
                    }
                booksList2.clear()
                builder.dismiss()
            }else{
                Toast.makeText(requireContext(), "Iltimos Barcha Maydonlarni To'ldiring", Toast.LENGTH_SHORT).show()
            }
        }
        yearTxt.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val yearSelected = calendar.get(Calendar.YEAR)
            val monthSelected = calendar.get(Calendar.MONTH)
            val dialogFragment: MonthYearPickerDialogFragment = MonthYearPickerDialogFragment
                .getInstance(monthSelected, yearSelected)
            val supportFragmentManager=fragmentManager
            if (supportFragmentManager != null) {
                dialogFragment.show(supportFragmentManager, null)
            }
            dialogFragment.setOnDateSetListener { year, _ ->
                yearTxt.text = year.toString()
            }
        }
        val language = resources.getStringArray(R.array.Province)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.language_item, language)
        provinceTxt.setAdapter(arrayAdapter)
        builder.show()
    }
}