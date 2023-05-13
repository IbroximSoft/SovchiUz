package uz.ibrohim.sovchiuz.payment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import uz.ibrohim.sovchiuz.databinding.ActivityPayInfoBinding
import uz.ibrohim.sovchiuz.language.AppCompat

class PayInfoActivity : AppCompat() {

    private lateinit var binding: ActivityPayInfoBinding
    private lateinit var db: DatabaseReference
    private lateinit var userArrayList : ArrayList<PayItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance().reference

        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.payRv.layoutManager = manager
        userArrayList = arrayListOf()
        getUserData()
    }

    private fun getUserData() {
        db.child("admin_setting").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val user:PayItem? = userSnapshot.getValue(PayItem::class.java)
                        userArrayList.add(user!!)
                    }
                    binding.payRv.adapter = PayAdapter(userArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}