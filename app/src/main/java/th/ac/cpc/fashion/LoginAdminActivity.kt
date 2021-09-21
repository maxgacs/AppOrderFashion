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

class LoginAdminActivity : AppCompatActivity() {

    val appPreference:String = "appPrefer"
    val userIdPreference:String = "userIdPref"
    val usernamePreference:String = "usernamePref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_admin)

        //To run network operations on a main thread or as an synchronous task.
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val callback = onBackPressedDispatcher.addCallback(this) {

            val intent = Intent(applicationContext, SelectMenuEmpActivity::class.java)
            startActivity(intent)
        }
        callback.isEnabled

        val btnCancle = findViewById<TextView>(R.id.btnCancel)
        btnCancle.setOnClickListener{
            val intent = Intent(applicationContext, SelectMenu::class.java)
            startActivity(intent)
            finish()
        }

        //Find to components on a layout
        val editUsername = findViewById<EditText>(R.id.editTextUsername)
        val editPassword = findViewById<EditText>(R.id.editTextPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        //Set action when a user clicks a login button
        btnLogin.setOnClickListener {

            val url = getString(R.string.root_url) + getString(R.string.Managerlogin_url)
            val okHttpClient = OkHttpClient()

            val formBody: RequestBody = FormBody.Builder()
                .add("username", editUsername.text.toString())
                .add("passwordApp", editPassword.text.toString())
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
                        val userID = obj["userID"].toString()
                        val username = obj["passwordApp"].toString()


                        //Create shared preference to store user data
                        val sharedPrefer: SharedPreferences =
                            getSharedPreferences(appPreference, Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = sharedPrefer.edit()


                        editor.putString(userIdPreference, userID)
                        editor.putString(usernamePreference, username)
                        editor.commit()

                        val intent = Intent(applicationContext, MainActivityAdmin::class.java)
                        startActivity(intent)
                        finish()

                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(applicationContext, "username หรือ password ผิดพลาด.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    response.code
                    Toast.makeText(applicationContext, "username หรือ password ผิดพลาด.", Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}