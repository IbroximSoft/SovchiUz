package uz.ibrohim.sovchiuz.more_page

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.databinding.FragmentMoreBinding
import uz.ibrohim.sovchiuz.more_page.favorite.FavoriteActivity
import uz.ibrohim.sovchiuz.more_page.moreAdapter.List
import uz.ibrohim.sovchiuz.more_page.moreAdapter.MoreAdapter
import uz.ibrohim.sovchiuz.more_page.profile.ProfileActivity

class MoreFragment : Fragment() {

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!
    private lateinit var userArrayList: ArrayList<List>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoreBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageId = intArrayOf(
            R.drawable.more_notification, R.drawable.more_favorite,
            R.drawable.more_account, R.drawable.ic_happy,
            R.drawable.more_language, R.drawable.more_about,
            R.drawable.more_privacy, R.drawable.more_logout
        )

        val text = arrayOf(
            "Yangiliklar",
            "Saqlangan",
            "Profil",
            "Baxtini topganlar",
            "Til",
            "Biz haqimizda",
            "Shartlar va xavfsizlik",
            "Chiqish"
        )

        userArrayList = ArrayList()

        for (i in text.indices) {
            val list = List(text[i], imageId[i])
            userArrayList.add(list)
        }

        binding.listMore.isClickable = true
        binding.listMore.adapter = MoreAdapter(view.context as Activity, userArrayList)

        binding.listMore.onItemClickListener =
            AdapterView.OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                when (position) {
                    0 -> {
                        Toast.makeText(context, "Notification", Toast.LENGTH_SHORT).show()
                    }
                    1 -> {
                        startActivity(Intent(requireContext(), FavoriteActivity::class.java))
                    }
                    2 -> {
                        startActivity(Intent(requireContext(), ProfileActivity::class.java))
                    }
                    3 -> {
                        Toast.makeText(context, "Baxtini topganlar", Toast.LENGTH_SHORT).show()
                    }
                    4 -> {
                        Toast.makeText(context, "Til", Toast.LENGTH_SHORT).show()

                    }
                    5 -> {
                        Toast.makeText(context, "Biz haqimizda", Toast.LENGTH_SHORT).show()

                    }
                    6 -> {
                        Toast.makeText(context, "Shartlar va xavfsizlik", Toast.LENGTH_SHORT).show()

                    }
                    7 -> {
                        Toast.makeText(context, "Chiqish", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}