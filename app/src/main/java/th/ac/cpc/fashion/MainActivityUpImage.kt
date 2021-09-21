package th.ac.cpc.fashion

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import th.ac.cpc.fashion.UpImageFragment

class MainActivityUpImage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_up_image)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, UpImageFragment())
        transaction.commit()

        val callback = onBackPressedDispatcher.addCallback(this) {

            val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivityUpImage)
            builder.setMessage("ต้องการออกหรือไม่?")
                .setCancelable(false)
                .setNegativeButton("ไม่", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                .setPositiveButton("ใช่", DialogInterface.OnClickListener { dialog, id -> finish() })
            val alert: AlertDialog = builder.create()
            alert.show()
        }
        callback.isEnabled

    }
}
