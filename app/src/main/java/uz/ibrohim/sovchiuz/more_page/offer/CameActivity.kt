package uz.ibrohim.sovchiuz.more_page.offer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.ibrohim.sovchiuz.databinding.ActivityCameBinding

class CameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}