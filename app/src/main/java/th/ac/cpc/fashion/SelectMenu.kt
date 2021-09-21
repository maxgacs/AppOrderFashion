package th.ac.cpc.fashion

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext

class SelectMenu : AppCompatActivity() {

    var buttonuser: Button? = null
    var buttonemp: Button? = null
    var userID: String? = null
    var empID: String? = null
    var maID: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_menu)

        //To run network operations on a main thread or as an synchronous task.
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        //find to widgets on a layout
        buttonuser = findViewById(R.id.buttonuser)
        buttonuser?.setOnClickListener{

            val intent = Intent(applicationContext, MainActivityCus::class.java)
            startActivity(intent)
            finish()

        }

        buttonemp = findViewById(R.id.buttonemp)
        buttonemp?.setOnClickListener{

                    val intent = Intent(applicationContext, SelectMenuEmpActivity::class.java)
                    startActivity(intent)
                    finish()
        }
        val callback = onBackPressedDispatcher.addCallback(this) {

            val builder: AlertDialog.Builder = AlertDialog.Builder(this@SelectMenu)
            builder.setMessage("ต้องการออกหรือไม่?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id -> finish() })
                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
            val alert: AlertDialog = builder.create()
            alert.show()
        }
        callback.isEnabled



    }


}