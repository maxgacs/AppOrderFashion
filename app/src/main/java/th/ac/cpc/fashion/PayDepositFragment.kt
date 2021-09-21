package th.ac.cpc.fashion

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.*
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class PayDepositFragment : Fragment() {
    var userID: String? = null

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

    var SUMdataMaterialID = ArrayList<String>()
    var SUMdataAll = ArrayList<String>()

    var recyclerView4: RecyclerView? = null
    var txtmemberStoreName: TextView? = null
    var txtmemberAddress: TextView? = null
    var txtmemberPhone: TextView? = null
    var txtNameCus: TextView? = null
    var txtDate: TextView? = null
    var txtDataBut: TextView? = null
    var txtDateline: TextView? = null
    var btnComfirm: Button? = null

    var txtTotalQuan: TextView? = null
    var txtTotalPrice: TextView? = null
    var txtdepos: TextView? = null

    var Dateline: Int = 6
    var ColorQuanTotal: String? = null
    var imageFilePath: String? = null
    var imageViewSlip: ImageView? = null
    var imagepro: ImageView? = null
    var file: File? = null

    var quanUsed: Int = 0

    var CheckComfirm: String? = "0"
    var CheckMaterialAll: Int? = 0
    var CheckMaterialSPAll: Int? = 0

    var progressDialog:ProgressDialog?=null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_pay_deposit, container, false)

        val bundle = this.arguments

        val policy =
                StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)



        val sharedPrefer = requireContext().getSharedPreferences(
                LoginActivity().appPreference, Context.MODE_PRIVATE
        )
        userID = sharedPrefer?.getString(LoginActivity().userIdPreference, 0.toString())

        recyclerView4 = root.findViewById(R.id.recyclerView4)
        txtmemberStoreName = root.findViewById(R.id.txtmemberStoreName)
        txtmemberAddress = root.findViewById(R.id.txtmemberAddress)
        txtmemberPhone = root.findViewById(R.id.txtmemberPhone)
        txtNameCus = root.findViewById(R.id.txtNameCus)
        txtDate = root.findViewById(R.id.txtDate)
        txtDataBut = root.findViewById(R.id.txtDataBut)
        txtTotalQuan = root.findViewById(R.id.txtTotalQuan)
        txtTotalPrice = root.findViewById(R.id.txtTotalPrice)
        txtdepos = root.findViewById(R.id.txtdepos)
        txtDateline = root.findViewById(R.id.txtDateline)




        if(userID == "0"){
            val editor = sharedPrefer.edit()
            editor.clear() // ทำการลบข้อมูลทั้งหมดจาก preferences

            editor.commit() // ยืนยันการแก้ไข preferences

            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }else{
            val date = LocalDate.now()

            viewUser(userID)
            viewSum(userID)
            viewDateline(userID)

            txtDate?.text = date.format(DateTimeFormatter.ofPattern("M/d/y"))
            txtDataBut?.text = date.format(DateTimeFormatter.ofPattern("M/d/y"))
            txtDateline?.text = Dateline.toString()
            showDataListMat(userID)

            imageViewSlip = root.findViewById(R.id.imageViewSlip)
            imagepro = root.findViewById(R.id.imagepro)
            imagepro?.setOnClickListener {
                val builder1 = AlertDialog.Builder(requireActivity())
                builder1.setMessage("คุณต้องการเลือกรูปภาพจากคลังหรือถ่ายรูป?")
                builder1.setNegativeButton(
                        "เลือกรูปจากคลัง"
                ) { dialog, id -> //dialog.cancel();
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "image/*"
                    startActivityForResult(intent, 100)
                    imagepro?.visibility = View.VISIBLE
                    CheckComfirm = "1"
                }
                builder1.setPositiveButton(
                        "ถ่ายรูป"
                ) { dialog, id -> //dialog.cancel();
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    var imageURI: Uri? = null
                    try {
                        imageURI = FileProvider.getUriForFile(
                                requireActivity(),
                                BuildConfig.APPLICATION_ID.toString() + ".provider",
                                createImageFile()!!
                        )
                    } catch (e: IOException) { e.printStackTrace() }

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI)
                    startActivityForResult(intent, 200)
                    imagepro?.visibility = View.VISIBLE
                    CheckComfirm = "1"
                }

                val alert11 = builder1.create()
                alert11.show()
            }

            permission()

            btnComfirm = root.findViewById(R.id.btnComfirm)
            btnComfirm?.setOnClickListener{
                if (CheckComfirm == "1"){
                    progressDialog = ProgressDialog(requireContext())
                    progressDialog?.setTitle("กำลังดำเนินการ")
                    progressDialog?.setMessage("กรุณารอซักครู่......")
                    progressDialog?.show()
                    Handler().postDelayed({

                    ViewChoose_Mat()

                    for (i in 0 until detmaterialID.size) {
                        CheckMaterial(detmaterialID[i],detsizeRate[i])
                    }
                    if (CheckMaterialAll.toString() >= "1"){
                        Toast.makeText(context, "มีวัตถุดิบไม่เพียงพอ", Toast.LENGTH_LONG).show()
                        progressDialog?.dismiss()
                    }else{
                        ViewChoose_Mat_Sp()
                        for (i in 0 until detmaterial_SpID.size) {
                            CheckMaterialSP(detmaterial_SpID[i],detquanRate[i])
                        }

                        if (CheckMaterialSPAll.toString() >= "1"){
                            Toast.makeText(context, "มีวัตถุดิบไม่เพียงพอ", Toast.LENGTH_LONG).show()
                            progressDialog?.dismiss()
                        }else{
                            viewSUMdata(userID)
                            viewCart(userID)
                            addOrder()
                            addOrderDetailmoney()
                            AddDeposit()
                            UpImageDeposit()
                            //ViewChoose_Mat()
                            updateMaterialSize()
                            //ViewChoose_Mat_Sp()
                            updateMaterial_SpSize()
                            //updateOrderData()

                            deletecart()
                        }
                    }

                    }, 100)


                }else{
                    Toast.makeText(context, "แนบใบเสร็จการจ่ายเงิน", Toast.LENGTH_LONG).show()
                }
            }

        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {

            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.nav_host_fragment, CartFragment())
            fragmentTransaction.commit()
        }
        callback.isEnabled

        return root
    }

    private fun CheckMaterialSP(materialIDSP: String?,sizeRateSP: String?)
    {

        var url: String = getString(R.string.root_url) + getString(R.string.viewMaterial_Sp_url) + materialIDSP
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

                        if (sizeRateSP != null) {
                            val num = (data.getString("material_spQuan").toFloat() - sizeRateSP.toFloat()).toString()
                            if (num.toFloat() < 0){
                                CheckMaterialSPAll = CheckMaterialSPAll?.plus(1)
                            }else{
                                CheckMaterialSPAll = CheckMaterialSPAll?.plus(0)
                            }
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

    private fun CheckMaterial(materialID: String?,sizeRate: String?)
    {

        var url: String = getString(R.string.root_url) + getString(R.string.viewMaterial_url) + materialID
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

                        if (sizeRate != null) {
                            val num = (data.getString("materialSize").toFloat() - sizeRate.toFloat()).toString()
                            if (num.toFloat() < 0){
                                CheckMaterialAll = CheckMaterialAll?.plus(1)
                            }else{
                                CheckMaterialAll = CheckMaterialAll?.plus(0)
                            }
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

    private fun viewMaterial_Sp(material_SpID: String?): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewMaterial_Sp_url) + material_SpID
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

                        id=data.getString("material_spQuan")

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

    private fun updateMaterial_SpSize()
    {
        for (i in 0 until detmaterial_SpID.size) {
            var url: String = getString(R.string.root_url) + getString(R.string.updateMaterial_SpSize_url) + detmaterial_SpID[i]
            val okHttpClient = OkHttpClient()
            val formBody: RequestBody = FormBody.Builder()

                    .add(
                            "material_spQuan",
                            ((viewMaterial_Sp(detmaterial_SpID[i]).toString()).toFloat() - (detquanRate[i]).toFloat()).toString()
                    )
                    .build()
            val request: Request = Request.Builder()
                    .url(url)
                    .put(formBody)
                    .build()
            Log.d("mytest1", url)
            Log.d("mytest1", detmaterial_SpID[i])
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
    }

    private fun ViewChoose_Mat_Sp() {
        var url: String = getString(R.string.root_url) + getString(R.string.QuanUsedSp_url) + userID
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
                            detquanRate.add((data.getString("Materials_SpQuanUsed")).toString())
                            detmaterial_SpID.add((data.getString("material_spID")).toString())
                        }
                    } else {
                        Toast.makeText(
                                context, "ไม่สามารถแสดงข้อมูลได้",
                                Toast.LENGTH_LONG
                        ).show()
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

    private fun viewMaterial(materialID: String?): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewMaterial_url) + materialID
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

                        id=data.getString("materialSize")

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

    private fun  updateMaterialSize()
    {
        for (i in 0 until detmaterialID.size) {
            var url: String = getString(R.string.root_url) + getString(R.string.updateMaterialSize_url) + detmaterialID[i]
            val okHttpClient = OkHttpClient()
            val formBody: RequestBody = FormBody.Builder()

                    .add(
                            "materialSize",
                            ((viewMaterial(detmaterialID[i]).toString()).toFloat() - (detsizeRate[i]).toFloat()).toString()
                    )
                    .build()
            val request: Request = Request.Builder()
                    .url(url)
                    .put(formBody)
                    .build()
            Log.d("mytest1", url)
            Log.d("mytest1", detmaterialID[i])
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
    }

    private fun ViewChoose_Mat() {
            var url: String = getString(R.string.root_url) + getString(R.string.QuanUsed_url) + userID
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
                                detsizeRate.add((data.getString("MaterialsQuanUsed")).toString())
                                detmaterialID.add((data.getString("materialID")).toString())
                            }
                        } else {
                            Toast.makeText(
                                    context, "ไม่สามารถแสดงข้อมูลได้",
                                    Toast.LENGTH_LONG
                            ).show()
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

    private fun UpImageDeposit()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.AddImageDeposit_url) + viewDepositDESC().toString()
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "file", file?.name,
                        RequestBody.create("application/octet-stream".toMediaTypeOrNull(), file!!)
                )
                .build()
        Log.d("mytest", url)
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

    private fun viewDepositDESC(): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewDepositDESC_url)
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
                .url(url)
                .get()////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {

                        id=data.getString("DepositID")

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

    private fun AddDeposit() {

        var url: String = getString(R.string.root_url) + getString(R.string.AddDeposit_url) + userID
        val okHttpClient = OkHttpClient()

        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "file", file?.name,
                        RequestBody.create("application/octet-stream".toMediaTypeOrNull(), file!!)
                )
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


        private fun deletecart()
    {
        for (i in 0 until addCartID.size) {
            var url: String = getString(R.string.root_url) + getString(R.string.cartdelete_url) + addCartID[i]
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

        progressDialog?.dismiss()

        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.nav_host_fragment, OrderFragment())
        fragmentTransaction.commit()
    }

    private fun addOrder() {

            var url: String = getString(R.string.root_url) + getString(R.string.AddOrder_url)
            val okHttpClient = OkHttpClient()

            val formBody: RequestBody = FormBody.Builder()

                    .add("overdueMoney", HalfpriceTotal().toString())
                    .add("memberID", userID.toString())
                    .add("dateline", Dateline.toString())

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

    private fun updateOrderData()
    {
        for (i in 0 until SUMdataMaterialID.size) {
            var url: String = getString(R.string.root_url) + getString(R.string.updateOrderData_url) + SUMdataMaterialID[i]
            val okHttpClient = OkHttpClient()
            val formBody: RequestBody = FormBody.Builder()

                .add("dateline", SUMdataAll[i])
                .build()
            val request: Request = Request.Builder()
                .url(url)
                .put(formBody)
                .build()
            Log.d("mytest1", url)
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
    }

    private fun viewSUMdata(userID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.SUMdataTotal_url) + userID
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .get()
            .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {

                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {
                        for (i in 0 until res.length()) {
                            val data: JSONObject = res.getJSONObject(i)

                            SUMdataMaterialID.add(data.getString("materialID"))
                            SUMdataAll.add(data.getString("TotalAllQuan"))

                        }
                    } else {
                        Toast.makeText(
                                context, "ไม่สามารถแสดงข้อมูลได้",
                                Toast.LENGTH_LONG
                        ).show()
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

    private fun viewCart(userID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewCart_url) + userID
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
                .url(url)
                .get()
                .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {

                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {
                        for (i in 0 until res.length()) {
                            val data: JSONObject = res.getJSONObject(i)

                            addCartID.add(data.getString("shopCartID"))
                            addpriceTotal.add(data.getString("priceTotal"))
                            addpriceSizeF.add(data.getString("priceSizeF"))
                            addpriceSizeS.add(data.getString("priceSizeS"))
                            addpriceSizeM.add(data.getString("priceSizeM"))
                            addpriceSizeL.add(data.getString("priceSizeL"))
                            addpriceSizeXL.add(data.getString("priceSizeXL"))
                            addpriceSize2XL.add(data.getString("priceSize2XL"))
                            addpriceSize3XL.add(data.getString("priceSize3XL"))
                            addquanTotal.add(data.getString("quanTotal"))
                            addquanSizeF.add(data.getString("quanSizeF"))
                            addquanSizeS.add(data.getString("quanSizeS"))
                            addquanSizeM.add(data.getString("quanSizeM"))
                            addquanSizeL.add(data.getString("quanSizeL"))
                            addquanSizeXL.add(data.getString("quanSizeXL"))
                            addquanSize2XL.add(data.getString("quanSize2XL"))
                            addquanSize3XL.add(data.getString("quanSize3XL"))
                            addproductID.add(data.getString("productID"))
                            addmaterialID.add(data.getString("materialID"))

                        }
                    } else {
                        Toast.makeText(
                                context, "ไม่สามารถแสดงข้อมูลได้",
                                Toast.LENGTH_LONG
                        ).show()
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

    private fun addOrderDetailmoney() {
        for (i in 0 until addpriceTotal.size) {
            var url: String = getString(R.string.root_url) + getString(R.string.AddOrder_Details_url)
            val okHttpClient = OkHttpClient()

            val formBody: RequestBody = FormBody.Builder()

                    .add("priceTotal", addpriceTotal[i])
                    .add("priceSizeF", addpriceSizeF[i])
                    .add("priceSizeS", addpriceSizeS[i])
                    .add("priceSizeM", addpriceSizeM[i])
                    .add("priceSizeL", addpriceSizeL[i])
                    .add("priceSizeXL", addpriceSizeXL[i])
                    .add("priceSize2XL", addpriceSize2XL[i])
                    .add("priceSize3XL", addpriceSize3XL[i])
                    .add("quanTotal", addquanTotal[i])
                    .add("quanSizeF", addquanSizeF[i])
                    .add("quanSizeS", addquanSizeS[i])
                    .add("quanSizeM", addquanSizeM[i])
                    .add("quanSizeL", addquanSizeL[i])
                    .add("quanSizeXL", addquanSizeXL[i])
                    .add("quanSize2XL", addquanSize2XL[i])
                    .add("quanSize3XL", addquanSize3XL[i])
                    .add("productID", addproductID[i])
                    .add("materialID", addmaterialID[i])
                    .add("orderID", viewOrderDESC().toString())
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
    }

    private fun viewOrderDESC(): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewOrderDESC_url)
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
                .url(url)
                .get()////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {

                        id=data.getString("orderID")

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

    private fun HalfpriceTotal(): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.HalfpriceTotal_url) + userID
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
                .url(url)
                .get()////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {

                        id=data.getString("TotalAllPrice")

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

    private fun viewDateline(userID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewMaterialID_url) + userID
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
                .url(url)
                .get()
                .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {


                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {
                        for (i in 0 until res.length()) {
                            val item: JSONObject = res.getJSONObject(i)
                            Dateline += ((item.getString("(SUM(shop_carts.quanTotal)/24)+1")).toFloat()).toInt()
                        }
                    } else {
                        Dateline = 0
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

    private fun viewSum(userID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.SUMpriceTotal_url) + userID
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

                        txtTotalPrice?.text = data.getString("TotalAllPrice")
                        txtTotalQuan?.text = data.getString("TotalAllQuan")
                        txtdepos?.text = data.getString("HalfPrice")

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


    private fun showDataListMat(userID: String?) {
        val data = ArrayList<Data>()
        val url: String = getString(R.string.root_url) + getString(R.string.viewONECartUserProductID_url) + userID
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
                            data.add(
                                    Data(
                                            item.getString("productID"),
                                            item.getString("productImage"),
                                            item.getString("typeName"),
                                            item.getString("productName"),
                                            item.getString("SUM(shop_carts.quanSizeF)"),
                                            item.getString("SUM(shop_carts.quanSizeS)"),
                                            item.getString("SUM(shop_carts.quanSizeM)"),
                                            item.getString("SUM(shop_carts.quanSizeL)"),
                                            item.getString("SUM(shop_carts.quanSizeXL)"),
                                            item.getString("SUM(shop_carts.quanSize2XL)"),
                                            item.getString("SUM(shop_carts.quanSize3XL)"),
                                            item.getString("SUM(shop_carts.quanTotal)")
                                    )
                            )
                            recyclerView4!!.adapter = DataAdapter(data)
                        }
                    } else {
                        Toast.makeText(
                                context, "ไม่มีสินค้าในตะกร้า",
                                Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: JSONException) { e.printStackTrace() }
            } else { response.code }
        } catch (e: IOException) { e.printStackTrace() }
    }


    internal class Data(
            var productID: String?, var productImage: String, var typeName: String?,var productName: String?,
            var SUMquanSizeF: String?,
            var SUMquanSizeS: String?,
            var SUMquanSizeM: String?,
            var SUMquanSizeL: String?,
            var SUMquanSizeXL: String?,
            var SUMquanSize2XL: String?,
            var SUMquanSize3XL: String?,
            var SUMAllPrice: String?,

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
            holder.txtTotalQuanSizeF.text = data.SUMquanSizeF
            holder.txtNameProduct.text = data.productName

                holder.txtorderid.text = "ต้องสั่งซื้อก่อน"

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

            showDataListColor(holder.recyclerView2, userID, holder.txtproductID.text.toString())

        }


        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) :
                RecyclerView.ViewHolder(itemView) {
            var data: Data? = null
            var recyclerView2: RecyclerView = itemView.findViewById(R.id.recyclerView2)
            var imageView: ImageView = itemView.findViewById(R.id.imageView)
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
            var txtorderid: TextView = itemView.findViewById(R.id.txtorderid)
            var txtNameProduct: TextView = itemView.findViewById(R.id.txtNameProduct)
        }
    }


    private fun showDataListColor(recyclerView2: RecyclerView?, userID: String?, productID: String?) {
        val data2 = ArrayList<Data2>()
        val url: String = getString(R.string.root_url) + getString(R.string.viewCartUserColorGROUP_url) + userID + "/" + productID
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
                            data2.add(
                                    Data2(

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
                        Toast.makeText(
                                context, "ไม่มีสินค้าในตะกร้า",
                                Toast.LENGTH_LONG
                        ).show()
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
            var materialColor: String,
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


    private fun permission()
    {
        //Set permission to open camera and access a directory
        if ((ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                )
                        != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                        != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(
                    arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 225
            )
        }
    }


    private fun createImageFile(): File? {
        // Create an image file name
        val storageDir = File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                ), ""
        )
        val image = File.createTempFile(
                SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()), ".png",
                storageDir
        )
        imageFilePath = image.absolutePath
        return image
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && null != intent) {
            val uri = intent.data
            file = File(getFilePath(uri))
            val bitmap: Bitmap
            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                //show image
                imagepro?.setImageBitmap(bitmap)
                imagepro?.setImageURI(uri)
                imagepro?.visibility = View.VISIBLE
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            val imageUri = Uri.parse("file:$imageFilePath")
            file = File(imageUri.path)
            try {
                val ims: InputStream = FileInputStream(file)
                var imageBitmap = BitmapFactory.decodeStream(ims)
                imageBitmap = resizeImage(imageBitmap, 1024, 1024) //resize image
                imageBitmap = resolveRotateImage(imageBitmap, imageFilePath!!) //Resolve auto rotate image

                //show image
                imagepro?.setImageBitmap(imageBitmap)
                imagepro?.visibility = View.VISIBLE
                getFileName(imageUri)

            } catch (e: FileNotFoundException) {
                return
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun getFilePath(uri: Uri?): String? {
        var path = ""
        val wholeID = DocumentsContract.getDocumentId(uri)
        // Split at colon, use second item in the arraygetDocumentId(uri)
        val id = wholeID.split(":".toRegex()).toTypedArray()[1]
        val column = arrayOf(MediaStore.Images.Media.DATA)
        // where id is equal to
        val sel = MediaStore.Images.Media._ID + "=?"
        val cursor = requireActivity().contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, arrayOf(id), null
        )
        var columnIndex = 0
        if (cursor != null) {
            columnIndex = cursor.getColumnIndex(column[0])
            if (cursor.moveToFirst()) {
                path = cursor.getString(columnIndex)
            }
            cursor.close()
        }
        return path
    }

    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = requireActivity().contentResolver.query(
                    uri, null, null, null, null
            )
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(
                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    )
                }
            } finally {
                cursor!!.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    private fun resizeImage(bm: Bitmap?, newWidth: Int, newHeight: Int): Bitmap? {
        val width = bm!!.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP
        val resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false
        )
        bm.recycle()
        return resizedBitmap
    }

    private fun resolveRotateImage(bitmap: Bitmap?, photoPath: String): Bitmap? {
        val ei = ExifInterface(photoPath)
        val orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
        )
        var rotatedBitmap: Bitmap? = null
        rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }
        return rotatedBitmap
    }

    private fun rotateImage(source: Bitmap?, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
                source!!, 0, 0, source.width, source.height,
                matrix, true
        )
    }


}