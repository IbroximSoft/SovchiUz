package uz.ibrohim.sovchiuz.db

import android.content.Context
import android.content.SharedPreferences


class UserData(context: Context) {

    val sharedPreferences = context.getSharedPreferences("user_status", Context.MODE_PRIVATE)

    fun save(ism: String) {
        sharedPreferences.edit().putString("name", ism).apply()
    }

    fun deleteDatas(context: Context) {
        val preferences: SharedPreferences =
            context.getSharedPreferences("user_status", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.clear()
        editor.apply()
    }

    fun getIsm(): String {
        return sharedPreferences.getString("name", "").toString()
    }

    fun saveRoom(id: String) {
        sharedPreferences.edit().putString("room_id", id).apply()
    }

    fun getRoom(): String {
        return sharedPreferences.getString("room_id", "").toString()
    }

}