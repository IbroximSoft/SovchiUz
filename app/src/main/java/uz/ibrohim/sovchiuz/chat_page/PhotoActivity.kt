package uz.ibrohim.sovchiuz.chat_page

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.database.FirebaseDatabase
import io.github.krtkush.lineartimer.LinearTimer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.ibrohim.sovchiuz.databinding.ActivityPhotoBinding

class PhotoActivity : AppCompatActivity() {

    lateinit var binding: ActivityPhotoBinding
    private var thisUser: Boolean = false
    private var messageId: String = ""
    private var room: String = ""
    private var counts = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val photoUrl = intent.getStringExtra("img")
        thisUser = intent.getBooleanExtra("user", false)

        val linearTimer = LinearTimer.Builder()
            .linearTimerView(binding.linearTimer)
            .duration((10 * 1750).toLong())
            .build()

        if (thisUser) {
            messageId = intent.getStringExtra("id")!!.toString()
            room = intent.getStringExtra("room")!!.toString()
        }

        Glide.with(this)
            .load(photoUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progress.visibility = View.GONE
                    binding.txtProgress.visibility = View.GONE
                    binding.image.visibility = View.VISIBLE
                    binding.linearTimer.visibility = View.VISIBLE
                    start()
                    linearTimer.startTimer()
                    return false
                }
            })
            .into(binding.image)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun start() {
        GlobalScope.launch {

            while (counts >= 0) {
                delay(1500)
                counts--
            }

            if (thisUser) {
                FirebaseDatabase.getInstance().getReference("Chat").child(room).child("chat")
                    .child(messageId).removeValue()
            }
            finish()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            // allow screenshots when activity is focused
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        } else {
            // hide information (blank view) on app switcher
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }
    }
}