package uz.ibrohim.sovchiuz.chat_page

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.databinding.ChatItemBinding
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

    class Holder(val item: ChatItemBinding):RecyclerView.ViewHolder(item.root) {
        var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        var currentUserid: String = mAuth.currentUser?.uid.toString()
        fun with(data: UserChatData) {
            CoroutineScope(Dispatchers.IO).launch {

                if (data.uid1 != currentUserid) {
                    val username = getUsernameFromId(data.uid1.toString())
                    getOnlineFromId(data.uid1,item.onlineImg)
                    withContext(Dispatchers.Main) {
                        item.userFirstName.text = username
                    }
                } else {
                    val username = getUsernameFromId(data.uid2.toString())
                    getOnlineFromId(data.uid2.toString(),item.onlineImg)
                    withContext(Dispatchers.Main) {
                        item.userFirstName.text = username
                    }
                }
            }

            FirebaseDatabase.getInstance().getReference("Chat").child(data.key.toString())
                .child("chat").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var i = 0
                    for (d in snapshot.children){
                        if (d.child("reading").value == false &&
                            d.child("user_id").value.toString() != currentUserid){
                            i++
                            item.isReading.visibility = View.GONE
                        }else if (d.child("user_id").value.toString() != currentUserid) {
                            item.isReading.visibility = View.GONE
                        }
                         else if (d.child("reading").value == false &&
                                d.child("user_id").value.toString() == currentUserid){
                                item.isReading.setBackgroundResource(R.drawable.ic_baseline_done_24)
                                item.isReading.visibility = View.VISIBLE
                            }else if(d.child("reading").value == true &&
                                d.child("user_id").value.toString() == currentUserid){
                                item.isReading.setBackgroundResource(R.drawable.ic_baseline_done_all_24)
                                item.isReading.visibility = View.VISIBLE
                            }
                        }

                    if (i>0){
                        item.isReadingLiner.visibility = View.VISIBLE
                        item.isReadingTxt.text = i.toString()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            FirebaseDatabase.getInstance().getReference("Chat").child(data.key.toString())
                .child("chat").limitToLast(1).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(d in snapshot.children) {
                        val privateChatData: PrivateChatData =
                            d.getValue(PrivateChatData::class.java)!!
                        if (privateChatData.type =="photo"){
                            item.message.text = "Rasm"
                        }else{
                            item.message.text = privateChatData.message
                        }
                        item.date.text = convertLongToTime(privateChatData.time.toLong())
                        break
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

        private suspend fun getUsernameFromId(id: String): String? {
            val usersRef = FirebaseDatabase.getInstance().getReference("users")
            val userRef = usersRef.child(id)

            //To'g'rimi ? ha
            return try {
                userRef.child("name").get().await().value as? String
                //Bu yerdan oldim otvordoi
            } catch (e: Exception) {
                null
            }
        }

         fun getOnlineFromId(id: String,img:ImageView) {
            val usersRef = FirebaseDatabase.getInstance().getReference("users")
            val userRef = usersRef.child(id)
            userRef.child("online").addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        if (snapshot.value as Boolean){
                            img.setBackgroundResource(R.drawable.online_radius)
                        }else{
                            img.setBackgroundResource(R.drawable.offline_radius)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
                //Bu yerdan oldim otvordoi

        }
    }

    interface Click{
        fun click(data: UserChatData){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = ChatItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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