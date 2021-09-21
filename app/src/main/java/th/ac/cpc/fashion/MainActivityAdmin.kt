package th.ac.cpc.fashion

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_admin)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, HomeAdminFragment())
        transaction.commit()

        val callback = onBackPressedDispatcher.addCallback(this) {

            val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivityAdmin)
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
            var fm: Fragment = HomeAdminFragment()
            when (it.itemId) {
                R.id.nav_home -> fm = HomeAdminFragment()
                R.id.nav_emp -> fm = DataEmpFragment()
                R.id.nav_DataEmpSalary -> fm = DataEmpSalaryFragment()
                R.id.nav_profile -> fm = ProfileAdminFragment()
            }

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fm)
            transaction.commit()
            return@setOnNavigationItemSelectedListener true
        }
    }
}
