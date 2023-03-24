package uz.ibrohim.sovchiuz.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import es.dmoral.toasty.Toasty
import uz.ibrohim.sovchiuz.HomeActivity
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.databinding.ActivityRegisterBinding
import uz.ibrohim.sovchiuz.language.AppCompat
import java.util.HashMap

class RegisterActivity : AppCompat() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        reference = FirebaseDatabase.getInstance().reference

        binding.circularProgress.setColor(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN)
        binding.circularProgress.setBodyColor(R.color.progress_color)
        binding.circularProgress.setRotationSpeeed(25)

        binding.registerBtn.setOnClickListener {
            authData()
        }

        binding.registerBack.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding.registerName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                val result = editable.toString().replace(" ".toRegex(), "")
                if (editable.toString() != result) {
                    binding.registerName.setText(result)
                    binding.registerName.setSelection(result.length)
                    Toasty.warning(this@RegisterActivity, getString(R.string.possible_not),
                        Toasty.LENGTH_SHORT, true).show()
                }
            }
        })

        binding.registerPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                val result = editable.toString().replace(" ".toRegex(), "")
                if (editable.toString() != result) {
                    binding.registerPassword.setText(result)
                    binding.registerPassword.setSelection(result.length)
                    Toasty.warning(this@RegisterActivity, getString(R.string.possible_not),
                        Toasty.LENGTH_SHORT, true).show()
                }
            }
        })
    }

    private fun authData() {
        val name: String = binding.registerName.text.toString()
        val emails: String = binding.registerEmail.text.toString()
        val password: String = binding.registerPassword.text.toString()
        val password2: String = binding.registerPassword2.text.toString()
        val email = "$emails@sovchi.uz"
        if (TextUtils.isEmpty(name)) {
            Toasty.warning(this@RegisterActivity, getString(R.string.enter_name),
                Toasty.LENGTH_SHORT, true).show()
            return
        }
        if (TextUtils.isEmpty(emails)) {
            Toasty.warning(this@RegisterActivity, getString(R.string.number_enter),
                Toasty.LENGTH_SHORT, true).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toasty.warning(this@RegisterActivity, getString(R.string.number_enter),
                Toasty.LENGTH_SHORT, true).show()
            return
        }
        if (TextUtils.isEmpty(password2)) {
            Toasty.warning(this@RegisterActivity, getString(R.string.password_again),
                Toasty.LENGTH_SHORT, true).show()
            return
        }
        if (password != password2) {
            Toasty.error(
                this@RegisterActivity, getString(R.string.password_not_suitable),
                Toasty.LENGTH_SHORT, true).show()
            return
        }

        binding.registerBtn.visibility = View.GONE
        binding.linearBtn.visibility = View.VISIBLE

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                binding.linearBtn.visibility = View.GONE
                binding.registerBtn.visibility = View.VISIBLE
                if (task.isSuccessful) {
                    val currentUserID = mAuth.currentUser!!.uid
                    val dataHas = HashMap<String, String>()
                    dataHas["name"] = name
                    dataHas["email"] = email
                    dataHas["password"] = password
                    dataHas["balance"] = "0"
                    dataHas["status"] = "no"
                    dataHas["uid"] = currentUserID
                    reference.child("users").child(currentUserID).setValue(dataHas)
                        .addOnCompleteListener {
                            Toasty.success(this@RegisterActivity, getString(R.string.success),
                                Toasty.LENGTH_SHORT, true).show()
                            val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                            val sharedPreferences: SharedPreferences =
                                getSharedPreferences("user_status", Context.MODE_PRIVATE)
                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
                            editor.apply {
                                putString("status", "no")
                                putString("name", name)
                            }.apply()
                        }
                } else {
                    Toasty.error(this@RegisterActivity, getString(R.string.there_number),
                        Toasty.LENGTH_SHORT, true).show()
                }
            }
    }
}