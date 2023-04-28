package uz.ibrohim.sovchiuz.chat_page

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.appuj.customizedalertdialoglib.CustomizedAlertDialog
import com.appuj.customizedalertdialoglib.CustomizedAlertDialogCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import es.dmoral.toasty.Toasty
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.ibrohim.sovchiuz.App
import uz.ibrohim.sovchiuz.R
import uz.ibrohim.sovchiuz.databinding.ActivityPrivateChatBinding
import uz.ibrohim.sovchiuz.message.FcmService
import uz.ibrohim.sovchiuz.message.NotificationDataModel
import uz.ibrohim.sovchiuz.message.NotificationModel
import uz.ibrohim.sovchiuz.message.UserChatData
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class PrivateChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityPrivateChatBinding
    val db = FirebaseDatabase.getInstance().getReference("Chat")
    val reference = FirebaseDatabase.getInstance().reference
    lateinit var list: ArrayList<PrivateChatData>
    lateinit var adapter: PrivateChatAdapter
    private lateinit var chat_key: String
    private var you_id: String = ""
    private var token: String? = null
    private lateinit var uid: String
    private lateinit var mAuth: FirebaseAuth
    var is_send = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivateChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        mAuth = FirebaseAuth.getInstance()
        uid = mAuth.currentUser?.uid.toString()

        chat_key = intent.getStringExtra("chat_key").toString()
        you_id = intent.getStringExtra("you_id").toString()
        App.shared.saveRoom(you_id)
        binding.sendImage.setOnClickListener {
            if (check()) {
                PickImages()
            }
        }

        reference.child("users").child(you_id)
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("name").value.toString()
                    val online = snapshot.child("online").value
                    supportActionBar?.title = ""
                    binding.toolName.text = name
                    if (online == true) {
                        binding.toolOnline.text = "Online"
                        binding.toolOnline.setTextColor(resources.getColor(android.R.color.holo_green_dark))
                    } else {
                        binding.toolOnline.text = "Offline"
                        binding.toolOnline.setTextColor(resources.getColor(android.R.color.holo_red_dark))
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        reference.child("Takliflar").child(you_id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val uidS = snapshot.child("accepted").value.toString()
                    if (uid == uidS) {

                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        binding.message.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isEmpty()) {
                    binding.sendBtn.visibility = View.GONE
                    binding.sendBtnOff.visibility = View.VISIBLE
                } else {
                    binding.sendBtnOff.visibility = View.GONE
                    binding.sendBtn.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        getToken()
        check_user()

        binding.sendBtn.setOnClickListener {
            if (chat_key == "null") {
                send1()
            } else {
                val key = db.push().push().key.toString()
                val data = PrivateChatData(
                    key,
                    binding.message.text.toString(),
                    uid,
                    System.currentTimeMillis().toString(),
                    "text",
                    "",
                    false,
                    chat_key
                )
                db.child(chat_key).child("chat").child(key).setValue(data)
                notfication()
                binding.message.setText("")
            }
            is_send = true
        }

        list = ArrayList()

        adapter = PrivateChatAdapter()

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        binding.rec.layoutManager = linearLayoutManager
        binding.rec.adapter = adapter

        if (chat_key == "null") {
            getChatKey()
        }
        loadData()
    }


    fun check_user() {
        db.child(chat_key)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child("status").value.toString() == "blok") {
                        finish()
                    }

                    Log.d("ttttttt", snapshot.child("status").value.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun send1() {
        val key = db.push().key.toString()
        val key2 = db.push().push().key.toString()
        val u = UserChatData(uid, you_id, "Aktiv", key)
        val data = PrivateChatData(
            key2,
            binding.message.text.toString(),
            uid,
            System.currentTimeMillis().toString(),
            "text", //image
            "", // download_url qilasiz bunga link beramanmi haligi notificationga yozilgan habarni qande bervorsek bo'ladi ?
            false,
            key
        )
        balanceDivorce()
        db.child(key).child("chat").child(key2).setValue(data)
        notfication()

        db.child(key).setValue(u).addOnSuccessListener {
            db.child(key).child("chat").child(key2).setValue(data)
            chat_key = key
            db.removeEventListener(read_chat_listern)
            loadData()
        }
        binding.message.setText("")
    }

    override fun onDestroy() {
        super.onDestroy()
        App.shared.saveRoom("")
    }

    //shunaqa ishlame qolyabdi
    override fun onStop() {
        super.onStop()
        App.shared.saveRoom("")
    }

    private fun balanceDivorce() {
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        reference.child("users").child(userID).child("balance")
            .runTransaction(object : Transaction.Handler {
                override fun doTransaction(currentData: MutableData): Transaction.Result {
                    val data = currentData.value.toString()
                    val incrementVal = java.lang.Long.valueOf(data) - 1
                    currentData.value = incrementVal.toString()
                    return Transaction.success(currentData)
                }

                override fun onComplete(
                    error: DatabaseError?,
                    committed: Boolean,
                    currentData: DataSnapshot?
                ) {
                }
            })
    }

    private fun getChatKey() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (d in snapshot.children) {
                    if ((d.child("uid1").value.toString() == uid || d.child("uid2").value == uid) and
                        (d.child("uid1").value.toString() == you_id || d.child("uid2").value == you_id)
                    ) {
                        chat_key = d.child("key").value.toString()
                        db.removeEventListener(read_chat_listern)
                        loadData()
                        check_user()
                        break
                    }
                }
                db.removeEventListener(this)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun loadData() {
        db.child(chat_key).child("chat").addValueEventListener(read_chat_listern)
    }

    val read_chat_listern = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            list.clear()
            for (d in snapshot.children) {
                val mainMenu: PrivateChatData? = d.getValue(PrivateChatData::class.java)
                list.add(mainMenu!!)
            }

            adapter.add(list)
            if (is_send) {
                binding.rec.smoothScrollToPosition(list.size - 1)
                is_send = false
            }
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    }

    private fun getToken() {
        FirebaseDatabase.getInstance().getReference("users").child(you_id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    token = snapshot.child("token").value.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun notfication() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(FcmService::class.java)

        val notification = NotificationModel(
            to = token.toString(),
            data = NotificationDataModel(
                key = chat_key, // Yozish shart emas
                title = App.shared.getIsm() + " Yangi xabar", // Sarlavha nomi shunga
                body = binding.message.text.toString(), // Qisqa matn Yozilgan xabar bo'lishi mumkin
                token = token.toString(),
                to_id = uid,
                click_action = "ChatActivity", // Bunlarga tegmang
                image = null // random rasm uchun link
            )
        )

        service.sendNotification(notification).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                // xabar muvaffaqiyatli yuborildi
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // xabarni yuborishda xatolik yuz berdi
                //Yuborgan fileni dasturga qoshing
            }
        })

    }

    lateinit var rasm: File

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            rasm = ImageCompress.compress(this, uriToFile(data.data!!, this))
            Log.d("dy_test", rasm.length().toString())
            val fileUri: String = rasm.path.toString()
            uploadImageToFirebaseStorage(fileUri)
        }
    }

    fun uriToFile(uri: Uri, context: Context): File? {
        val contentResolver = context.contentResolver
        val file = File(context.cacheDir, "temp")
        try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val outputStream = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while (inputStream.read(buf).also { len = it } > 0) {
                outputStream.write(buf, 0, len)
            }
            outputStream.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return file
    }


    private fun check(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 1
            )
            false
        } else {
            true
        }
    }

    private fun uploadImageToFirebaseStorage(fileUri: String) {
        val storageRef = FirebaseStorage.getInstance().reference
        val file: Uri = Uri.fromFile(File(fileUri))
        val imageRef = storageRef.child("images/" + file.lastPathSegment)
        val uploadTask = imageRef.putFile(file)
        uploadTask.addOnProgressListener { taskSnapshot ->
            val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
            Log.d("rasimyuklanmoqda", "Upload is $progress% done")
        }
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri -> // URL of the uploaded image
                    val imageUrl = uri.toString()
                    Log.d("rasimyuklanmoqda", "Image uploaded successfully. Image URL: $imageUrl")
                    if (chat_key == "null") {
                        sendImg1(imageUrl)
                    } else {
                        sendImg(imageUrl)
                    }
                }
            }
            .addOnFailureListener { e -> Log.e("rasimyuklanmoqda", "Image upload failed", e) }
    }


    private fun sendImg(imageUrl: String) {

        val key2 = db.push().push().key.toString()
        val data = PrivateChatData(
            key2,
            binding.message.text.toString(),
            uid,
            System.currentTimeMillis().toString(),
            "photo",
            imageUrl,
            false,
            chat_key
        )
        balanceDivorce()
        db.child(chat_key).child("chat").child(key2).setValue(data)
        notfication()


    }

    private fun sendImg1(imageUrl: String) {
        val key = db.push().key.toString()
        val key2 = db.push().push().key.toString()
        val u = UserChatData(uid, you_id, "Aktiv", key)
        val data = PrivateChatData(
            key2,
            binding.message.text.toString(),
            uid,
            System.currentTimeMillis().toString(),
            "photo",
            imageUrl,
            false,
            key
        )
        balanceDivorce()
        db.child(key).child("chat").child(key2).setValue(data)
        notfication()

        db.child(key).setValue(u).addOnSuccessListener {
            db.child(key).child("chat").child(key2).setValue(data)
            chat_key = key
            db.removeEventListener(read_chat_listern)
            loadData()
        }
    }

    private fun getTaklifs() {
        val dataHas = HashMap<String, String>()
        dataHas["accepted"] = you_id
        dataHas["sent"] = uid
        dataHas["status"] = "wait"
        reference.child("Takliflar").child(uid).setValue(dataHas)
            .addOnCompleteListener{
                Toasty.success(this, "Taklif yuborildi!",
                    Toasty.LENGTH_SHORT, true).show()
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                PickImages()
            }
        }
    }


    private fun PickImages() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 29)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_option, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.block -> {
                CustomizedAlertDialog.callAlertDialog(this, "Diqqat!",
                    "Siz ushbu nomzodni rostdan ham blo'k qilmoqchimisiz ?",
                    "Xa", "Yo'q", false,
                    object : CustomizedAlertDialogCallback<String> {
                        override fun alertDialogCallback(callback: String) {
                            if (callback == "1") {
                                db.child(chat_key).child("bloked_user").setValue(uid)
                                db.child(chat_key).child("chat").removeValue()
                                db.child(chat_key).child("status").setValue("blok")
                                finish()
                            }
                        }
                    })
                true
            }
            R.id.nikoh -> {
                CustomizedAlertDialog.callAlertDialog(this, "Diqqat!",
                    "Siz xa tugmasini bosgandan so'ng ushbu nomzodni tasdiqlashini kutasiz, tasdiqlansa anketalaringiz o'chadi",
                    "Xa", "Ortga", false,
                    object : CustomizedAlertDialogCallback<String> {
                        override fun alertDialogCallback(callback: String) {
                            if (callback == "1") {
                                getTaklif()
                            }
                        }
                    })
                true
            }
            else -> {
                Log.d("tttttt", "Emas")
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun getTaklif() {
        getTaklifs()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(FcmService::class.java)

        val notification = NotificationModel(
            to = token.toString(),
            data = NotificationDataModel(
                key = chat_key, // Yozish shart emas
                title = App.shared.getIsm() + " Yangi xabar", // Sarlavha nomi shunga
                body = "Taklif yuborildi", // Qisqa matn Yozilgan xabar bo'lishi mumkin
                token = token.toString(),
                to_id = uid,
                click_action = "ChatActivity", // Bunlarga tegmang
                image = null // random rasm uchun link
            )
        )

        service.sendNotification(notification).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                // xabar muvaffaqiyatli yuborildi
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // xabarni yuborishda xatolik yuz berdi
                //Yuborgan fileni dasturga qoshing
            }
        })
    }
}