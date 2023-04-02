package uz.ibrohim.sovchiuz.message

import retrofit2.Call
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FcmService {
    @Headers ("Authorization: key=AAAArpB_3JY:APA91bFmVdmQD5AgAgEpeJwuWObaZC4JcH6WealuDCZVtyE6WVhBiSaKXvhd_TlkVg0YYivOMHPvZmEDmwHfQic9SCjbj-lhc1HL3cKJ6yZjJ19eeoqnEycRdLmKM8wkuY9l3TRpQM6T")
    @POST("fcm/send")
    fun sendNotification(@Body notification: NotificationModel): Call<ResponseBody>
}