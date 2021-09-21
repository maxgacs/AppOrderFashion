package th.ac.cpc.fashion

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class HomeCusFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var mainProductTypeID : String? =null

    var orderID= ArrayList<String>()
    var orderIDCheck: String? =null
    var userID: String? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home_cus, container, false)

        //To run network operations on a main thread or as an synchronous task.
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val sharedPrefer = requireContext().getSharedPreferences(
                LoginActivity().appPreference, Context.MODE_PRIVATE)
        userID = sharedPrefer?.getString(LoginActivity().userIdPreference, 0.toString())

        recyclerView = root.findViewById(R.id.recyclerView)
        showidol()
        viewOrderText_alert()
        if (userID == "0"){


        }else{
            if (orderIDCheck == "1"){
                val builder1 = AlertDialog.Builder(requireActivity())
                builder1.setMessage("คุณมึออเดอร์ที่เราเลื่อนหรือยกเลิก")
                builder1.setNegativeButton(
                        "ตกลง"
                ) { dialog, id -> //dialog.cancel();
                    updateOrderText_alertStatus()
                }

                val alert11 = builder1.create()
                alert11.show()
            }
        }

        return root
    }

    private fun updateOrderText_alertStatus()
    {
        for (i in 0 until orderID.size) {

        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderText_alertStatus_url) + orderID[i]
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("text_alertStatus", "0")
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
    }

    private fun viewOrderText_alert() {

        val url: String = getString(R.string.root_url) + getString(R.string.viewOrderText_alert_url) + userID
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

                            orderID.add(item.getString("orderID"))
                            orderIDCheck = "1"

                        }
                    } else {
                        orderIDCheck = "0"
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

    private fun showidol() {
        val data = ArrayList<Data>()
        val url: String = getString(R.string.root_url) + getString(R.string.viewMainProductType_url)
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
                                    item.getString("mainProductTypeImage"),
                                    item.getString("mainProductTypeID")

                            )
                            )
                            recyclerView!!.adapter = DataAdapter(data)
                        }
                    } else {
                        Toast.makeText(context, "ลองอีกครั้ง.",
                                Toast.LENGTH_LONG).show()
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

    internal class Data(
            var imageFileName: String,var mainProductTypeID: String
    )

    internal inner class DataAdapter(private val list: List<Data>) :
            RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_main_product,
                    parent, false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val data = list[position]
            holder.data = data
            var url = getString(R.string.root_url) + getString(R.string.main_product_image_url) + data.imageFileName
            Picasso.get().load(url).into(holder.imageButton3)
            holder.imageButton3.setOnClickListener {

                mainProductTypeID = data.mainProductTypeID
                val bundle = Bundle()
                bundle.putString("mainProductTypeID", mainProductTypeID)
                val fm = ShopFragment()
                fm.arguments = bundle;

                val fragmentTransaction = requireActivity().
                supportFragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.nav_host_fragment, fm)
                fragmentTransaction.commit()

            }
        }

        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) :
                RecyclerView.ViewHolder(itemView) {
            var data: Data? = null
            var imageButton3: ImageView = itemView.findViewById(R.id.imageButton3)

        }
    }
}
