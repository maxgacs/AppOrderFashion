package th.ac.cpc.fashion

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class ProfileEmpFragment : Fragment() {
    var userID: String? = null
    var imageView: ImageView? = null
    var txtFirstName: TextView? = null
    var txtEmail: TextView? = null
    var txtMobilePhone: TextView? = null
    var txtaddress: TextView? = null
    var txtGender: TextView? = null
    var btnUpdate: Button? = null
    var txtjob: TextView? = null
    var btnlogout: Button? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_profile_emp, container, false)

        //get shared preference
        val sharedPrefer = requireContext().getSharedPreferences(
            LoginEmpActivity().appPreference, Context.MODE_PRIVATE)
        userID = sharedPrefer?.getString(LoginEmpActivity().userIdPreference, 0.toString())

        if(userID == "0"){
            val editor = sharedPrefer.edit()
            editor.clear() // ทำการลบข้อมูลทั้งหมดจาก preferences

            editor.commit() // ยืนยันการแก้ไข preferences

            val intent = Intent(context, LoginEmpActivity::class.java)
            startActivity(intent)
        }else {

            //find to widgets on a layout
            imageView = root.findViewById(R.id.imageView2)
            txtFirstName = root.findViewById(R.id.txtFirstName2)
            txtEmail = root.findViewById(R.id.txtEmail)
            // txtUsername = root.findViewById(R.id.txtUsername)
            // txtPassword = root.findViewById(R.id.txtPassword)
            txtaddress = root.findViewById(R.id.txtaddress)
            txtMobilePhone = root.findViewById(R.id.txtMobilePhone)
            txtjob = root.findViewById(R.id.txtjob)
            btnUpdate = root.findViewById(R.id.btnUpdate)
            btnlogout= root.findViewById(R.id.btnlogout)
            btnlogout?.setOnClickListener {
                val editor = sharedPrefer.edit()
                editor.clear() // ทำการลบข้อมูลทั้งหมดจาก preferences

                editor.commit() // ยืนยันการแก้ไข preferences

                val intent = Intent(context, LoginEmpActivity::class.java)
                startActivity(intent)
            }
            viewUser(userID)
        }
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {

            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.nav_host_fragment, HomeEmpFragment())
            fragmentTransaction.commit()
        }
        callback.isEnabled

        return root
    }
    private fun viewUser(userID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewOneEmp_url) + userID
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .get()
            .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {

                        var imgUrl = getString(R.string.root_url) +
                                getString(R.string.user_image_url) +
                                data.getString("empImage")

                        Picasso.get().load(imgUrl).into(imageView)
                        txtFirstName?.text = data.getString("empName")
                        txtEmail?.text = data.getString("empEmail")
                        txtaddress?.text = data.getString("empAddress")
                        txtMobilePhone?.text = data.getString("empPhone")
                        txtjob?.text = data.getString("nameEmpType")
                        txtGender?.text = if(data.getString("empGender") == "1") "Man" else "Women"




                        btnUpdate!!.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putString("userID", userID)

                            val fm = EditProfileEmpFragment()
                            fm.arguments = bundle;

                            val fragmentTransaction = requireActivity().
                            supportFragmentManager.beginTransaction()
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.replace(R.id.nav_host_fragment, fm)
                            fragmentTransaction.commit()
                        }

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