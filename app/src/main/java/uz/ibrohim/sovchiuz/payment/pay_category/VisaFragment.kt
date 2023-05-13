package uz.ibrohim.sovchiuz.payment.pay_category

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import es.dmoral.toasty.Toasty
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.databinding.FragmentVisaBinding

class VisaFragment : Fragment() {

    private var _binding: FragmentVisaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisaBinding.inflate(inflater, container, false)

        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("user_card", Context.MODE_PRIVATE)

        binding.visaNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                when (binding.visaNumber.text.toString()) {
                    "8600" -> {
                        replaceFragment(UzcardFragment())
                    }
                    "9860" -> {
                        replaceFragment(HumoFragment())
                    }
                }
            }
        })

        binding.visaUzcard.setOnClickListener {
            replaceFragment(UzcardFragment())
        }

        binding.visaHumo.setOnClickListener {
            replaceFragment(HumoFragment())
        }

        binding.visaBtn.setOnClickListener {
            val number = binding.visaNumber.text.toString()
            val year = binding.visaYear.text.toString()
            val name = binding.visaName.text.toString()
            if (TextUtils.isEmpty(number)) {
                Toasty.warning(requireContext(), "Plastik raqamini kiriting",
                    Toasty.LENGTH_SHORT, true).show()
                return@setOnClickListener
            } else if (TextUtils.isEmpty(year)) {
                Toasty.warning(requireContext(), "Plastik raqam tilini kiriting",
                    Toasty.LENGTH_SHORT, true).show()
                return@setOnClickListener
            }
            getDatas(number, year, name, sharedPreferences)
        }

        return binding.root
    }

    private fun getDatas(
        number: String,
        year: String,
        name: String,
        sharedPreferences: SharedPreferences) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.apply {
            putString("number", number)
            putString("year", year)
            putString("name", name)
            putString("choose", "9860")
        }.apply()
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.pay_fragment, fragment)
        fragmentTransaction.commit()
    }
}