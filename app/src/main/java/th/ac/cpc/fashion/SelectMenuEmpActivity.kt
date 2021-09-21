package th.ac.cpc.fashion

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext

class SelectMenuEmpActivity : AppCompatActivity() {

    var buttonma: Button? = null
    var buttonemp: Button? = null
    var userID: String? = null
    var empTypeID: String? = null
    var maID: String? = null
    var empID: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_menu_emp)

        //To run network operations on a main thread or as an synchronous task.
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        //get shared preference
        val sharedPrefer = getSharedPreferences(
                LoginActivity().appPreference, Context.MODE_PRIVATE)
        userID = sharedPrefer?.getString(LoginActivity().userIdPreference, 0.toString())

        val sharedPrefer2 = getSharedPreferences(
                LoginEmpActivity().appPreference, Context.MODE_PRIVATE)
        empTypeID = sharedPrefer2?.getString(LoginEmpActivity().userTypePreference, 0.toString())

        val sharedPrefer3 = getSharedPreferences(
                LoginAdminActivity().appPreference, Context.MODE_PRIVATE)
        maID = sharedPrefer3?.getString(LoginAdminActivity().userIdPreference, 0.toString())

        val sharedPrefer4 = getSharedPreferences(
                LoginAdminActivity().appPreference, Context.MODE_PRIVATE)
        empID = sharedPrefer4?.getString(LoginAdminActivity().userIdPreference, 0.toString())

        //find to widgets on a layout
        buttonma = findViewById(R.id.buttonma)
        buttonma?.setOnClickListener{

            val intent = Intent(applicationContext, LoginAdminActivity::class.java)
            startActivity(intent)
            finish()

        }
        buttonemp = findViewById(R.id.buttonemp)
        buttonemp?.setOnClickListener{

            val intent = Intent(applicationContext, LoginEmpActivity::class.java)
            startActivity(intent)
            finish()
        }
        val callback = onBackPressedDispatcher.addCallback(this) {

            val intent = Intent(applicationContext, SelectMenu::class.java)
            startActivity(intent)
        }
        callback.isEnabled

    }
}