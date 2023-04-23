package uz.ibrohim.sovchiuz

import android.app.Application
import uz.ibrohim.sovchiuz.db.UserData

class App: Application() {

    companion object{
        lateinit var shared: UserData
    }

    override fun onCreate() {
        super.onCreate()
        shared = UserData(this)
    }

    //Mana shu yerga Bloklani soni hisoblab turadigan fun yasash kerak  n>=5  bo'ldi spam uriladi

}
