package uz.ibrohim.sovchiuz.chat_page

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import uz.ibrohim.sovchiuz.databinding.ChatLayoutBinding
import java.text.SimpleDateFormat
import java.util.*

class PrivateChatAdapter : RecyclerView.Adapter<PrivateChatAdapter.Holder>() {

    var list: List<PrivateChatData> = ArrayList<PrivateChatData>()

    fun add(l: List<PrivateChatData>) {
        list = l
        notifyDataSetChanged()
    }

    class Holder(val item: ChatLayoutBinding) : RecyclerView.ViewHolder(item.root) {
        private lateinit var auth: FirebaseAuth

        fun with(data: PrivateChatData) {

            auth = FirebaseAuth.getInstance()
            val currentUserID = auth.currentUser?.uid.toString()

            if (currentUserID == data.user_id) {
                item.u1Layout.visibility = View.GONE
                item.u2Layout.visibility = View.VISIBLE
                if (data.type == "photo") {
                    item.u2Text.visibility = View.GONE
                    item.u2Photo.visibility = View.VISIBLE

                } else {
                    item.u2Text.visibility = View.VISIBLE
                    item.u2Photo.visibility = View.GONE
                    item.u2Text.text = data.message
                }

                item.u2Date.text = convertLongToTime(data.time.toLong())

            } else {
                item.u1Layout.visibility = View.VISIBLE
                item.u2Layout.visibility = View.GONE
                if (data.type == "photo") {
                    item.u1Text.visibility = View.GONE
                    item.u1Photo.visibility = View.VISIBLE
                } else {
                    item.u1Text.visibility = View.VISIBLE
                    item.u1Photo.visibility = View.GONE
                    item.u1Text.text = data.message
                }

                item.u1Date.text = convertLongToTime(data.time.toLong())
            }
            if (data.user_id != currentUserID) {
                if (!data.reading) {
                    val updates = HashMap<String, Any>()
                    updates["reading"] = true
                    FirebaseDatabase.getInstance().getReference("Chat")
                        .child(data.chat_key).child("chat").child(data.id).updateChildren(updates)
                }
            }
            item.u2Photo.setOnClickListener {
                val intent = Intent()
                intent.putExtra("img", data.image_url)
                intent.setClass(itemView.context, PhotoActivity::class.java) //Xaxaxa startActivt
                itemView.context.startActivity(intent)
            }

            item.u1Photo.setOnClickListener {
                val intent = Intent()
                intent.putExtra("img", data.image_url)
                intent.putExtra("user", true)
                intent.putExtra("id", data.id)
                intent.putExtra("room", data.chat_key)
                intent.setClass(itemView.context, PhotoActivity::class.java)
                itemView.context.startActivity(intent)
            }

        }

        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("HH:mm")
            return format.format(date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = ChatLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.with(list[position])

        holder.item.date.text = getDate(list[position].time)

        if (position > -1 && position == 0) {
            holder.item.date.visibility = View.VISIBLE
        } else {
            if (position > 0) {
                val old: String = getDate(list[position - 1].time)
                if (holder.item.date.text.toString() == old) {
                    holder.item.date.visibility = View.GONE
                } else {
                    holder.item.date.visibility = View.VISIBLE
                }
            } else {
                if (position == list.size) {
                    val old: String = getDate(list[position].time)
                    if (holder.item.date.text.toString() == old) {
                        holder.item.date.visibility = View.GONE
                    } else {
                        holder.item.date.visibility = View.VISIBLE
                    }
                }
            }
        }

    }

    fun getDate(time: String): String {
        val df = SimpleDateFormat("dd.MM.yyyy")
        return df.format(time.toLong())
    }

    override fun getItemCount(): Int {
        return list.size
    }
}