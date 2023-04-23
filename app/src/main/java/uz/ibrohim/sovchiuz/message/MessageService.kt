package uz.ibrohim.sovchiuz.message

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import uz.ibrohim.sovchiuz.App
import uz.ibrohim.sovchiuz.R
import java.util.*

class MessageService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        sendMessage(message)
    }

    private fun sendMessage(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        val title = data["title"]
        val content = data["body"]
        val id = data["key"]
        val you_id = data["to_id"]
        if (you_id != App.shared.getRoom()) {
        Log.d("Xabar", title!!)
        val intent = Intent(remoteMessage.data["click_action"])
        intent.putExtra("chat_key", id)
        intent.putExtra("you_id",you_id)
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntentWithParentStack(intent)
        val pendingIntent: PendingIntent? = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, "SovchiUz")
                .setSmallIcon(R.drawable.ic_baseline_send_24) // app icon
                .setContentText(content)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setLights(Color.GREEN, 3000, 3000)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel("xabarlar", "Sovchi Uz", importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(false)
            notificationChannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationBuilder.setChannelId("xabarlar")
            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.notify(0, notificationBuilder.build())
        }
            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.notify(0, notificationBuilder.build())
        }
    }
}