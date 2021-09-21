package th.ac.cpc.fashion

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityEmp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_emp)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, HomeEmpFragment())
        transaction.commit()

        val callback = onBackPressedDispatcher.addCallback(this) {

            val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivityEmp)
            builder.setMessage("ต้องการออกหรือไม่?")
                .setCancelable(false)
                .setNegativeButton("ไม่", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                .setPositiveButton("ใช่", DialogInterface.OnClickListener { dialog, id -> finish() })
            val alert: AlertDialog = builder.create()
            alert.show()
        }
        callback.isEnabled

        //binding bottom menu and fragment
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener {
            var fm: Fragment = HomeEmpFragment()
            when (it.itemId) {
                R.id.nav_home -> fm = HomeEmpFragment()
                R.id.nav_Salary -> fm = SalaryEmpFragment()
                R.id.nav_profile -> fm = ProfileEmpFragment()
            }

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fm)
            transaction.commit()
            return@setOnNavigationItemSelectedListener true
        }
    }
}
