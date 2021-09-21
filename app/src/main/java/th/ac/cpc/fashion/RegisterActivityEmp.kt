package th.ac.cpc.fashion

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import com.squareup.picasso.Picasso
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

class RegisterActivityEmp : AppCompatActivity() {

    var editTextFirstName: EditText? = null
    var editTextEmail: EditText? = null
    var editTextUsername: EditText? = null
    var editTextPassword: EditText? = null
    var radioMan: RadioButton? = null
    var radioWoman: RadioButton? = null
    var radio1: RadioButton? = null
    var radio2: RadioButton? = null
    var radio3: RadioButton? = null
    var radio4: RadioButton? = null
    var editTextAddress: EditText? = null
    var editTextMobilePhone: EditText? = null
    var editBank: EditText? = null
    var txtBank: TextView? = null
    var btnRegister: Button? = null

    var CheckUser= ArrayList<String>()

    var x: Int = 0

    val appPreference:String = "appPrefer"
    val IDPreference:String = "userTypePref"


    var spinnerBank: Spinner? = null
    private var Bank = java.util.ArrayList<AllBank>()
    var BankID:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_emp)

        //To run network operations on a main thread or as an synchronous task.
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        //find to widgets on a layout
        editTextFirstName = findViewById(R.id.editTextFirstName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)

        radioMan = findViewById(R.id.radioMan)
        radioWoman = findViewById(R.id.radioWoman)
        radio1 = findViewById(R.id.radio1)
        radio2 = findViewById(R.id.radio2)
        radio3 = findViewById(R.id.radio3)
        radio4 = findViewById(R.id.radio4)
        editTextAddress = findViewById(R.id.editTextAddress)
        editTextMobilePhone = findViewById(R.id.editTextMobilePhone)
        btnRegister = findViewById(R.id.btnRegisterOK)
        spinnerBank = findViewById(R.id.spinnerBank)
        txtBank = findViewById(R.id.txtBank)
        editBank = findViewById(R.id.editBank)


        Bank.add(AllBank("", "กรุณาเลือกบัญชีธนาคารของคุณ"))
        listBank()
        val adapterBand = ArrayAdapter(this,android.R.layout.simple_spinner_item, Bank)
        spinnerBank!!.adapter = adapterBand
        spinnerBank!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                val mat = spinnerBank!!.selectedItem as AllBank
                BankID = mat.bankID
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val btnCancel = findViewById<TextView>(R.id.btnCancel)
        btnCancel.setOnClickListener{
            val intent = Intent(applicationContext, LoginEmpActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnRegister!!.setOnClickListener {
            if (!radio1!!.isChecked && !radio2!!.isChecked && !radio3!!.isChecked && !radio4!!.isChecked){
                Toast.makeText(this, "ใส่ข้อมูลให้ครบ", Toast.LENGTH_LONG).show()
            }else{
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
                                if (!radioMan!!.isChecked && !radioWoman!!.isChecked){
                                    Toast.makeText(this, "กรุณาเลือกเพศ", Toast.LENGTH_LONG).show()
                                    }else{
                                        if (editTextAddress?.text.toString() == ""){
                                            Toast.makeText(this, "กรุณาใส่ที่อยู่", Toast.LENGTH_LONG).show()
                                        }else{
                                            if (editTextMobilePhone?.text.toString() == ""){
                                                Toast.makeText(this, "กรุณาใส่เบอร์โทร", Toast.LENGTH_LONG).show()
                                            }else{
                                                if(BankID.toString() == ""){
                                                    Toast.makeText(this, "กรุณาเลือกธนาคาร", Toast.LENGTH_LONG).show()
                                            }else{
                                                    if (editBank?.text.toString() == ""){
                                                        Toast.makeText(this, "กรุณาใส่เลขที่บัญชี", Toast.LENGTH_LONG).show()
                                                    }else{
                                                        CheckUsername()
                                                        for (i in 0 until CheckUser.size) {
                                                            if (editTextUsername?.text.toString() == CheckUser[i]) {
                                                                x += 1
                                                            }
                                                        }
                                                        if (x == 0) {
                                                            register()
                                                            val sharedPrefer: SharedPreferences =
                                                                    getSharedPreferences(appPreference, Context.MODE_PRIVATE)
                                                            val editor: SharedPreferences.Editor = sharedPrefer.edit()
                                                            editor.putString(IDPreference, SelectID().toString())
                                                            editor.commit()

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




            }
        }

        val callback = onBackPressedDispatcher.addCallback(this) {

            val intent = Intent(applicationContext, LoginEmpActivity::class.java)
            startActivity(intent)
        }
        callback.isEnabled


    }


    private fun listBank() {
        val urlProvince: String = getString(R.string.root_url) + getString(R.string.viewbank_url)
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url(urlProvince).get().build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {
                        for (i in 0 until res.length()) {
                            val item: JSONObject = res.getJSONObject(i)
                            Bank.add(
                                    AllBank(
                                            item.getString("bankID"),
                                            item.getString("bankName")
                                    )
                            )
                        }
                    }
                } catch (e: JSONException) { e.printStackTrace() }
            } else { response.code }
        } catch (e: IOException) { e.printStackTrace() }
    }

    internal class AllBank(var bankID: String, var bankName: String) {
        override fun toString(): String {
            return bankName
        }

    }

    private fun CheckUsername() {
        val url: String = getString(R.string.root_url) + getString(R.string.viewEmp_url)
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

                            CheckUser.add( item.getString("empUsername"))
                        }
                    } else {
                    }
                } catch (e: JSONException) { e.printStackTrace() }
            } else { response.code }
        } catch (e: IOException) { e.printStackTrace() }
    }

    private fun SelectID(): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.SelectIDEmployeesDESC_url)
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .get()////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {

                        id=data.getString("empID")

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
        return id
    }

    private fun register()
    {

        var url: String = getString(R.string.root_url) + getString(R.string.createEmp_url)
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = FormBody.Builder()

                .add("empName", editTextFirstName?.text.toString())
                .add("empUsername", editTextUsername?.text.toString())
                .add("empPassword", editTextPassword?.text.toString())
                .add("empGender", if (radioWoman!!.isChecked) "0" else "1")
                .add("empAddress", editTextAddress?.text.toString())
                .add("empEmail", editTextEmail?.text.toString())
                .add("empPhone", editTextMobilePhone?.text.toString())
                .add("bankCode", editBank?.text.toString())
                .add("bankID", BankID.toString())
                .add("empTypeID", if (radio1!!.isChecked) "1" else if (radio2!!.isChecked) "2" else if (radio3!!.isChecked) "3" else if (radio4!!.isChecked) "4" else "0")
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

                        val intent = Intent(applicationContext, MainActivityUpImage::class.java)
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