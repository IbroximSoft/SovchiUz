package uz.ibrohim.sovchiuz.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.ibrohim.sovchiuz.App
import uz.ibrohim.sovchiuz.message.FcmService
import uz.ibrohim.sovchiuz.message.NotificationDataModel
import uz.ibrohim.sovchiuz.message.NotificationModel
import uz.ibrohim.sovchiuz.ui.modul.TaklifData
import kotlin.collections.ArrayList

class TaklifViewModel : ViewModel() {

    private var takliflar: MutableLiveData<ArrayList<TaklifData>> = MutableLiveData()
    private var db = FirebaseDatabase.getInstance().getReference("Takliflar")

    fun getTaklif(): MutableLiveData<ArrayList<TaklifData>> {
        return takliflar
    }


    fun sendTaklif(data: TaklifData) {
        db.child(data.key.toString()).setValue(data)
    }

    fun readTaklif(uid: String) {
        db.orderByChild("send_id").equalTo(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val l = ArrayList<TaklifData>()
                for (d in snapshot.children) {
                    val data: TaklifData? = d.getValue(TaklifData::class.java)
                    l.add(data!!)
                }
                takliflar.postValue(l)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    fun notfication(token:String, uid: String,chat_key:String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(FcmService::class.java)

        val notification = NotificationModel(
            to = token.toString(),
            data = NotificationDataModel(
                key = chat_key, // Yozish shart emas
                title = App.shared.getIsm() + " Yangi xabar", // Sarlavha nomi shunga
                body = "Assalomu Alleykum sizni taklifingizni qabul qildim", // Qisqa matn Yozilgan xabar bo'lishi mumkin
                token = token.toString(),
                to_id = uid,
                click_action = "ChatActivity", // Bunlarga tegmang
                image = null // random rasm uchun link
            )
        )

        service.sendNotification(notification).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                // xabar muvaffaqiyatli yuborildi
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // xabarni yuborishda xatolik yuz berdi
                //Yuborgan fileni dasturga qoshing
            }
        })
    }

}