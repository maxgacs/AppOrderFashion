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

class StepEmpWrokOrderFragment : Fragment() {
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
    var txtCancel: EditText? = null
    var txtDateline: TextView? = null
    var txtCountdown: TextView? = null

    var quanUsed: Int = 0
    var cutRate: Int = 0
    var patternRate: Int = 0
    var empTypeID: String? = null
    var userID: String? = null
    var TotalAllPrice: String? = null
    var TotalAllQuan: String? = null

    var NullempID2: String = "0"
    var EmpID2: String = "0"
    var NullempID3: String = "0"
    var EmpID3 = ArrayList<String>()
    //var EmpID3: String = "0"
    var NullempID4: String = "0"
    var EmpID4: String = "0"

    var NullJobemp3: String = "0"

    var SOrder1: String = "0"
    var OrderID1: String = "0"

    var SOrder2: String = "0"
    var OrderID2: String = "0"

    var SOrder3: String = "0"
    var OrderID3: String = "0"
    var salaryIDJobEmp3: String = "0"

    var SOrder4: String = "0"
    var OrderID4: String = "0"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_step_emp_wrok_order, container, false)

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
        txtCountdown = root.findViewById(R.id.txtCountdown)
        if (Integer.valueOf(viewQuanEndDateOrderID(bundle?.get("salaryID").toString())) > 0){
            txtCountdown?.text = "0"
        }else{
            txtCountdown?.text = (Integer.valueOf(viewQuanEndDateOrderID(bundle?.get("salaryID").toString()))*(-1)).toString()
        }
        txtDateline = root.findViewById(R.id.txtDateline)
        txtDateline?.text = ViewDateline(bundle?.get("salaryID").toString())


        viewOrderID(bundle?.get("orderID").toString())
        viewUser()
        viewSum(bundle?.get("orderID").toString())
        txtDate?.text = ViewCreated(bundle?.get("orderID").toString())
        txtDataBut?.text = viewSalaryDataStart(bundle?.get("salaryID").toString())

        viewEmpType()

        if(empTypeID == "3"){

            viewONEOrderUserProductIDJobEmp3(bundle?.get("salaryID").toString())

        }else {

            showDataListMat(bundle?.get("orderID").toString())

        }


        SelectEmployeesTypeID2Updated_atASC()
        SelectEmployeesTypeID3Updated_atASC()
        SelectEmployeesTypeID4Updated_atASC()

        viewOrderStepStatusIDWaitingASC1()
        viewOrderStepStatusIDWaitingASC2()

        viewOrderStepStatusIDWaitingASC4()

        viewSalaryJobEmpCutNull(bundle?.get("orderID").toString())

        btnComfirm = root.findViewById(R.id.btnComfirm)
        btnComfirm?.setOnClickListener{
            if(empTypeID == "1"){
                updateSalaryStat(bundle?.get("salaryID").toString())
                OrderCutRate(bundle?.get("orderID").toString())
                if (NullempID2 == "0"){

                    updateOrderComfirm2(bundle?.get("orderID").toString())
                    AddSalary2(bundle?.get("orderID").toString())
                    updateEmpStat2()
                    Toast.makeText(
                        context, "ส่งงานเรียบร้อย",
                        Toast.LENGTH_LONG
                    ).show()

                }else{

                    updateOrderComfirm2Work(bundle?.get("orderID").toString())
                    AddSalaryWaiting2(bundle?.get("orderID").toString())
                    Toast.makeText(
                        context, "ไม่มีพนักงานว่าง ออเดอร์นี้อยู่ในสถานะรอ",
                        Toast.LENGTH_LONG
                    ).show()
                }

                if (Integer.valueOf(txtCountdown?.text.toString()) > 0){
                    updateSalaryMoneyEmp(bundle?.get("salaryID").toString(),viewSalaryMoney(bundle?.get("salaryID").toString()))
                }

                if (SOrder1 == "0"){
                    //updateEmpStatLoginNow()
                    //AddSalaryLogin1()
                    updateSalaryLogin1(userID)
                    updateOrderComfirmLogin1()
                }else{
                    updateEmpStat0()
                }

                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.nav_host_fragment,HomeEmpFragment())
                fragmentTransaction.commit()

            }else if (empTypeID == "2"){
                updateSalaryStat(bundle?.get("salaryID").toString())

                if (NullempID3 == "0"){
                    updateOrderComfirm3(bundle?.get("orderID").toString())
                    AddSalary3(bundle?.get("orderID").toString())
                    updateEmpStat3()
                    Toast.makeText(
                        context, "ส่งงานเรียบร้อย",
                        Toast.LENGTH_LONG
                    ).show()
                }else{
                    AddSalary3(bundle?.get("orderID").toString())
                    updateOrderComfirm3Work(bundle?.get("orderID").toString())
                    Toast.makeText(
                        context, "ไม่มีพนักงานว่าง ออเดอร์นี้อยู่ในสถานะรอ",
                        Toast.LENGTH_LONG
                    ).show()
                }

                if (Integer.valueOf(txtCountdown?.text.toString()) > 0){
                    updateSalaryMoneyEmp(bundle?.get("salaryID").toString(),viewSalaryMoney(bundle?.get("salaryID").toString()))
                }

                if (SOrder2 == "0"){
                    //updateEmpStatLoginNow()
                    //AddSalaryLogin2()
                    updateSalaryLogin2(userID)
                    updateOrderComfirmLogin2()
                }else{
                    updateEmpStat0()
                }

                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.nav_host_fragment,HomeEmpFragment())
                fragmentTransaction.commit()

            }else if (empTypeID == "3"){
                updateEmpStat0()
                updateSalaryStat(bundle?.get("salaryID").toString())

                if (NullJobemp3 == "0"){


                }else{

                    if (NullempID4 == "0"){
                        updateOrderComfirm4(bundle?.get("orderID").toString())
                        AddSalary4(bundle?.get("orderID").toString())
                        updateEmpStat4()
                        Toast.makeText(
                                context, "ส่งงานเรียบร้อย",
                                Toast.LENGTH_LONG
                        ).show()
                    }else{

                        updateOrderComfirm4Work(bundle?.get("orderID").toString())
                        AddSalaryWaiting4(bundle?.get("orderID").toString())
                        Toast.makeText(
                                context, "ไม่มีพนักงานว่าง ออเดอร์นี้อยู่ในสถานะรอ",
                                Toast.LENGTH_LONG
                        ).show()
                        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.replace(R.id.nav_host_fragment,HomeEmpFragment())
                        fragmentTransaction.commit()
                    }
                }

                updateEmpStat0()

                if (Integer.valueOf(txtCountdown?.text.toString()) > 0){
                    updateSalaryMoneyEmp(bundle?.get("salaryID").toString(),viewSalaryMoney(bundle?.get("salaryID").toString()))
                }


                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.nav_host_fragment,HomeEmpFragment())
                fragmentTransaction.commit()

            }else if (empTypeID == "4"){
                updateSalaryStat(bundle?.get("salaryID").toString())

                updateOrderLastComfirm(bundle?.get("orderID").toString())
                Toast.makeText(
                    context, "ออเดอร์นี้เสร็จสิ้น",
                    Toast.LENGTH_LONG
                ).show()
                Log.d("mytest1", Integer.valueOf(txtCountdown?.text.toString()).toString())
                if (Integer.valueOf(txtCountdown?.text.toString()) > 0){
                    updateSalaryMoneyEmp(bundle?.get("salaryID").toString(),viewSalaryMoney(bundle?.get("salaryID").toString()))
                }

                if (SOrder4 == "0"){
                    //updateEmpStatLoginNow()
                    //AddSalaryLogin2()
                    updateSalaryLogin4(userID)
                    updateOrderComfirmLogin4()
                }else{
                    updateEmpStat0()
                }
                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.nav_host_fragment,HomeEmpFragment())
                fragmentTransaction.commit()
            }

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

    private fun updateSalaryMoneyEmp(orderID: String?,Money: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateSalaryMoneyEmp_url) + orderID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("Salary", txtCountdown?.text.toString())
                .addFormDataPart("SalaryNum", Money.toString())
                .build()
        Log.d("mytest11111",Money.toString())
        Log.d("mytest1", url)
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


    private fun viewQuanEndDateOrderID(salaryID: String?): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewQuanEndDateOrderID_url) + salaryID
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

                        id = data.getString("Salary")
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



    private fun viewSalaryWaiting4(): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewSalaryWaiting_url) + OrderID4 + "/0"
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

                        id = data.getString("salaryID")
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

    private fun updateSalaryLogin4(userID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateSalaryWaiting_url) + viewSalaryWaiting4()
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("statusOrder", "0")
                .addFormDataPart("empID", userID.toString())
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

    private fun viewSalaryWaiting3(): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewSalaryWaiting_url) + OrderID3 + "/0"
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

                        id = data.getString("salaryID")
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

    private fun updateSalaryLogin3()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateSalaryWaiting_url) + salaryIDJobEmp3
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("statusOrder", "0")
                .addFormDataPart("empID", userID.toString())
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

    private fun updateOrderComfirmLogin4()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderComfirm_url) + OrderID4
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("stepStatusID", "11")
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



    private fun updateOrderComfirmLogin3()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderComfirm_url) + OrderID3
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("stepStatusID", "8")
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



    private fun viewSalaryWaiting2(): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewSalaryWaiting_url) + OrderID2 + "/0"
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

                        id = data.getString("salaryID")
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

    private fun updateSalaryLogin2(userID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateSalaryWaiting_url) + viewSalaryWaiting2()
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("statusOrder", "0")
                .addFormDataPart("empID", userID.toString())
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

    private fun updateOrderComfirmLogin2()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderComfirm_url) + OrderID2
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("stepStatusID", "5")
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

    /*private fun AddSalaryLogin2() {

        var url: String = getString(R.string.root_url) + getString(R.string.AddSalary_url)
        val okHttpClient = OkHttpClient()

        val formBody: RequestBody = FormBody.Builder()

                .add("Salary", )
                .add("statusOrder", "0")
                .add("empID", userID.toString())
                .add("orderID", OrderID2)

                .build()
        val request: Request = Request.Builder()
                .url(url)
                .post(formBody)
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

    }*/

    private fun updateOrderComfirmLogin1()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderComfirm_url) + OrderID1
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("stepStatusID", "2")
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


    private fun viewSalaryWaiting1(): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewSalaryWaiting_url) + OrderID1 + "/0"
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

                        id = data.getString("salaryID")
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

    private fun updateSalaryLogin1(userID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateSalaryWaiting_url) + viewSalaryWaiting1()
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("empID", userID.toString())
                .addFormDataPart("statusOrder", "0")
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

    /*private fun AddSalaryLogin1() {

        var url: String = getString(R.string.root_url) + getString(R.string.AddSalary_url)
        val okHttpClient = OkHttpClient()

        val formBody: RequestBody = FormBody.Builder()

                .add("Salary", )
                .add("statusOrder", "0")
                .add("empID", userID.toString())
                .add("orderID", OrderID1)

                .build()
        val request: Request = Request.Builder()
                .url(url)
                .post(formBody)
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

    private fun updateEmpStatLoginNow()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateEmpStat_url) + userID
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
    }*/


    private fun viewOrderStepStatusIDWaitingASC4()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewOrderStepStatusIDWaitingASC_url) + "10"
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
                        OrderID4 = data.getString("orderID")

                    } else {
                        SOrder4 = ""
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

    private fun viewOrderStepStatusIDWaitingASC3()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewSalaryJobEmpCutNull_url)
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
                        salaryIDJobEmp3 = data.getString("salaryID")

                    } else {
                        SOrder3 = ""
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

    private fun viewOrderStepStatusIDWaitingASC2()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewOrderStepStatusIDWaitingASC_url) + "4"
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
                        OrderID2 = data.getString("orderID")

                    } else {
                        SOrder2 = ""
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

    private fun viewOrderStepStatusIDWaitingASC1()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewOrderStepStatusIDWaitingASC_url) + "1"
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
                        OrderID1 = data.getString("orderID")

                    } else {
                        SOrder1 = ""
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

    private fun ViewDateline(salaryID: String?): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewSalaryOne_url) + salaryID
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

                        id = data.getString("endDate")
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
    private fun viewSalaryDataStart(salaryID: String?): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewSalaryOne_url) + salaryID
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

                        id = data.getString("startDate")
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
    private fun viewSalaryMoney(salaryID: String?): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewSalaryOne_url) + salaryID
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

                        id = data.getString("Salary")
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
    private fun updateOrderLastComfirm(orderID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderComfirm_url) + orderID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("stepStatusID", "13")
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

    private fun AddSalaryWaiting4(orderID: String?) {

        var url: String = getString(R.string.root_url) + getString(R.string.AddSalary_url)
        val okHttpClient = OkHttpClient()

        val formBody: RequestBody = FormBody.Builder()

                .add("Salary", (Integer.valueOf(TotalAllQuan) * 2).toString())
                .add("statusOrder", "0")
                .add("empID", "0")
                .add("orderID", orderID.toString())

                .build()
        val request: Request = Request.Builder()
                .url(url)
                .post(formBody)
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

    private fun AddSalary4(orderID: String?) {

        var url: String = getString(R.string.root_url) + getString(R.string.AddSalary_url)
        val okHttpClient = OkHttpClient()

        val formBody: RequestBody = FormBody.Builder()

            .add("Salary", (Integer.valueOf(TotalAllQuan) * 2).toString())
            .add("statusOrder", "0")
            .add("empID", EmpID4)
            .add("orderID", orderID.toString())

            .build()
        val request: Request = Request.Builder()
            .url(url)
            .post(formBody)
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


    private fun updateOrderComfirm4(orderID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderComfirm_url) + orderID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("stepStatusID", "11")
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

    private fun updateOrderComfirm4Work(orderID: String?)
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
    private fun updateEmpStat3()
    {
        for (i in 0 until EmpID3.size) {

        var url: String = getString(R.string.root_url) + getString(R.string.updateEmpStat3_url) + EmpID3[i]
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
                    //NullempID3 = ""
                }
            } else {
                response.code
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        }
    }

    private fun AddSalary3(orderID: String?) {

        var url: String = getString(R.string.root_url) + getString(R.string.AddSalaryEmpCut_url)
        val okHttpClient = OkHttpClient()

        val formBody: RequestBody = FormBody.Builder()

            .add("orderID", orderID.toString())

            .build()
        val request: Request = Request.Builder()
            .url(url)
            .post(formBody)
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


    /*private fun AddSalary3(orderID: String?) {

        var url: String = getString(R.string.root_url) + getString(R.string.AddSalary_url)
        val okHttpClient = OkHttpClient()

        val formBody: RequestBody = FormBody.Builder()

                .add("Salary", (Integer.valueOf(TotalAllQuan) * 35).toString())
                .add("statusOrder", "0")
                .add("empID", EmpID3)
                .add("orderID", orderID.toString())

                .build()
        val request: Request = Request.Builder()
                .url(url)
                .post(formBody)
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

    }*/


    private fun updateOrderComfirm3(orderID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderComfirm_url) + orderID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("stepStatusID", "8")
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

    private fun AddSalaryWaiting3(orderID: String?) {

        var url: String = getString(R.string.root_url) + getString(R.string.AddSalary_url)
        val okHttpClient = OkHttpClient()

        val formBody: RequestBody = FormBody.Builder()

                .add("Salary", (Integer.valueOf(TotalAllQuan) * 35).toString())
                .add("statusOrder", "0")
                .add("empID", "0")
                .add("orderID", orderID.toString())

                .build()
        val request: Request = Request.Builder()
                .url(url)
                .post(formBody)
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

    private fun updateOrderComfirm3Work(orderID: String?)
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

    private fun viewSum(orderID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.SUMOrderpriceTotal_url) + memberID + "/" + orderID
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

                        TotalAllPrice = data.getString("TotalAllPrice")
                        TotalAllQuan = data.getString("TotalAllQuan")

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

    private fun OrderCutRate(orderID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.OrderCutRate_url) + memberID + "/" + orderID
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
                        for (i in 0 until res.length()) {
                            val data: JSONObject = res.getJSONObject(i)
                            cutRate += Integer.valueOf(data.getString("cutRate"))
                        }
                    } else {

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

    private fun AddSalaryWaiting2(orderID: String?) {

        var url: String = getString(R.string.root_url) + getString(R.string.AddSalary_url)
        val okHttpClient = OkHttpClient()

        val formBody: RequestBody = FormBody.Builder()

                .add("Salary", (Integer.valueOf(TotalAllQuan) * cutRate).toString())
                .add("statusOrder", "0")
                .add("empID", "0")
                .add("orderID", orderID.toString())

                .build()
        val request: Request = Request.Builder()
                .url(url)
                .post(formBody)
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

    private fun AddSalary2(orderID: String?) {

        var url: String = getString(R.string.root_url) + getString(R.string.AddSalary_url)
        val okHttpClient = OkHttpClient()

        val formBody: RequestBody = FormBody.Builder()

            .add("Salary", (Integer.valueOf(TotalAllQuan) * cutRate).toString())
            .add("statusOrder", "0")
            .add("empID", EmpID2)
            .add("orderID", orderID.toString())

            .build()
        val request: Request = Request.Builder()
            .url(url)
            .post(formBody)
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
            .addFormDataPart("stepStatusID", "5")
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

    private fun updateOrderComfirm2Work(orderID: String?)
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

    private fun viewSalaryJobEmpCutNull(orderID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewSalaryJobEmpCutNull_url) + orderID
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


                    } else {
                        NullJobemp3 = ""
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
                        for (i in 0 until res.length()) {

                            val data: JSONObject = res.getJSONObject(i)
                            EmpID3.add(data.getString("empID"))

                        }
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


    private fun updateSalaryStat(salaryID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateSalaryStatEndDate_url) + salaryID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("statusOrder", "2")
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
                            data.add(Data(
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
                                item.getString("orderID"),(0.toString())
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
                            data2.add(Data2(

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