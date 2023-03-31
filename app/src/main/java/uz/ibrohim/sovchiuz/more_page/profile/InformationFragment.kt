package uz.ibrohim.sovchiuz.more_page.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.thecode.aestheticdialogs.AestheticDialog
import com.thecode.aestheticdialogs.DialogStyle
import com.thecode.aestheticdialogs.DialogType
import uz.ibrohim.sovchiuz.databinding.FragmentInformationBinding
import uz.ibrohim.sovchiuz.more_page.profile.quest.MaleQuesActivity
import uz.ibrohim.sovchiuz.more_page.profile.quest.WomanQuesActivity

class InformationFragment : Fragment() {

    private var _binding: FragmentInformationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInformationBinding.inflate(inflater, container, false)

        binding.infoMale.setOnClickListener {
            val intent = Intent(requireContext(), MaleQuesActivity::class.java)
            startActivity(intent)
        }

        binding.infoWoman.setOnClickListener {
            val intent = Intent(requireContext(), WomanQuesActivity::class.java)
            startActivity(intent)
        }

        binding.infoQuestion.setOnClickListener {
            AestheticDialog.Builder(
                requireActivity(),
                DialogStyle.RAINBOW,
                DialogType.INFO)
                .setTitle("Qo'shimcha")
                .setMessage("O'zingizni jinsingizni tanlang!")
                .setDarkMode(true)
                .show()
        }
        return binding.root
    }
}