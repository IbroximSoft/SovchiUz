package uz.ibrohim.sovchiuz.more_page.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import uz.ibrohim.sovchiuz.databinding.ActivityFavoriteBinding
import uz.ibrohim.sovchiuz.home_page.adapters.AnketaItems

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var dbRef : DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var userArrayList : ArrayList<AnketaItems>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentUserID = auth.currentUser?.uid.toString()
        dbRef = FirebaseDatabase.getInstance().getReference("all_favorite").child(currentUserID)

        val gridLayoutManager = GridLayoutManager(this, 3)
        binding.rvAll.layoutManager = gridLayoutManager
        userArrayList = arrayListOf()
        getUserData()

        binding.favoriteBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getUserData() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(AnketaItems::class.java)
                        userArrayList.add(user!!)
                    }
                    binding.rvAll.adapter = FavoriteAdapter(userArrayList)
                }else{
                    binding.rvAll.visibility = View.GONE
                    binding.linerFavorite.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}