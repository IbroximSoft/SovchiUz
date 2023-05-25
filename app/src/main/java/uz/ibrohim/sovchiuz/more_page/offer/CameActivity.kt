package uz.ibrohim.sovchiuz.more_page.offer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.ibrohim.sovchiuz.databinding.ActivityCameBinding
import uz.ibrohim.sovchiuz.ui.modul.TaklifData

class CameActivity : AppCompatActivity(),CameAdapter.click {

    private lateinit var binding: ActivityCameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onlistern(data: TaklifData, listern: Int) {
        if (listern == 1){
            //qabul
        }else{
            //rad etildi
        }
    }
}