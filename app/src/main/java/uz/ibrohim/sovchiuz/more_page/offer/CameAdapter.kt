package uz.ibrohim.sovchiuz.more_page.offer

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.ibrohim.sovchiuz.databinding.ItemCameLayoutBinding
import uz.ibrohim.sovchiuz.ui.modul.TaklifData

class CameAdapter(val list: ArrayList<TaklifData>,val c: click): RecyclerView.Adapter<CameAdapter.Holder>() {

    class Holder(val item: ItemCameLayoutBinding):RecyclerView.ViewHolder(item.root) {
        @SuppressLint("SetTextI18n")
        fun setup(data: TaklifData){
            item.ism.text = data.last_name
            item.yosh.text = data.yosh.toString() + " yosh"
            item.manzil.text = data.manzil
        }
    }


    interface click {
        fun onlistern(data: TaklifData, listern: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = ItemCameLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setup(list[position])
        holder.item.qabulQilish.setOnClickListener {
            c.onlistern(list[position],1)
        }

        holder.item.radEtish.setOnClickListener {
            c.onlistern(list[position],0)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}