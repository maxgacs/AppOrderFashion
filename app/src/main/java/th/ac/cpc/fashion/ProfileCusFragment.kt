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
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class ProfileCusFragment : Fragment() {
    var userID: String? = null
    var imageView: ImageView? = null
    var txtFirstName: TextView? = null
    var txtEmail: TextView? = null
    var txtBirthday: TextView? = null
    var txtMobilePhone: TextView? = null
    var txtaddress: TextView? = null
    var txtGender: TextView? = null
    var btnUpdate: Button? = null
    var txtStoreName: TextView? = null
    var txtpoint: TextView? = null
    var btnlogout: Button? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_profile_cus, container, false)

        //get shared preference
        val sharedPrefer = requireContext().getSharedPreferences(
            LoginActivity().appPreference, Context.MODE_PRIVATE)
        userID = sharedPrefer?.getString(LoginActivity().userIdPreference, 0.toString())

        imageView = root.findViewById(R.id.imageView2)
        txtFirstName = root.findViewById(R.id.txtFirstName2)
        txtStoreName = root.findViewById(R.id.txtStoreName)
        txtEmail = root.findViewById(R.id.txtEmail)
        // txtUsername = root.findViewById(R.id.txtUsername)
        // txtPassword = root.findViewById(R.id.txtPassword)
        txtaddress = root.findViewById(R.id.txtaddress)
        txtMobilePhone = root.findViewById(R.id.txtMobilePhone)
        btnUpdate = root.findViewById(R.id.btnUpdate)
        btnlogout= root.findViewById(R.id.btnlogout)
        viewUser(userID)


            if(txtEmail?.text == "Email"){
                val editor = sharedPrefer.edit()
                editor.clear() // ทำการลบข้อมูลทั้งหมดจาก preferences

                editor.commit() // ยืนยันการแก้ไข preferences

                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)

            }else {

                btnlogout?.setOnClickListener {
                    val editor = sharedPrefer.edit()
                    editor.clear() // ทำการลบข้อมูลทั้งหมดจาก preferences

                    editor.commit() // ยืนยันการแก้ไข preferences

                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                }

            }



        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {

            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.nav_host_fragment, HomeCusFragment())
            fragmentTransaction.commit()
        }
        callback.isEnabled

        return root
    }
    private fun viewUser(userID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewOneUser_url) + userID
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
                                data.getString("memberImage")

                        Picasso.get().load(imgUrl).into(imageView)
                        txtFirstName?.text = data.getString("memberName")
                        txtStoreName?.text = data.getString("memberStoreName")
                        txtEmail?.text = data.getString("memberEmail")
                        txtaddress?.text = data.getString("memberAddress")
                        txtMobilePhone?.text = data.getString("memberPhone")
                        txtGender?.text = if(data.getString("memberGender") == "1") "Man" else "Women"




                        btnUpdate!!.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putString("userID", userID)

                            val fm = EditProfileCusFragment()
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