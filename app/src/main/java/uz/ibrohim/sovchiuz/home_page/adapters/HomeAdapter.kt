package uz.ibrohim.sovchiuz.home_page.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.read_data.InfoAllActivity

class HomeAdapter(
    private val booksList: MutableList<AnketaItems>
) :
    RecyclerView.Adapter<HomeAdapter.MyViewHolder>(), Filterable {
    private lateinit var auth: FirebaseAuth

    private var filteredList: MutableList<AnketaItems> = booksList

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
        holder.year.text = filteredList[position].year
        holder.province.text = filteredList[position].province
        holder.marriage.text = filteredList[position].marriage
        val uid: String? = filteredList[position].uid
        val gender: String? = filteredList[position].gender
        Glide.with(holder.itemView.context).load(filteredList[position].image).into(holder.image)

        auth = FirebaseAuth.getInstance()
        val currentUserID = auth.currentUser?.uid.toString()
        if (uid == currentUserID){
            holder.star.visibility = View.VISIBLE
        }else{
            holder.star.visibility = View.GONE
        }

        //xa  boldi aka yana nima muammo bolyapdi haligi o'qimasa ham true bo'lishi
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, InfoAllActivity::class.java)
            intent.putExtra("uid", uid)
            intent.putExtra("gender", gender)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<AnketaItems>()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(booksList)
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim()
                    for (item in booksList) {
                        if (item.province!!.toLowerCase().contains(filterPattern) ||
                            item.year!!.toLowerCase().contains(filterPattern) ||
                            item.marriage!!.toLowerCase().contains(filterPattern)) {
                            Log.d("dy_test",item.uid.toString())
                            Log.d("dy_test",item.province.toString())
                            Log.d("dy_test",filterPattern)
                            filteredList.add(item)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as MutableList<AnketaItems>
                Log.d("dy_test",filteredList.size.toString())
                notifyDataSetChanged()
            }
        }
    }


}