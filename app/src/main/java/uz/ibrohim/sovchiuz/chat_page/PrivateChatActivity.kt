package uz.ibrohim.sovchiuz.chat_page

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.ibrohim.sovchiuz.databinding.ActivityPrivateChatBinding
import uz.ibrohim.sovchiuz.message.FcmService
import uz.ibrohim.sovchiuz.message.NotificationDataModel
import uz.ibrohim.sovchiuz.message.NotificationModel
import uz.ibrohim.sovchiuz.message.UserChatData

class PrivateChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityPrivateChatBinding
    val db = FirebaseDatabase.getInstance().getReference("Chat")
    lateinit var list: ArrayList<PrivateChatData>
    lateinit var adapter: PrivateChatAdapter
    private lateinit var chat_key: String
    private var you_id: String = ""
    private var token: String? = null
    lateinit var uid: String
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivateChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        uid = mAuth.currentUser?.uid.toString()

        chat_key = intent.getStringExtra("chat_key").toString()
        you_id = intent.getStringExtra("you_id").toString()
        Toast.makeText(this, you_id, Toast.LENGTH_SHORT).show()
        // BU yerda ham byll boladi
        binding.message.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isEmpty()) {
                    binding.sendBtn.visibility = View.GONE
                } else {
                    binding.sendBtn.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        getToken()

        binding.sendBtn.setOnClickListener {
            db.child(chat_key).child("chat").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        Log.d("tessst", "bosildi")
                        val key = db.push().key.toString()
                        val data = PrivateChatData(
                            key,
                            binding.message.text.toString(),
                            uid,
                            System.currentTimeMillis().toString(),
                            "text",
                            ""
                        )
                        db.child(chat_key).child("chat").child(key).setValue(data)
                        notfication()
                        db.removeEventListener(this)
                    } else {
                        val key = db.push().key.toString()
                        val key2 = db.push().push().key.toString()
                        val u = UserChatData(uid, you_id, "Aktiv", key)
                        val data = PrivateChatData(
                            key,
                            binding.message.text.toString(),
                            uid,
                            System.currentTimeMillis().toString(),
                            "text",
                            ""
                        )

                        //key ni orniga chat_key ishlatibman chat_key null ku axri
                        db.child(key).child("chat").child(key2).setValue(data)
                        notfication()
                        db.removeEventListener(this)

                        db.child(key).setValue(u).addOnSuccessListener {
                            db.child(key).child("chat").child(key2).setValue(data)
                            chat_key = key
                            db.removeEventListener(read_chat_listern)
                            loadData()
                        }

                    }
                    db.removeEventListener(this)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        list = ArrayList()

        adapter = PrivateChatAdapter()
        binding.rec.layoutManager = LinearLayoutManager(this)
        binding.rec.adapter = adapter

        if (chat_key == "null") {
            getChatKey()
        }
        loadData()
    }

    private fun getChatKey() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (d in snapshot.children) {
                    if ((d.child("uid1").value.toString() == uid || d.child("uid2").value == uid) and
                        (d.child("uid1").value.toString() == you_id || d.child("uid2").value == you_id)
                    ) {
                        chat_key = d.child("key").value.toString()
                        db.removeEventListener(read_chat_listern)
                        loadData()
                        break
                    }
                }
                db.removeEventListener(this)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun loadData() {
        db.child(chat_key).child("chat").addValueEventListener(read_chat_listern)
    }

    val read_chat_listern = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            list.clear()
            for (d in snapshot.children) {
                val mainMenu: PrivateChatData? = d.getValue(PrivateChatData::class.java)
                list.add(mainMenu!!)
            }

            adapter.add(list)
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    }

    private fun getToken() {
        FirebaseDatabase.getInstance().getReference("users").child(you_id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    token = snapshot.child("token").value.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun notfication() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(FcmService::class.java)

        val notification = NotificationModel(
            to = token.toString(),
            data = NotificationDataModel(
                key = chat_key, // Yozish shart emas
                title = "Yangi xabar", // Sarlavha nomi
                body = "Assalomu alaykum", // Qisqa matn Yozilgan xabar bo'lishi mumkin
                token = token.toString(),
                to_id = uid,
                click_action = "ChatActivity", // Bunlarga tegmang
                image = null // random rasm uchun link
            )
        )

        service.sendNotification(notification).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                // xabar muvaffaqiyatli yuborildi
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // xabarni yuborishda xatolik yuz berdi
            }
        })

    }
}