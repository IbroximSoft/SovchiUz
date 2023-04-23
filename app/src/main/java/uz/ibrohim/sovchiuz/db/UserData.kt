package uz.ibrohim.sovchiuz.db

import android.content.Context

class UserData(context: Context) {

    val sharedPreferences =context.getSharedPreferences("user_status",Context.MODE_PRIVATE)

    fun save(ism:String){
        sharedPreferences.edit().putString("name",ism).apply()
    }

    fun getIsm():String{
        return sharedPreferences.getString("name","").toString()
    }

    fun saveRoom(id:String){
        sharedPreferences.edit().putString("room_id",id).apply()
    }

    fun getRoom():String{
        return sharedPreferences.getString("room_id","").toString()
    }

}