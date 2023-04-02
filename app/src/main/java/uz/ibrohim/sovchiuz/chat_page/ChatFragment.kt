package uz.ibrohim.sovchiuz.chat_page

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import uz.ibrohim.sovchiuz.databinding.FragmentChatBinding
import uz.ibrohim.sovchiuz.message.UserChatData

class ChatFragment : Fragment(), ChatsAdapter.Click {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    val db = FirebaseDatabase.getInstance().getReference("Chat")
    lateinit var adapter: ChatsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        val currentUserID = auth.currentUser?.uid.toString()
        adapter = ChatsAdapter(this)

        binding.rvAll.layoutManager = LinearLayoutManager(requireContext())

        val list: ArrayList<UserChatData> = ArrayList()

        db.orderByChild("status")
            .equalTo("Aktiv")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        binding.rvAll.visibility = View.GONE
                        binding.linerFavorite.visibility = View.VISIBLE
                    }
                    list.clear()
                    for (d in snapshot.children) {
                        if (d.child("uid1").exists() and d.child("uid2").exists() and
                            (d.child("uid1").value == currentUserID ||
                                    d.child("uid2").value == currentUserID) and
                            d.child("status").exists() and d.child("chat").exists() and
                            d.child("key").exists()) {
                            val mainMenu: UserChatData? = d.getValue(UserChatData::class.java)
                            list.add(mainMenu!!)
                        }
                    }
                    adapter.add(list)
                    binding.rvAll.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        return binding.root
    }

    override fun click(data: UserChatData) {
        val intent = Intent()
        intent.putExtra("chat_key", data.key)
        val currentUserID = auth.currentUser?.uid

        if (currentUserID == data.uid1) {
            intent.putExtra("you_id", data.uid2)
        } else {
            intent.putExtra("you_id", data.uid1)
        }
        intent.setClass(requireContext(), PrivateChatActivity::class.java)
        startActivity(intent)
    }
}