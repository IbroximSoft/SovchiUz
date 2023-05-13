package uz.ibrohim.sovchiuz.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import uz.ibrohim.sovchiuz.ui.modul.TaklifData
import kotlin.collections.ArrayList

class TaklifViewModel : ViewModel() {

    private var takliflar : MutableLiveData<ArrayList<TaklifData>> = MutableLiveData()
    private var db = FirebaseDatabase.getInstance().getReference("Takliflar")

    fun getTaklif():MutableLiveData<ArrayList<TaklifData>>{
        return takliflar
    }



    fun sendTaklif(data: TaklifData){
       db.child(data.key.toString()).setValue(data)
    }

    fun readTaklif(uid: String){
        db.orderByChild("send_id").equalTo(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val l = ArrayList<TaklifData>()
                for(d in snapshot.children){
                   val data : TaklifData? = d.getValue(TaklifData::class.java)
                   l.add(data!!)
                }
                takliflar.postValue(l)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}