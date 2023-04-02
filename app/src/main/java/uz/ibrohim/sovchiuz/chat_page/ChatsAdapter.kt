package uz.ibrohim.sovchiuz.chat_page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import uz.ibrohim.sovchiuz.databinding.ChatsLayoutBinding
import uz.ibrohim.sovchiuz.message.UserChatData
import java.text.SimpleDateFormat
import java.util.*


class ChatsAdapter(val click: Click):RecyclerView.Adapter<ChatsAdapter.Holder>() {

    var list: ArrayList<UserChatData> = ArrayList()

    fun add(l: ArrayList<UserChatData>){
        list = l
        notifyDataSetChanged()
    }

    class Holder(val item: ChatsLayoutBinding):RecyclerView.ViewHolder(item.root) {
        fun with(data: UserChatData) {
            CoroutineScope(Dispatchers.IO).launch {
                if (data.uid1 != currentUserid) {
                    val username = getUsernameFromId(data.uid1.toString())
                    withContext(Dispatchers.Main) {
                        item.userFirstName.text = username
                    }
                } else {
                    val username = getUsernameFromId(data.uid2.toString())
                    withContext(Dispatchers.Main) {
                        item.userFirstName.text = username
                    }
                }
            }

            FirebaseDatabase.getInstance().getReference("Chat").child(data.key.toString()).child("chat").limitToLast(1).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(d in snapshot.children) {
                        val privateChatData: PrivateChatData =
                            d.getValue(PrivateChatData::class.java)!!
                        item.message.text = privateChatData.message
                        item.date.text = convertLongToTime(privateChatData.time.toLong())
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        }

        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("HH:mm")
            return format.format(date)
        }

        var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        var currentUserid: String = mAuth.currentUser?.uid.toString()

        suspend fun getUsernameFromId(id: String): String? {
            val usersRef = FirebaseDatabase.getInstance().getReference("users")
            val userRef = usersRef.child(id)

            return try {
                userRef.child("name").get().await().value as? String
            } catch (e: Exception) {
                null
            }


        }



    }

    interface Click{
        fun click(data: UserChatData){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = ChatsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.with(list[position])
        holder.itemView.setOnClickListener {
            click.click(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}