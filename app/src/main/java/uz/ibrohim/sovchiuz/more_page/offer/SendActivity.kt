package uz.ibrohim.sovchiuz.more_page.offer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import uz.ibrohim.sovchiuz.databinding.ActivitySendBinding
import uz.ibrohim.sovchiuz.ui.TaklifViewModel

class SendActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendBinding
    lateinit var viewModel: TaklifViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TaklifViewModel::class.java]
    }



}