package uz.ibrohim.sovchiuz.filter_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.ibrohim.sovchiuz.databinding.FragmentFilterWBinding

class FilterWFragment : Fragment() {

    private var _binding: FragmentFilterWBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterWBinding.inflate(inflater, container, false)

        return binding.root
    }
}