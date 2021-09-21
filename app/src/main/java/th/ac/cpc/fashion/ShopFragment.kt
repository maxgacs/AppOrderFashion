package th.ac.cpc.fashion

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
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

class ShopFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var productID : String? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_shop, container, false)
        val bundle = this.arguments
        //To run network operations on a main thread or as an synchronous task.
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        recyclerView = root.findViewById(R.id.recyclerView)

            showidol(bundle?.get("mainProductTypeID").toString())

        return root
    }

    private fun showidol(mainProductTypeID: String?) {
        val data = ArrayList<Data>()
        val url: String = getString(R.string.root_url) + getString(R.string.viewProductType_url) + mainProductTypeID
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
                                item.getString("productImage"),
                                item.getString("productID"),
                                item.getString("productName")

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
        var imageFileName: String,var productID: String,var productName: String
    )

    internal inner class DataAdapter(private val list: List<Data>) :
        RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_product,
                parent, false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val data = list[position]
            holder.data = data
            var url = getString(R.string.root_url) + getString(R.string.product_image_url) + data.imageFileName
            Picasso.get().load(url).into(holder.imageButton3)



            holder.imageButton3.setOnClickListener {
                productID = data.productID
                val bundle = Bundle()
                bundle.putString("productID", productID)
                val fm = ProductDetailFragment()
                fm.arguments = bundle;

                val fragmentTransaction = requireActivity().
                supportFragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.nav_host_fragment, fm)
                fragmentTransaction.commit()
            }
            holder.txtNameProduct.text = data.productName
            holder.itemProductMat.text = viewChoose_Mat_materialTypeName(data.productID).toString()
            holder.itemProductCount.text = ViewProductCount(data.productID).toString()


        }

        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var data: Data? = null
            var imageButton3: ImageView = itemView.findViewById(R.id.imageButton3)
            var txtNameProduct: TextView = itemView.findViewById(R.id.txtNameProduct)
            var itemProductMat: TextView = itemView.findViewById(R.id.itemProductMat)
            var itemProductCount: TextView = itemView.findViewById(R.id.itemProductCount)

        }
    }

    private fun ViewProductCount(orderID: String?): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewChoose_MatCOUNT_url) + orderID
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

                        id = data.getString("COUNT(*)")
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

private fun viewChoose_Mat_materialTypeName(orderID: String?): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewChoose_Mat_materialTypeName_url) + orderID
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

                        id = data.getString("materialTypeName")
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
