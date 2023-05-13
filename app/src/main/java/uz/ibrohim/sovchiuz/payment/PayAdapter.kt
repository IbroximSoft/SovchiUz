package uz.ibrohim.sovchiuz.payment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.ibrohim.sovchiuz.R

class PayAdapter(private val userList: ArrayList<PayItem>) :
    RecyclerView.Adapter<PayAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.pay_item,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.description.text = currentItem.description
        holder.title.text = currentItem.title
        holder.price.text = currentItem.price
        val key: String? = currentItem.key

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, PaymentActivity::class.java)
            intent.putExtra("key", key)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val description: TextView = itemView.findViewById(R.id.pay_description)
        val title: TextView = itemView.findViewById(R.id.pay_title)
        val price: TextView = itemView.findViewById(R.id.pay_price)
    }
}