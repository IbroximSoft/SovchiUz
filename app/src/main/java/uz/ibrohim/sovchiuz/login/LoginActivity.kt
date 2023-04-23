package uz.ibrohim.sovchiuz.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import es.dmoral.toasty.Toasty
import uz.ibrohim.sovchiuz.App
import uz.ibrohim.sovchiuz.HomeActivity
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.TermsActivity
import uz.ibrohim.sovchiuz.databinding.ActivityLoginBinding
import uz.ibrohim.sovchiuz.language.AppCompat

class LoginActivity : AppCompat() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var reference: DatabaseReference
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        reference = FirebaseDatabase.getInstance().reference

        binding.circularProgress.setColor(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN)
        binding.circularProgress.setBodyColor(R.color.progress_color)
        binding.circularProgress.setRotationSpeeed(25)

        binding.loginBtn.setOnClickListener {
            userLogin()
        }
        binding.registerText.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginBack.setOnClickListener{
            val intent = Intent(this@LoginActivity, TermsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun userLogin() {
        val emails: String = binding.loginEmail.text.toString()
        val email = "$emails@sovchi.uz"
        val password: String = binding.loginPassword.text.toString()

        if (TextUtils.isEmpty(emails)) {
            Toasty.warning(this@LoginActivity, getString(R.string.number_enter),
                Toasty.LENGTH_SHORT, true).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toasty.warning(this@LoginActivity, getString(R.string.password),
                Toasty.LENGTH_SHORT, true).show()
            return
        }
        binding.loginBtn.visibility = View.GONE
        binding.linearBtn.visibility = View.VISIBLE
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                statusTest(task)
            }
    }

    private fun statusTest(task: Task<AuthResult>) {
        binding.linearBtn.visibility = View.GONE
        binding.loginBtn.visibility = View.VISIBLE
        if (task.isSuccessful) {

            val currentUserID = mAuth.currentUser!!.uid
            FirebaseMessaging.getInstance().token
                .addOnCompleteListener(OnCompleteListener { tasks ->
                    if (!task.isSuccessful) {
                        Toast.makeText(this,
                            "Hatolik yuz berdi qayta urinib ko'ring", Toast.LENGTH_SHORT
                        ).show()
                        return@OnCompleteListener
                    }
                    token = tasks.result
                    reference.child("users").child(currentUserID)
                        .child("token").setValue(token)
                })

            reference.child("users").child(currentUserID).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val status = snapshot.child("status").value.toString()
                    val name = snapshot.child("name").value.toString()
                    val sharedPreferences: SharedPreferences = App.shared.sharedPreferences
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.apply {
                        putString("status", status)
                        putString("name", name)
                    }.apply()
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@LoginActivity, getString(R.string.error_again),
                        Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toasty.error(this@LoginActivity, getString(R.string.number_incorrect),
                Toasty.LENGTH_SHORT, true).show()
        }
    }
}