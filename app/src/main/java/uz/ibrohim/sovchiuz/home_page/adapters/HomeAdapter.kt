package uz.ibrohim.sovchiuz.home_page.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.read_data.InfoAllActivity

class HomeAdapter(
    private val booksList: MutableList<AnketaItems>
) :
    RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {
    private lateinit var auth: FirebaseAuth

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val eye: TextView = itemView.findViewById(R.id.home_eye)
        val image: ImageView = itemView.findViewById(R.id.home_img)
        val star: ImageView = itemView.findViewById(R.id.star)
        val year: TextView = itemView.findViewById(R.id.home_year)
        val province: TextView = itemView.findViewById(R.id.home_province)
        val marriage: TextView = itemView.findViewById(R.id.home_marriage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.home_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.year.text = booksList[position].year
        holder.province.text = booksList[position].province
        holder.marriage.text = booksList[position].marriage
        val uid: String? = booksList[position].uid
        val gender: String? = booksList[position].gender
        Glide.with(holder.itemView.context).load(booksList[position].image).into(holder.image)

        auth = FirebaseAuth.getInstance()
        val currentUserID = auth.currentUser?.uid.toString()
        if (uid == currentUserID){
            holder.star.visibility = View.VISIBLE
        }else{
            holder.star.visibility = View.GONE
        }

        //xa
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, InfoAllActivity::class.java)
            intent.putExtra("uid", uid)
            intent.putExtra("gender", gender)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return booksList.size
    }
}