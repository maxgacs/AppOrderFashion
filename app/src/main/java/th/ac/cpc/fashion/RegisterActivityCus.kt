package th.ac.cpc.fashion

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.*
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivityCus : AppCompatActivity() {

    var editTextFirstName: EditText? = null
    var editTextEmail: EditText? = null
    var editTextUsername: EditText? = null
    var editTextStoreName: EditText? = null
    var editTextPassword: EditText? = null
    var radioMan: RadioButton? = null
    var radioWoman: RadioButton? = null
    var editTextAddress: EditText? = null
    var editTextMobilePhone: EditText? = null
    var btnUpdate: Button? = null

    var CheckUser= ArrayList<String>()

    var x: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_cus)

        //To run network operations on a main thread or as an synchronous task.
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        //find to widgets on a layout
        editTextFirstName = findViewById(R.id.editTextFirstName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextStoreName = findViewById(R.id.editTextStoreName)
        radioMan = findViewById(R.id.radioMan)
        radioWoman = findViewById(R.id.radioWoman)
        editTextAddress = findViewById(R.id.editTextAddress)
        editTextMobilePhone = findViewById(R.id.editTextMobilePhone)
        btnUpdate = findViewById(R.id.btnRegisterOK)

        val btnCancel = findViewById<TextView>(R.id.btnCancel)
        btnCancel.setOnClickListener{
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }




        btnUpdate!!.setOnClickListener {

            if (editTextFirstName?.text.toString() == ""){
                Toast.makeText(this, "กรุณาใส่ชื่อ", Toast.LENGTH_LONG).show()
            }else{
                if (editTextEmail?.text.toString() == ""){
                    Toast.makeText(this, "กรุณาใส่อีเมล", Toast.LENGTH_LONG).show()
                }else{
                    if (editTextUsername?.text.toString() == ""){
                        Toast.makeText(this, "กรุณาใส่ username", Toast.LENGTH_LONG).show()
                    }else{
                        if (editTextPassword?.text.toString() == ""){
                            Toast.makeText(this, "กรุณาใส่รหัสผ่าน", Toast.LENGTH_LONG).show()
                        }else{
                            if (editTextStoreName?.text.toString() == ""){
                                Toast.makeText(this, "กรุณาใส่ชื่อร้าน", Toast.LENGTH_LONG).show()
                            }else{
                                if (!radioMan!!.isChecked && !radioWoman!!.isChecked){
                                    Toast.makeText(this, "กรุณาเลือกเพศ", Toast.LENGTH_LONG).show()
                                }else{
                                    if (editTextAddress?.text.toString() == ""){
                                        Toast.makeText(this, "กรุณาใส่ที่อยู่", Toast.LENGTH_LONG).show()
                                    }else{
                                        if (editTextMobilePhone?.text.toString() == ""){
                                            Toast.makeText(this, "กรุณาใส่เบอร์โทร", Toast.LENGTH_LONG).show()
                                        }else{
                                            CheckUsername()
                                            for (i in 0 until CheckUser.size) {
                                                if (editTextUsername?.text.toString() == CheckUser[i]) {
                                                    x += 1
                                                }
                                            }
                                            if (x == 0) {
                                                register()
                                            }else{
                                                Toast.makeText(this, "username ซ้ำ", Toast.LENGTH_LONG).show()
                                                x = 0
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }


        }
        val callback = onBackPressedDispatcher.addCallback(this) {

            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
        callback.isEnabled


    }

    private fun CheckUsername() {
        val url: String = getString(R.string.root_url) + getString(R.string.viewUsers_url)
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url(url).get().build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {
                        for (i in 0 until res.length()) {
                            val item: JSONObject = res.getJSONObject(i)

                            CheckUser.add( item.getString("memberUsername"))
                        }
                    } else {
                    }
                } catch (e: JSONException) { e.printStackTrace() }
            } else { response.code }
        } catch (e: IOException) { e.printStackTrace() }
    }

    private fun register()
    {

        var url: String = getString(R.string.root_url) + getString(R.string.createUsers_url)
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = FormBody.Builder()

                .add("memberName", editTextFirstName?.text.toString())
                .add("memberEmail", editTextEmail?.text.toString())
                .add("memberUsername", editTextUsername?.text.toString())
                .add("memberPassword", editTextPassword?.text.toString())
                .add("memberStoreName", editTextStoreName?.text.toString())
                .add("memberGender", if (radioWoman!!.isChecked) "0" else "1")
                .add("memberAddress", editTextAddress?.text.toString())
                .add("memberPhone", editTextMobilePhone?.text.toString())
                .build()
        Log.d("txt","x2")
        val request: Request = Request.Builder()
                .url(url)
                .post(formBody)
                .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {
                        Log.d("txt","x3")
                        Toast.makeText(this, "Register Success", Toast.LENGTH_LONG).show()
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                response.code
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}