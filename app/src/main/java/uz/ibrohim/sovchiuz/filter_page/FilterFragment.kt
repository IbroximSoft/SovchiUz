package uz.ibrohim.sovchiuz.filter_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.databinding.FragmentFilterBinding

class FilterFragment : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)

        maleOnClick()

        binding.womanBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.home_fr, FilterWFragment()).commit()
            binding.womanBtn.setBackgroundResource(R.drawable.filter_btn)
            binding.maleBtn.setBackgroundResource(android.R.color.transparent)
        }

        binding.maleBtn.setOnClickListener {
            maleOnClick()
        }

        return binding.root
    }

    private fun maleOnClick() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.home_fr, FilterMFragment()).commit()
        binding.maleBtn.setBackgroundResource(R.drawable.filter_btn)
        binding.womanBtn.setBackgroundResource(android.R.color.transparent)
    }
}