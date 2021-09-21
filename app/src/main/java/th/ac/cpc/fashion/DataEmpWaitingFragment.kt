package th.ac.cpc.fashion

import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.DecimalFormat
import java.text.NumberFormat

class DataEmpWaitingFragment : Fragment() {
    var userID: String? = null

    var txtempName: TextView? = null
    var txtnameJob: TextView? = null
    var txtempGender: TextView? = null
    var txtempAddress: TextView? = null
    var txtempEmail: TextView? = null
    var txtempPhone: TextView? = null
    var btnAllow: Button? = null
    var btnCancel: Button? = null
    var imageView: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_data_emp_waiting, container, false)

        val bundle = this.arguments

        txtempName = root.findViewById(R.id.txtempName)
        txtnameJob = root.findViewById(R.id.txtnameJob)
        txtempGender = root.findViewById(R.id.txtempGender)
        txtempAddress = root.findViewById(R.id.txtempAddress)
        txtempEmail = root.findViewById(R.id.txtempEmail)
        txtempPhone = root.findViewById(R.id.txtempPhone)
        imageView = root.findViewById(R.id.imageView2)
        btnAllow = root.findViewById(R.id.btnAllow)
        btnAllow?.setOnClickListener {
            updateStatusAllow(bundle?.get("empID").toString())
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.nav_host_fragment,DataEmpFragment())
            fragmentTransaction.commit()
        }
        btnCancel = root.findViewById(R.id.btnCancel)
        btnCancel?.setOnClickListener {
            deletecart(bundle?.get("empID").toString())
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.nav_host_fragment,DataEmpFragment())
            fragmentTransaction.commit()
        }

        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val sharedPrefer = requireContext().getSharedPreferences(
            LoginActivity().appPreference, Context.MODE_PRIVATE)
        userID = sharedPrefer?.getString(LoginActivity().userIdPreference, 0.toString())


        viewEmp(bundle?.get("empID").toString())


        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {

            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.nav_host_fragment,DataEmpFragment())
            fragmentTransaction.commit()
        }
        callback.isEnabled

        return root
    }


    private fun viewEmp(empID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewOneEmp_url) + empID
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

                        txtempName?.text = data.getString("empName")
                        txtnameJob?.text = data.getString("nameJob")
                        txtempAddress?.text = data.getString("empAddress")
                        txtempPhone?.text = data.getString("empPhone")
                        txtempEmail?.text = data.getString("empEmail")
                        txtempGender?.text = if(data.getString("empGender") == "1") "เพศ:ชาย" else "เพศ:หญิง"


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

    private fun updateStatusAllow(empID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateStatusEmp_url) + empID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

            .addFormDataPart("empStatus", "2")

            .build()
        Log.d("mytest",url)
        val request: Request = Request.Builder()
            .url(url)
            .post(formBody) ///////////////////////////////////////
            .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {
                        Toast.makeText(requireContext(), "สำเร็จ", Toast.LENGTH_LONG).show()
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

    private fun deletecart(empID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.Empdelete_url) + empID
        val okHttpClient = OkHttpClient()

        val formBody: RequestBody = FormBody.Builder()

                .build()
        val request: Request = Request.Builder()
                .url(url)
                .delete(formBody) ///////////////////////////////////////
                .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {
                        Toast.makeText(requireContext(), "ไม่อนุมัติสำเร็จ", Toast.LENGTH_LONG).show()
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