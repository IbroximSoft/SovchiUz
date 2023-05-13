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
import es.dmoral.toasty.Toasty
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.databinding.FragmentUzcardBinding

class UzcardFragment : Fragment() {

    private var _binding: FragmentUzcardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUzcardBinding.inflate(inflater, container, false)

        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("user_card", Context.MODE_PRIVATE)

        binding.uzcardNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                when (binding.uzcardNumber.text.toString()) {
                    "9860" -> {
                        replaceFragment(HumoFragment())
                    }
                    "4231" -> {
                        replaceFragment(VisaFragment())
                    }
                }
            }
        })

        binding.uzcardHumo.setOnClickListener {
            replaceFragment(HumoFragment())
        }

        binding.uzcardVisa.setOnClickListener {
            replaceFragment(VisaFragment())
        }

        binding.uzcardBtn.setOnClickListener {
            val number = binding.uzcardNumber.text.toString()
            val year = binding.uzcardYear.text.toString()
            val name = binding.uzcardName.text.toString()
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
            putString("choose", "8600")
        }.apply()
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.pay_fragment, fragment)
        fragmentTransaction.commit()
    }
}