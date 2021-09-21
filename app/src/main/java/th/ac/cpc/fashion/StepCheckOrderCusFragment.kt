package th.ac.cpc.fashion

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class StepCheckOrderCusFragment : Fragment() {
    var userID: String? = null

    var txtStatus: TextView? = null
    var txttext_alert: TextView? = null
    var btnBack: Button? = null
    var txtDateline: TextView? = null
    var txtIDOrder: TextView? = null
    var txtdataBuy: TextView? = null
    var txtTotalQuan: TextView? = null
    var txtTotalPrice: TextView? = null
    var txtdepos: TextView? = null
    var imageViewCompensation: ImageView? = null
    var recyclerViewOrderDe: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_step_check_order_cus, container, false)
        val bundle = this.arguments
        //To run network operations on a main thread or as an synchronous task.
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val sharedPrefer = requireContext().getSharedPreferences(
                LoginActivity().appPreference, Context.MODE_PRIVATE
        )
        userID = sharedPrefer?.getString(LoginActivity().userIdPreference, 0.toString())

        recyclerViewOrderDe = root.findViewById(R.id.recyclerViewOrderDe)
        txtStatus = root.findViewById(R.id.txtStatus)
        btnBack = root.findViewById(R.id.btnBack)
        txttext_alert = root.findViewById(R.id.txttext_alert)
        txtdataBuy = root.findViewById(R.id.txtdataBuy)
        txtdataBuy?.text = ViewCreated(bundle?.get("orderID").toString())
        txtIDOrder = root.findViewById(R.id.txtIDOrder)
        txtIDOrder?.text = bundle?.get("orderID").toString()
        txtDateline = root.findViewById(R.id.txtDateline)
        txtDateline?.text = ViewDateline(bundle?.get("orderID").toString())

        txtStatus?.text = ViewStepStatus(bundle?.get("orderID").toString())
        txttext_alert?.text = ViewText_Alert(bundle?.get("orderID").toString())
        if (txttext_alert?.text == "null"){
            txttext_alert?.text = "หากเลื่อนหรือยกเลิกจะแจ้งที่นี่"
        }else{
            txttext_alert?.text = ViewText_Alert(bundle?.get("orderID").toString())
        }

        imageViewCompensation = root.findViewById(R.id.imageViewCompensation)
        txtTotalQuan = root.findViewById(R.id.txtTotalQuan)
        txtTotalPrice = root.findViewById(R.id.txtTotalPrice)
        txtdepos = root.findViewById(R.id.txtdepos)
        viewSum(bundle?.get("orderID").toString())
        viewOrderID(bundle?.get("orderID").toString())
        showDataListMat(bundle?.get("orderID").toString())

        btnBack?.setOnClickListener {

            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.nav_host_fragment,OrderFragment())
            fragmentTransaction.commit()
        }
        return root
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


    private fun viewSum(userID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.SUMpriceTotalOrder_url) + userID
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

                        var url = getString(R.string.root_url) + getString(R.string.salarycompensation_image_url) + data.getString("imageCompensation")
                        Picasso.get().load(url).into(imageViewCompensation)


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


    private fun ViewText_Alert(orderID: String?): String?
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

                        id=data.getString("text_alert")

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

}
