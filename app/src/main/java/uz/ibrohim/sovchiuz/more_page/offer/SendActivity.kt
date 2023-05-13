package uz.ibrohim.sovchiuz.more_page.offer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.ibrohim.sovchiuz.databinding.ActivitySendBinding

class SendActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}