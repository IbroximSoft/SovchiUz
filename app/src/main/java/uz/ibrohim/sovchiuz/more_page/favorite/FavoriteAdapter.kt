package uz.ibrohim.sovchiuz.more_page.favorite

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.home_page.adapters.AnketaItems
import uz.ibrohim.sovchiuz.read_data.InfoAllActivity

class FavoriteAdapter(private val userList: ArrayList<AnketaItems>) :
    RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.home_item,
            parent, false
        )
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = userList[position]
        holder.year.text = currentItem.year
        holder.province.text = currentItem.province
        holder.marriage.text = currentItem.marriage
        Glide.with(holder.itemView.context).load(currentItem.image).into(holder.image)
        val uid: String? = currentItem.uid
        val gender: String? = currentItem.gender

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, InfoAllActivity::class.java)
            intent.putExtra("uid", uid)
            intent.putExtra("gender", gender)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {

        return userList.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val year: TextView = itemView.findViewById(R.id.home_year)
        val province: TextView = itemView.findViewById(R.id.home_province)
        val marriage: TextView = itemView.findViewById(R.id.home_marriage)
        val image: ImageView = itemView.findViewById(R.id.home_img)

    }
}