package th.ac.cpc.fashion

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import android.widget.*
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
import java.util.*
import kotlin.collections.ArrayList

class PaymentFragment : Fragment() {
    var userID: String? = null
    var txtStatus: TextView? = null
    var btnComfirm: Button? = null
    var txtDateline: TextView? = null
    var txtIDOrder: TextView? = null
    var txtmemberStoreName: TextView? = null
    var txtmemberAddress: TextView? = null
    var txtmemberPhone: TextView? = null
    var txtDate: TextView? = null
    var txtDataBut: TextView? = null
    var txtHalfPrice: TextView? = null
    var txtendDate: TextView? = null
    var txtCountdown: TextView? = null
    var recyclerViewOrderDe: RecyclerView? = null

    var textView170: TextView? = null
    var txtNameCus: TextView? = null
    var txtTotalQuan: TextView? = null
    var txtTotalPrice: TextView? = null
    var txtdepos: TextView? = null
    var imageFilePath: String? = null
    var imageViewSlip: ImageView? = null
    var imagepro: ImageView? = null
    var file: File? = null
    var CheckComfirm: String? = "0"
    var CheckDateline: String? = "0"
    var TotalPrice: String? = "0"
    var progressDialog:ProgressDialog?=null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_payment, container, false)
        val bundle = this.arguments
        //To run network operations on a main thread or as an synchronous task.
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val sharedPrefer = requireContext().getSharedPreferences(
            LoginActivity().appPreference, Context.MODE_PRIVATE)
        userID = sharedPrefer?.getString(LoginActivity().userIdPreference, 0.toString())

        recyclerViewOrderDe = root.findViewById(R.id.recyclerViewOrderDe)
        txtmemberStoreName = root.findViewById(R.id.txtmemberStoreName)
        txtmemberAddress = root.findViewById(R.id.txtmemberAddress)
        txtmemberPhone = root.findViewById(R.id.txtmemberPhone)
        txtNameCus = root.findViewById(R.id.txtNameCus)
        txtdepos = root.findViewById(R.id.txtdepos)
        txtCountdown = root.findViewById(R.id.txtCountdown)
        textView170 = root.findViewById(R.id.textView170)
        txtTotalQuan = root.findViewById(R.id.txtTotalQuan)
        txtTotalPrice = root.findViewById(R.id.txtTotalPrice)
        txtHalfPrice = root.findViewById(R.id.txtHalfPrice)

        viewQuanDatelineOrderID(bundle?.get("orderID").toString())
        viewSum(bundle?.get("orderID").toString())
        if (Integer.valueOf(CheckDateline) > -3){
            txtCountdown?.text = "0"
            txtdepos?.text = viewOrderID(bundle?.get("orderID").toString())
        }else{
            txtCountdown?.text = (Integer.valueOf(CheckDateline)*(-1) - 3).toString()
            //txtdepos?.text = (Integer.valueOf(viewOrderID(bundle?.get("orderID").toString())) - (Integer.valueOf(viewOrderID(bundle?.get("orderID").toString())) * (0.1))).toString()
            txtdepos?.text = ((Integer.valueOf(TotalPrice)/2) - (Integer.valueOf(TotalPrice)*0.1)).toString()
        }
        txtendDate = root.findViewById(R.id.txtendDate)
        txtendDate?.text = viewOrderDateline(bundle?.get("orderID").toString())
        txtDate = root.findViewById(R.id.txtDate)
        txtDate?.text = ViewCreated(bundle?.get("orderID").toString())
        txtDataBut = root.findViewById(R.id.txtDataBut)
        txtDataBut?.text = ViewCreated(bundle?.get("orderID").toString())

        txtIDOrder = root.findViewById(R.id.txtIDOrder)
        txtIDOrder?.text = bundle?.get("orderID").toString()
        txtStatus = root.findViewById(R.id.txtStatus)
        btnComfirm = root.findViewById(R.id.btnComfirm)
        txtDateline = root.findViewById(R.id.txtDateline)
        txtDateline?.text = ViewDateline(bundle?.get("orderID").toString())

        viewUser(bundle?.get("orderID").toString())
        showDataListMat(bundle?.get("orderID").toString())
        textView170?.text == viewOrderIDtext_alert(bundle?.get("orderID").toString())
        txtStatus?.text = ViewStepStatus(bundle?.get("orderID").toString())

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

                    updateStatusOrder(bundle?.get("orderID").toString())
                    AddPayment(bundle?.get("orderID").toString())

                }, 0)


            }else{
                Toast.makeText(context, "แนบใบเสร็จการจ่ายเงิน", Toast.LENGTH_LONG).show()
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


    private fun viewSum(orderID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.SUMpriceTotalOrder_url) + orderID
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
                        TotalPrice = data.getString("TotalAllPrice")
                        txtTotalQuan?.text = data.getString("TotalAllQuan")
                        txtHalfPrice?.text = data.getString("HalfPrice")

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


    private fun AddPayment(orderID: String?) {

        var url: String = getString(R.string.root_url) + getString(R.string.AddPayment_url) + orderID
        val okHttpClient = OkHttpClient()

        Log.d("mytest",url)
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
        progressDialog?.dismiss()

        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.nav_host_fragment,OrderFragment())
        fragmentTransaction.commit()


    }

    private fun updateStatusOrder(orderID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderCancelCusMoney_url) + orderID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("stepStatusID", "14")
            .addFormDataPart("overdueMoney", txtdepos?.text.toString())
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

    private fun showDataListMat(orderID: String?) {
        val data = ArrayList<Data>()
        val url: String = getString(R.string.root_url) + getString(R.string.viewONEOrderUserProductID_url) + userID + "/" + orderID
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
                                item.getString("orderID")
                            )
                            )
                            recyclerViewOrderDe!!.adapter = DataAdapter(data)
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
        var orderID: String?

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
            holder.txtorderid.text = data.orderID
            holder.txtNameProduct.text = data.productName

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

            showDataListColor(holder.recyclerView2, userID, holder.txtproductID.text.toString(),data.orderID)

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


    private fun viewOrderIDtext_alert(orderID: String?): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewOrderID_url) + orderID
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

                        id = data.getString("text_alert")
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

    private fun viewOrderDateline(orderID: String?): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewOrderID_url) + orderID
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

    private fun viewOrderID(orderID: String?): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewOrderID_url) + orderID
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

                        id = data.getString("overdueMoney")
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

    private fun viewQuanDatelineOrderID(orderID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewQuanDatelineOrderID_url) + orderID
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

                        CheckDateline = data.getString("text_alert")
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
    private fun viewUser(orderID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewOrderIDOneUser_url) + orderID
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

    private fun ViewDateline(orderID: String?): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewOrderID_url) + orderID
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


    private fun ViewStepStatus(orderID: String?): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.ViewStepStatus_url) + orderID
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

                        id=data.getString("stepStatusName")

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
