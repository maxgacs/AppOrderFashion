package th.ac.cpc.fashion

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class LoginEmpActivity : AppCompatActivity() {

    val appPreference:String = "appPrefer"
    val userIdPreference:String = "userIdPref"
    val usernamePreference:String = "usernamePref"
    val userTypePreference:String = "userTypePref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_emp)

        //To run network operations on a main thread or as an synchronous task.
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val callback = onBackPressedDispatcher.addCallback(this) {

            val intent = Intent(applicationContext, SelectMenu::class.java)
            startActivity(intent)
        }
        callback.isEnabled

        val btnCancle = findViewById<TextView>(R.id.btnCancel)
        btnCancle.setOnClickListener{
            val intent = Intent(applicationContext, SelectMenu::class.java)
            startActivity(intent)
            finish()
        }


        val btnRegisterEmp= findViewById<TextView>(R.id.btnRegisterEmp)
        btnRegisterEmp.setOnClickListener{
            val intent = Intent(applicationContext, RegisterActivityEmp::class.java)
            startActivity(intent)
            finish()
        }

        //Find to components on a layout
        val editUsername = findViewById<EditText>(R.id.editTextUsername)
        val editPassword = findViewById<EditText>(R.id.editTextPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        //Set action when a user clicks a login button
        btnLogin.setOnClickListener {

            val url = getString(R.string.root_url) + getString(R.string.Emplogin_url)
            val okHttpClient = OkHttpClient()

            val formBody: RequestBody = FormBody.Builder()
                    .add("empUsername", editUsername.text.toString())
                    .add("empPassword", editPassword.text.toString())
                    .build()
            val request: Request = Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build()
            try {
                val response = okHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    try {
                        val obj = JSONObject(response.body!!.string())
                        val empID = obj["empID"].toString()
                        val empUsername = obj["empUsername"].toString()
                        val empTypeID = obj["empTypeID"].toString()
                        val empStatus = obj["empStatus"].toString()


                        //Create shared preference to store user data
                        val sharedPrefer: SharedPreferences =
                                getSharedPreferences(appPreference, Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = sharedPrefer.edit()

                        editor.putString(userIdPreference, empID)
                        editor.putString(usernamePreference, empUsername)
                        editor.putString(userTypePreference, empTypeID)
                        editor.commit()

                        if(empStatus == "0"){
                            val intent = Intent(applicationContext, MainActivityUpImage::class.java)
                            startActivity(intent)
                            finish()
                        }else if (empStatus == "1"){
                            Toast.makeText(applicationContext, "?????????????????????????????????????????????????????????????????????", Toast.LENGTH_LONG).show()

                        }else if (empStatus == "2"){

                            if (empTypeID == "1")//0 = general users
                            {
                                val intent = Intent(applicationContext, MainActivityEmp::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else if (empTypeID == "2")//1 = admin
                            {
                                val intent = Intent(applicationContext, MainActivityEmp::class.java)
                                startActivity(intent)
                                finish()

                            }
                            else if (empTypeID == "3")//1 = admin
                            {
                                val intent = Intent(applicationContext, MainActivityEmp::class.java)
                                startActivity(intent)
                                finish()

                            }
                            else if (empTypeID == "4")//1 = admin
                            {
                                val intent = Intent(applicationContext, MainActivityEmp::class.java)
                                startActivity(intent)
                                finish()

                            }
                        }else if (empStatus == "3"){
                            Toast.makeText(applicationContext, "?????????????????????????????????????????????????????????????????????????????????", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(applicationContext, "????????????????????????????????????????????????", Toast.LENGTH_LONG).show()
                        }



                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(applicationContext, "username ???????????? password ?????????????????????.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    response.code
                    Toast.makeText(applicationContext, "username ???????????? password ?????????????????????.", Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onResume() {
        val sharedPrefer: SharedPreferences =
                getSharedPreferences(appPreference, Context.MODE_PRIVATE)
        val empTypeID = sharedPrefer?.getString(userTypePreference, null)

        if (empTypeID=="1") {
            val i = Intent(this, MainActivityEmp::class.java)
            startActivity(i)
            finish()
        }
        else if(empTypeID=="2")
        {
            val i = Intent(this, MainActivityEmp::class.java)
            startActivity(i)
            finish()
        }
        else if(empTypeID=="3")
        {
            val i = Intent(this, MainActivityEmp::class.java)
            startActivity(i)
            finish()
        }
        else if(empTypeID=="4")
        {
            val i = Intent(this, MainActivityEmp::class.java)
            startActivity(i)
            finish()
        }

        super.onResume()
    }
}