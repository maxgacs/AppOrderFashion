package th.ac.cpc.fashion


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class StepEmpOrderFragment : Fragment() {
    var memberID: String? = null

    var addpriceTotal= ArrayList<String>()
    var addpriceSizeF = ArrayList<String>()
    var addpriceSizeS = ArrayList<String>()
    var addpriceSizeM = ArrayList<String>()
    var addpriceSizeL = ArrayList<String>()
    var addpriceSizeXL = ArrayList<String>()
    var addpriceSize2XL = ArrayList<String>()
    var addpriceSize3XL = ArrayList<String>()
    var addquanTotal = ArrayList<String>()
    var addquanSizeF = ArrayList<String>()
    var addquanSizeS = ArrayList<String>()
    var addquanSizeM = ArrayList<String>()
    var addquanSizeL = ArrayList<String>()
    var addquanSizeXL = ArrayList<String>()
    var addquanSize2XL = ArrayList<String>()
    var addquanSize3XL = ArrayList<String>()
    var addproductID = ArrayList<String>()
    var addmaterialID = ArrayList<String>()
    var addCartID = ArrayList<String>()

    var detsizeRate = ArrayList<String>()
    var detmaterialID = ArrayList<String>()

    var detquanRate = ArrayList<String>()
    var detmaterial_SpID = ArrayList<String>()

    var recyclerView4: RecyclerView? = null
    var txtmemberStoreName: TextView? = null
    var txtmemberAddress: TextView? = null
    var txtmemberPhone: TextView? = null
    var txtNameCus: TextView? = null
    var txtDate: TextView? = null
    var txtDataBut: TextView? = null
    var btnComfirm: Button? = null
    var btnCancel: Button? = null
    var txtCancel: EditText? = null
    var txtDateline: TextView? = null

    var userID: String? = null
    var empTypeID: String? = null
    var quanUsed: Int = 0
    var patternRate: Int = 0
    var NullempID: String = "0"
    var EmpID: String = "0"

    var NullempID1: String = "0"
    var EmpID1: String = "0"
    var NullempID2: String = "0"
    var EmpID2: String = "0"
    var NullempID3: String = "0"
    var EmpID3: String = "0"
    var NullempID4: String = "0"
    var EmpID4: String = "0"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_step_emp_order, container, false)

        val bundle = this.arguments

        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val sharedPrefer = requireContext().getSharedPreferences(
                LoginEmpActivity().appPreference, Context.MODE_PRIVATE)
        userID = sharedPrefer?.getString(LoginEmpActivity().userIdPreference, 0.toString())


        recyclerView4 = root.findViewById(R.id.recyclerView4)
        txtmemberStoreName = root.findViewById(R.id.txtmemberStoreName)
        txtmemberAddress = root.findViewById(R.id.txtmemberAddress)
        txtmemberPhone = root.findViewById(R.id.txtmemberPhone)
        txtNameCus = root.findViewById(R.id.txtNameCus)
        txtDate = root.findViewById(R.id.txtDate)
        txtDataBut = root.findViewById(R.id.txtDataBut)
        txtDateline = root.findViewById(R.id.txtDateline)


        viewOrderID(bundle?.get("orderID").toString())
        viewUser()
        viewEmpType()

        txtDate?.text = ViewCreated(bundle?.get("orderID").toString())
        txtDataBut?.text = ViewCreated(bundle?.get("orderID").toString())

        if(empTypeID == "1"){

            showDataListMat(bundle?.get("orderID").toString())
            txtDateline?.text = ViewDateline(bundle?.get("orderID").toString())

        }else if(empTypeID == "2"){

            showDataListMat(bundle?.get("orderID").toString())
            txtDateline?.text = ViewDateline(bundle?.get("orderID").toString())

        }else if(empTypeID == "3"){

            viewONEOrderUserProductIDJobEmp3(bundle?.get("salaryID").toString())
            txtDateline?.text = viewOrderIDDatelineS3(bundle?.get("salaryID").toString())

        }else if(empTypeID == "4"){

            showDataListMat(bundle?.get("orderID").toString())
            txtDateline?.text = ViewDateline(bundle?.get("orderID").toString())

        }


        btnComfirm = root.findViewById(R.id.btnComfirm)
        btnComfirm?.setOnClickListener{
            if(empTypeID == "1"){
                updateSalaryStat1(bundle?.get("salaryID").toString())
                updateOrderComfirm1(bundle?.get("orderID").toString())

                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.nav_host_fragment,HomeEmpFragment())
                fragmentTransaction.commit()
            }else if (empTypeID == "2"){
                updateSalaryStat1(bundle?.get("salaryID").toString())
                updateOrderComfirm2(bundle?.get("orderID").toString())

                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.nav_host_fragment,HomeEmpFragment())
                fragmentTransaction.commit()
            }else if (empTypeID == "3"){
                updateSalaryStat1(bundle?.get("salaryID").toString())
                updateOrderComfirm3(bundle?.get("orderID").toString())

                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.nav_host_fragment,HomeEmpFragment())
                fragmentTransaction.commit()
            }else if (empTypeID == "4"){
                updateSalaryStat1(bundle?.get("salaryID").toString())
                updateOrderComfirm4(bundle?.get("orderID").toString())

                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.nav_host_fragment,HomeEmpFragment())
                fragmentTransaction.commit()
            }

        }

        btnCancel = root.findViewById(R.id.btnCancel)
        btnCancel?.setOnClickListener{

            if(empTypeID == "1"){
                SelectEmployeesTypeID1Updated_atASC()
                if(NullempID1 == "0"){
                    updateEmpStat1()
                    updateSalary(bundle?.get("salaryID").toString(),EmpID1)
                    updateEmpStat0()
                }else{

                    updateSalary(bundle?.get("salaryID").toString(),"0")
                    updateOrderComfirmLogin1(bundle?.get("orderID").toString())
                    updateEmpStat0()
                }

            }else if (empTypeID == "2"){
                SelectEmployeesTypeID2Updated_atASC()
                if(NullempID2 == "0"){
                    updateEmpStat2()
                    updateSalary(bundle?.get("salaryID").toString(),EmpID2)

                    updateEmpStat0()
                }else{

                    updateSalary(bundle?.get("salaryID").toString(),"0")
                    updateOrderComfirmLogin2(bundle?.get("orderID").toString())
                    updateEmpStat0()
                }
            }else if (empTypeID == "3"){
                SelectEmployeesTypeID3Updated_atASC()
                if(NullempID3 == "0"){
                    updateEmpStat3()
                    updateSalary(bundle?.get("salaryID").toString(),EmpID3)
                    updateEmpStat0()
                }else{
                    updateOrderComfirmLogin3(bundle?.get("orderID").toString())
                    updateSalary(bundle?.get("salaryID").toString(),"0")
                    updateEmpStat0()
                }
            }else if (empTypeID == "4"){
                SelectEmployeesTypeID4Updated_atASC()
                if(NullempID4 == "0"){

                    updateSalary(bundle?.get("salaryID").toString(),EmpID4)
                    updateEmpStat4()
                    updateEmpStat0()
                }else{

                    updateSalary(bundle?.get("salaryID").toString(),"0")
                    updateOrderComfirmLogin4(bundle?.get("orderID").toString())
                    updateEmpStat0()
                }
            }

            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.nav_host_fragment,HomeEmpFragment())
            fragmentTransaction.commit()
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {

            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.nav_host_fragment,HomeEmpFragment())
            fragmentTransaction.commit()
        }
        callback.isEnabled

        return root
    }

    private fun updateEmpStat4()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateEmpStat_url) + EmpID4
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("empStat", "1")
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
    private fun updateEmpStat3()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateEmpStat_url) + EmpID3
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("empStat", "1")
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
    private fun updateEmpStat2()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateEmpStat_url) + EmpID2
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("empStat", "1")
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
    private fun updateEmpStat1()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateEmpStat_url) + EmpID1
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("empStat", "1")
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
    private fun updateOrderComfirmLogin4(orderID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderComfirm_url) + orderID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("stepStatusID", "10")
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

    private fun updateOrderComfirmLogin3(orderID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderComfirm_url) + orderID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("stepStatusID", "7")
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

    private fun updateOrderComfirmLogin2(orderID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderComfirm_url) + orderID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("stepStatusID", "4")
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

    private fun updateOrderComfirmLogin1(orderID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderComfirm_url) + orderID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("stepStatusID", "1")
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
    private fun updateSalary(salaryID: String?,EmpID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateSalaryWaiting_url) + salaryID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("statusOrder", "0")
                .addFormDataPart("empID", EmpID.toString())
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


    private fun SelectEmployeesTypeID4Updated_atASC()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.SelectEmployeesTypeID4Updated_atASC_url)
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
                .url(url)
                .get()////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                .build()
        Log.d("mytest1", url)
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {

                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {

                        val data: JSONObject = res.getJSONObject(0)
                        EmpID4 = data.getString("empID")

                    } else {
                        NullempID4 = ""
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

    private fun SelectEmployeesTypeID3Updated_atASC()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.SelectEmployeesTypeID3Updated_atASC_url)
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
                .url(url)
                .get()////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                .build()
        Log.d("mytest1", url)
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {

                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {

                        val data: JSONObject = res.getJSONObject(0)
                        EmpID3 = data.getString("empID")

                    } else {
                        NullempID3 = ""
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

    private fun SelectEmployeesTypeID2Updated_atASC()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.SelectEmployeesTypeID2Updated_atASC_url)
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
                .url(url)
                .get()////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                .build()
        Log.d("mytest1", url)
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {

                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {

                        val data: JSONObject = res.getJSONObject(0)
                        EmpID2 = data.getString("empID")

                    } else {
                        NullempID2 = ""
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

    private fun SelectEmployeesTypeID1Updated_atASC()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.SelectEmployeesTypeID1Updated_atASC_url)
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
                .url(url)
                .get()////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                .build()
        Log.d("mytest1", url)
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {

                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {

                        val data: JSONObject = res.getJSONObject(0)
                        EmpID1 = data.getString("empID")

                    } else {
                        NullempID1 = ""
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

    private fun updateEmpStat0()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateEmpStat_url) + userID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("empStat", "0")
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

    private fun viewEmpType()
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

                        empTypeID = data.getString("empTypeID")



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

    private fun updateOrderComfirm4(orderID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderComfirm_url) + orderID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("stepStatusID", "12")
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


    private fun updateOrderComfirm3(orderID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderComfirm_url) + orderID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("stepStatusID", "9")
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


    private fun updateOrderComfirm2(orderID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderComfirm_url) + orderID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("stepStatusID", "6")
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




    private fun updateSalaryStat1(salaryID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateSalaryStatStartDate_url) + salaryID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("statusOrder", "1")
                .addFormDataPart("endDate", txtDateline?.text.toString())
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

    private fun updateOrderComfirm1(orderID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderComfirm_url) + orderID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("stepStatusID", "3")
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


    private fun ViewDateline(orderID: String?): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.ViewSUM1_4Dateline_url) + "1/" + orderID
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
                .url(url)
                .get()////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                .build()
        Log.d("mytest1", url)
        try {
            val response = okHttpClient.newCall(request).execute()

            if (response.isSuccessful) {
                try {
                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {

                        id = data.getString("dateline")
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
        return id
    }

    private fun DatelineS3(orderID: String?): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.ViewSUM3Dateline_url) + orderID
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
                .url(url)
                .get()////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                .build()
        Log.d("mytest1", url)
        try {
            val response = okHttpClient.newCall(request).execute()

            if (response.isSuccessful) {
                try {
                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {

                        id = data.getString("DatelineS3")
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
        return id
    }
    private fun viewOrderIDDatelineS3(salaryID: String?): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.ViewSUM3Dateline2_url) + salaryID
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
                .url(url)
                .get()////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                .build()
        Log.d("mytest1", url)
        try {
            val response = okHttpClient.newCall(request).execute()

            if (response.isSuccessful) {
                try {
                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {

                        id = data.getString("dateline")
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
        return id
    }


    private fun ViewCreated(orderID: String?): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.ViewOrderCreated_at_url) + orderID
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
                .url(url)
                .get()////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                .build()
        Log.d("mytest1", url)
        try {
            val response = okHttpClient.newCall(request).execute()

            if (response.isSuccessful) {
                try {
                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {

                        id = data.getString("SUBSTRING(orders.created_at,1,10)")
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
        return id
    }






    private fun viewOrderID(orderID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewOrderID_url) + orderID
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

                        memberID = data.getString("memberID")

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


    private fun viewUser()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewOneUser_url) + memberID
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

                        txtmemberStoreName?.text = data.getString("memberStoreName")
                        txtmemberAddress?.text = data.getString("memberAddress")
                        txtmemberPhone?.text = data.getString("memberPhone")
                        txtNameCus?.text = data.getString("memberName")

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


    private fun viewONEOrderUserProductIDJobEmp3(salaryID: String?) {
        val data = ArrayList<Data>()
        val url: String = getString(R.string.root_url) + getString(R.string.viewONEOrderUserProductIDJobEmp3_url) + salaryID
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url(url).get().build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {
                        for (i in 0 until res.length()) {
                            val item: JSONObject = res.getJSONObject(i)
                            data.add( Data(
                                item.getString("productID"),
                                item.getString("productImage"),
                                item.getString("typeName"),
                                item.getString("productName"),
                                item.getString("quanSizeF"),
                                item.getString("quanSizeS"),
                                item.getString("quanSizeM"),
                                item.getString("quanSizeL"),
                                item.getString("quanSizeXL"),
                                item.getString("quanSize2XL"),
                                item.getString("quanSize3XL"),
                                item.getString("Total"),
                                item.getString("orderID"),
                                    salaryID
                            )
                            )
                            patternRate += Integer.valueOf(item.getString("patternRate"))
                            recyclerView4!!.adapter = DataAdapter(data)
                        }
                    } else {
                        Toast.makeText(context, "ไม่สามารถแสดงข้อมูลได้",
                            Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) { e.printStackTrace() }
            } else { response.code }
        } catch (e: IOException) { e.printStackTrace() }
    }

private fun showDataListMat(orderID: String?) {
        val data = ArrayList<Data>()
        val url: String = getString(R.string.root_url) + getString(R.string.viewONEOrderUserProductID_url) + memberID + "/" + orderID
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url(url).get().build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {
                        for (i in 0 until res.length()) {
                            val item: JSONObject = res.getJSONObject(i)
                            data.add( Data(
                                item.getString("productID"),
                                item.getString("productImage"),
                                item.getString("typeName"),
                                item.getString("productName"),
                                item.getString("SUM(order_details.quanSizeF)"),
                                item.getString("SUM(order_details.quanSizeS)"),
                                item.getString("SUM(order_details.quanSizeM)"),
                                item.getString("SUM(order_details.quanSizeL)"),
                                item.getString("SUM(order_details.quanSizeXL)"),
                                item.getString("SUM(order_details.quanSize2XL)"),
                                item.getString("SUM(order_details.quanSize3XL)"),
                                item.getString("SUM(order_details.quanTotal)"),
                                item.getString("orderID"),
                                    (0.toString())
                            )
                            )
                            patternRate += Integer.valueOf(item.getString("patternRate"))
                            recyclerView4!!.adapter = DataAdapter(data)
                        }
                    } else {
                        Toast.makeText(context, "ไม่สามารถแสดงข้อมูลได้",
                            Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) { e.printStackTrace() }
            } else { response.code }
        } catch (e: IOException) { e.printStackTrace() }
    }


    internal class Data(
        var productID: String?,var productImage: String,var typeName: String?,var productName: String?,
        var SUMquanSizeF: String?,
        var SUMquanSizeS: String?,
        var SUMquanSizeM: String?,
        var SUMquanSizeL: String?,
        var SUMquanSizeXL: String?,
        var SUMquanSize2XL: String?,
        var SUMquanSize3XL: String?,
        var SUMAllPrice: String?,
        var orderID: String?,
        var salaryID: String?

    )

    internal inner class DataAdapter(private val list: List<Data>) :
        RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_order_detail,
                parent, false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val data = list[position]
            holder.data = data
            var url = getString(R.string.root_url) + getString(R.string.product_image_url) + data.productImage
            Picasso.get().load(url).into(holder.imageView)
            holder.txtproductID.text = data.productID
            holder.txttypeName.text = data.typeName
            holder.txtorderid.text = data.orderID
            holder.txtNameProduct.text = data.productName
            holder.txtTotalQuanSizeF.text = data.SUMquanSizeF
            if (holder.txtTotalQuanSizeF.text == "0"){
                holder.txtTotalQuanSizeF.text = ""
            }
            holder.txtTotalQuanSizeS.text = data.SUMquanSizeS
            if (holder.txtTotalQuanSizeS.text == "0"){
                holder.txtTotalQuanSizeS.text = ""
            }
            holder.txtTotalQuanSizeM.text = data.SUMquanSizeM
            if (holder.txtTotalQuanSizeM.text == "0"){
                holder.txtTotalQuanSizeM.text = ""
            }
            holder.txtTotalQuanSizeL.text = data.SUMquanSizeL
            if (holder.txtTotalQuanSizeL.text == "0"){
                holder.txtTotalQuanSizeL.text = ""
            }
            holder.txtTotalQuanSizeXL.text = data.SUMquanSizeXL
            if (holder.txtTotalQuanSizeXL.text == "0"){
                holder.txtTotalQuanSizeXL.text = ""
            }
            holder.txtTotalQuanSize2XL.text = data.SUMquanSize2XL
            if (holder.txtTotalQuanSize2XL.text == "0"){
                holder.txtTotalQuanSize2XL.text = ""
            }
            holder.txtTotalQuanSize3XL.text = data.SUMquanSize3XL
            if (holder.txtTotalQuanSize3XL.text == "0"){
                holder.txtTotalQuanSize3XL.text = ""
            }
            holder.txtTotalAllPrice.text = data.SUMAllPrice



            if(empTypeID == "3"){
                viewOrderUserColorGROUPJobEmp3(holder.recyclerView2, data.salaryID)
            }else{
                showDataListColor(holder.recyclerView2, memberID, holder.txtproductID.text.toString(),data.orderID)
            }



        }


        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var data: Data? = null
            var recyclerView2: RecyclerView = itemView.findViewById(R.id.recyclerView2)
            var imageView: ImageView = itemView.findViewById(R.id.imageView)
            var txtorderid: TextView = itemView.findViewById(R.id.txtorderid)
            var txtNameProduct: TextView = itemView.findViewById(R.id.txtNameProduct)
            var txtproductID: TextView = itemView.findViewById(R.id.txtproductID)
            var txttypeName: TextView = itemView.findViewById(R.id.txttypeName)
            var txtTotalQuanSizeF: TextView = itemView.findViewById(R.id.txtTotalQuanSizeF)
            var txtTotalQuanSizeS: TextView = itemView.findViewById(R.id.txtTotalQuanSizeS)
            var txtTotalQuanSizeM: TextView = itemView.findViewById(R.id.txtTotalQuanSizeM)
            var txtTotalQuanSizeL: TextView = itemView.findViewById(R.id.txtTotalQuanSizeL)
            var txtTotalQuanSizeXL: TextView = itemView.findViewById(R.id.txtTotalQuanSizeXL)
            var txtTotalQuanSize2XL: TextView = itemView.findViewById(R.id.txtTotalQuanSize2XL)
            var txtTotalQuanSize3XL: TextView = itemView.findViewById(R.id.txtTotalQuanSize3XL)
            var txtTotalAllPrice: TextView = itemView.findViewById(R.id.txtTotalAllPrice)
        }
    }

    private fun viewOrderUserColorGROUPJobEmp3(recyclerView2: RecyclerView?,salaryID: String?) {
        val data2 = ArrayList<Data2>()
        val url: String = getString(R.string.root_url) + getString(R.string.viewOrderUserColorGROUPJobEmp3_url) + salaryID
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url(url).get().build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {

                            val item: JSONObject = res.getJSONObject(0)
                            data2.add( Data2(

                                    item.getString("quanTotal"),
                                    item.getString("quanSizeF"),
                                    item.getString("quanSizeS"),
                                    item.getString("quanSizeM"),
                                    item.getString("quanSizeL"),
                                    item.getString("quanSizeXL"),
                                    item.getString("quanSize2XL"),
                                    item.getString("quanSize3XL"),
                                    item.getString("materialColor")
                            )
                            )
                            recyclerView2!!.adapter = DataAdapter2(data2)

                    } else {
                        Toast.makeText(context, "ไม่สามารถแสดงข้อมูลได้",
                                Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) { e.printStackTrace() }
            } else { response.code }
        } catch (e: IOException) { e.printStackTrace() }
    }


    private fun showDataListColor(recyclerView2: RecyclerView?,memberID: String?,productID: String?,orderID: String?) {
        val data2 = ArrayList<Data2>()
        val url: String = getString(R.string.root_url) + getString(R.string.viewOrderUserColorGROUP_url) + memberID + "/" + productID + "/" + orderID
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url(url).get().build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {
                        for (i in 0 until res.length()) {
                            val item: JSONObject = res.getJSONObject(i)
                            data2.add( Data2(

                                item.getString("quanTotal"),
                                item.getString("quanSizeF"),
                                item.getString("quanSizeS"),
                                item.getString("quanSizeM"),
                                item.getString("quanSizeL"),
                                item.getString("quanSizeXL"),
                                item.getString("quanSize2XL"),
                                item.getString("quanSize3XL"),
                                item.getString("materialColor")
                            )
                            )
                            recyclerView2!!.adapter = DataAdapter2(data2)
                        }
                    } else {
                        Toast.makeText(context, "ไม่สามารถแสดงข้อมูลได้",
                            Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) { e.printStackTrace() }
            } else { response.code }
        } catch (e: IOException) { e.printStackTrace() }
    }


    internal class Data2(
        var priceTotal: String?,
        var quansumF: String?,
        var quansumS: String?,
        var quansumM: String?,
        var quansumL: String?,
        var quansumXL: String?,
        var quansum2XL: String?,
        var quansum3XL: String?,
        var materialColor: String
    )

    internal inner class DataAdapter2(private val list2: List<Data2>) :
        RecyclerView.Adapter<DataAdapter2.ViewHolder2>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder2 {
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_color,
                parent, false
            )
            return ViewHolder2(view)
        }

        override fun onBindViewHolder(holder: ViewHolder2, position: Int) {

            val data = list2[position]
            holder.data = data
            holder.txtColor.text = data.materialColor
            holder.txtF.text = data.quansumF
            if (holder.txtF.text == "0"){
                holder.txtF.text = ""
            }
            holder.txtS.text = data.quansumS
            if (holder.txtS.text == "0"){
                holder.txtS.text = ""
            }
            holder.txtM.text = data.quansumM
            if (holder.txtM.text == "0"){
                holder.txtM.text = ""
            }
            holder.txtL.text = data.quansumL
            if (holder.txtL.text == "0"){
                holder.txtL.text = ""
            }
            holder.txtXL.text = data.quansumXL
            if (holder.txtXL.text == "0"){
                holder.txtXL.text = ""
            }
            holder.txt2XL.text = data.quansum2XL
            if (holder.txt2XL.text == "0"){
                holder.txt2XL.text = ""
            }
            holder.txt3XL.text = data.quansum3XL
            if (holder.txt3XL.text == "0"){
                holder.txt3XL.text = ""
            }
            holder.txtTotal.text = data.priceTotal


        }


        override fun getItemCount(): Int {
            return list2.size
        }

        internal inner class ViewHolder2(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var data: Data2? = null
            var txtColor: TextView = itemView.findViewById(R.id.txtColor)
            var txtF: TextView = itemView.findViewById(R.id.txtF)
            var txtS: TextView = itemView.findViewById(R.id.txtS)
            var txtM: TextView = itemView.findViewById(R.id.txtM)
            var txtL: TextView = itemView.findViewById(R.id.txtL)
            var txtXL: TextView = itemView.findViewById(R.id.txtXL)
            var txt2XL: TextView = itemView.findViewById(R.id.txt2XL)
            var txt3XL: TextView = itemView.findViewById(R.id.txt3XL)
            var txtTotal: TextView = itemView.findViewById(R.id.txtTotal)
        }
    }



}