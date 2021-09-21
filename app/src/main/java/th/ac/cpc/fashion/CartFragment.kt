package th.ac.cpc.fashion

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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

class CartFragment : Fragment() {
    var userID: String? = null

    var recyclerViewCart: RecyclerView? = null
    var btnComfirm: Button? = null
    var txtPricePay: TextView? = null
    var txtPricePay2: TextView? = null

    var sumQuan: String? = "0"
    var sumPrice: Float = "0".toFloat()


    var QuanF: String? = "0"
    var QuanS: String? = "0"
    var QuanM: String? = "0"
    var QuanL: String? = "0"
    var QuanXL: String? = "0"
    var Quan2XL: String? = "0"
    var Quan3XL: String? = "0"
    var Quan4XL: String? = "0"
    var Quan5XL: String? = "0"

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_cart, container, false)

        val bundle = this.arguments

        val policy =
                StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val sharedPrefer = requireContext().getSharedPreferences(
                LoginActivity().appPreference, Context.MODE_PRIVATE)
        userID = sharedPrefer?.getString(LoginActivity().userIdPreference, 0.toString())

        recyclerViewCart = root.findViewById(R.id.recyclerViewCart)
        txtPricePay = root.findViewById(R.id.txtPricePay)
        txtPricePay2 = root.findViewById(R.id.txtPricePay2)

        if(userID == "0"){
            val editor = sharedPrefer.edit()
            editor.clear() // ทำการลบข้อมูลทั้งหมดจาก preferences

            editor.commit() // ยืนยันการแก้ไข preferences

            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }else{
            showDataListMat(userID)
            TotalAllPrice(userID)
        }
        btnComfirm = root.findViewById(R.id.btnComfirm)
        btnComfirm?.setOnClickListener {

            if (txtPricePay2?.text == "null"){
                Toast.makeText(context, "คุณยังไม่มีสินค้าในตะกร้า",
                        Toast.LENGTH_LONG).show()
            }else{
                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.nav_host_fragment,PayDepositFragment())
                fragmentTransaction.commit()
            }
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {

            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.nav_host_fragment,HomeCusFragment())
            fragmentTransaction.commit()
        }
        callback.isEnabled

        return root
    }

    private fun TotalAllPrice(userID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.SUMpriceTotal_url) + userID
        Log.d("tagshow",url)
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

                        txtPricePay2?.text = data.getString("TotalAllPrice")
                        txtPricePay?.text = data.getString("HalfPrice")
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
    //show a data list
    private fun showDataListMat(userID: String?) {
        val data = ArrayList<Data>()
        val url: String = getString(R.string.root_url) + getString(R.string.viewCartUser_url) + userID
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

                                    item.getString("shopCartID"),
                                    item.getString("priceTotal"),
                                    item.getString("priceSizeF"),
                                    item.getString("priceSizeS"),
                                    item.getString("priceSizeM"),
                                    item.getString("priceSizeL"),
                                    item.getString("priceSizeXL"),
                                    item.getString("priceSize2XL"),
                                    item.getString("priceSize3XL"),
                                    item.getString("quanTotal"),
                                    item.getString("quanSizeF"),
                                    item.getString("quanSizeS"),
                                    item.getString("quanSizeM"),
                                    item.getString("quanSizeL"),
                                    item.getString("quanSizeXL"),
                                    item.getString("quanSize2XL"),
                                    item.getString("quanSize3XL"),
                                    item.getString("productName"),
                                    item.getString("productImage"),
                                    item.getString("productPriceSizeF"),
                                    item.getString("productPriceSizeS"),
                                    item.getString("productPriceSizeM"),
                                    item.getString("productPriceSizeL"),
                                    item.getString("productPriceSizeXL"),
                                    item.getString("productPriceSize2XL"),
                                    item.getString("productPriceSize3XL"),
                                    item.getString("materialColor")
                            )
                            )
                            recyclerViewCart!!.adapter = DataAdapter(data)
                        }
                    } else {
                        Toast.makeText(context, "คุณยังไม่มีสินค้าในตะกร้า",
                                Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) { e.printStackTrace() }
            } else { response.code }
        } catch (e: IOException) { e.printStackTrace() }
    }


    internal class Data(
            var idcart: String?,
            var priceTotal: String?,
            var pricesumF: String?,
            var pricesumS: String?,
            var pricesumM: String?,
            var pricesumL: String?,
            var pricesumXL: String?,
            var pricesum2XL: String?,
            var pricesum3XL: String?,
            var quanTotal: String?,
            var quansumF: String?,
            var quansumS: String?,
            var quansumM: String?,
            var quansumL: String?,
            var quansumXL: String?,
            var quansum2XL: String?,
            var quansum3XL: String?,
            var productName: String, var productImage: String,
            var productPriceF: String?,
            var productPriceS: String?,
            var productPriceM: String?,
            var productPriceL: String?,
            var productPriceXL: String?,
            var productPrice2XL: String?,
            var productPrice3XL: String?, var materialColor: String
    )

    internal inner class DataAdapter(private val list: List<Data>) :
            RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_cart,
                    parent, false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val data = list[position]
            holder.data = data
            var url = getString(R.string.root_url) + getString(R.string.product_image_url) + data.productImage
            Picasso.get().load(url).into(holder.imgImageFileName)
            holder.txtProductName.text = data.productName
            holder.txtColor.text = data.materialColor
            holder.txtColor.text = data.materialColor
            holder.txtQuan.text = data.quanTotal
            holder.txtPrice.text = data.priceTotal
            sumPrice += data.priceTotal?.toFloat()!!



            holder.btnDeleteCart.setOnClickListener {
                deletecart(data.idcart)
                Toast.makeText(context, "ลบ " + holder.txtProductName.text + " จากตะกร้า.", Toast.LENGTH_LONG).show()
                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.nav_host_fragment,CartFragment())
                fragmentTransaction.commit()
            }

        }


        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) :
                RecyclerView.ViewHolder(itemView) {
            var data: Data? = null
            var imgImageFileName: ImageView = itemView.findViewById(R.id.imgImageFileName)
            var txtProductName: TextView = itemView.findViewById(R.id.txtProductName)
            var txtColor: TextView = itemView.findViewById(R.id.txtColor)
            var txtPrice: TextView = itemView.findViewById(R.id.txtPrice)
            var txtQuan: TextView = itemView.findViewById(R.id.txtQuan)
            var btnDeleteCart: ImageButton = itemView.findViewById(R.id.btnDeleteCart)
        }
    }

    private fun deletecart(idcart: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.cartdelete_url) + idcart
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