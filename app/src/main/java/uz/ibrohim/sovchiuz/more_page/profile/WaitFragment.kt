package uz.ibrohim.sovchiuz.more_page.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uz.ibrohim.sovchiuz.databinding.FragmentWaitBinding

class WaitFragment : Fragment() {

    private var _binding: FragmentWaitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWaitBinding.inflate(inflater, container, false)

        return binding.root
    }
}