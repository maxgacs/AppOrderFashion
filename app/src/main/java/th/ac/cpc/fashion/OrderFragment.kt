package th.ac.cpc.fashion

import android.content.Context
import android.content.Intent
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

class OrderFragment : Fragment() {
    var recyclerViewDataOrder: RecyclerView? = null
    var recyclerViewDataOrderPay: RecyclerView? = null
    var userID: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_order, container, false)

        //To run network operations on a main thread or as an synchronous task.
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val sharedPrefer = requireContext().getSharedPreferences(
                LoginActivity().appPreference, Context.MODE_PRIVATE)
        userID = sharedPrefer?.getString(LoginActivity().userIdPreference, 0.toString())

        recyclerViewDataOrder = root.findViewById(R.id.recyclerViewDataOrder)
        recyclerViewDataOrderPay = root.findViewById(R.id.recyclerViewDataOrderPay)

        if(userID == "0"){
            val editor = sharedPrefer.edit()
            editor.clear() // ทำการลบข้อมูลทั้งหมดจาก preferences

            editor.commit() // ยืนยันการแก้ไข preferences

            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }else{
            showidol()
            showStepStatusPay()

        }

        return root
    }

    private fun showidol() {
        val data = ArrayList<Data>()
        val url: String = getString(R.string.root_url) + getString(R.string.viewOrderUserStepStatusASC_url) + userID
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url(url).get().build()

        Log.d("mytest1", url)
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {
                        for (i in 0 until res.length()) {
                            val item: JSONObject = res.getJSONObject(i)
                            data.add(Data(
                                item.getString("orderID"),
                                item.getString("memberID"),
                                item.getString("stepStatusID"),
                                item.getString("memberName"),
                                item.getString("memberStoreName"),
                                item.getString("memberImage"),
                                item.getString("dateline"),
                                item.getString("stepStatusName")

                            )
                            )
                            recyclerViewDataOrder!!.adapter = DataAdapter(data)
                        }
                    } else {
                        Toast.makeText(context, "หืม..เหมือนว่าคุณยังไม่สั่งออเดอร์นะ!",
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
        var orderID: String,var memberID: String,var stepStatus: String,var memberName: String,
        var memberStoreName: String,var memberImage: String,var dateline: String,var stepStatusName: String
    )

    internal inner class DataAdapter(private val list: List<Data>) :
        RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_order,
                parent, false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val data = list[position]
            holder.data = data
            var url = getString(R.string.root_url) + getString(R.string.user_image_url) + data.memberImage
            Picasso.get().load(url).into(holder.memberImage)
            holder.memberName.text = data.memberName
            holder.memberStoreName.text = data.memberStoreName


                holder.stepStatus.text = data.stepStatusName

                holder.txtDateline.text = data.dateline


            holder.button.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("orderID", data.orderID)
                    val fm = StepCheckOrderCusFragment()
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
            var memberImage: ImageView = itemView.findViewById(R.id.memberImage)
            var memberStoreName: TextView = itemView.findViewById(R.id.memberStoreName)
            var memberName: TextView = itemView.findViewById(R.id.memberName)
            var stepStatus: TextView = itemView.findViewById(R.id.stepStatus)
            var txtDateline: TextView = itemView.findViewById(R.id.txtDateline)
            var button: Button = itemView.findViewById(R.id.button)

        }
    }

        private fun showStepStatusPay() {
            val data2 = ArrayList<Data2>()
            val url: String = getString(R.string.root_url) + getString(R.string.viewOrderUserStepStatusPayASC_url) + userID
            val okHttpClient = OkHttpClient()
            val request: Request = Request.Builder().url(url).get().build()

            Log.d("mytest1", url)
            try {
                val response = okHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    try {
                        val res = JSONArray(response.body!!.string())
                        if (res.length() > 0) {
                            for (i in 0 until res.length()) {
                                val item: JSONObject = res.getJSONObject(i)
                                data2.add(Data2(
                                        item.getString("orderID"),
                                        item.getString("memberID"),
                                        item.getString("stepStatusID"),
                                        item.getString("memberName"),
                                        item.getString("memberStoreName"),
                                        item.getString("memberImage"),
                                        item.getString("dateline"),
                                        item.getString("stepStatusName")

                                )
                                )
                                recyclerViewDataOrderPay!!.adapter = DataAdapter2(data2)
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

    internal class Data2(
            var orderID: String,var memberID: String,var stepStatus: String,var memberName: String,
            var memberStoreName: String,var memberImage: String,var dateline: String,var stepStatusName: String
    )

    internal inner class DataAdapter2(private val list2: List<Data2>) :
            RecyclerView.Adapter<DataAdapter2.ViewHolder2>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder2 {
            val view: View = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_order_pay,
                    parent, false
            )
            return ViewHolder2(view)
        }

        override fun onBindViewHolder(holder: ViewHolder2, position: Int) {

            val data = list2[position]
            holder.data = data
            var url = getString(R.string.root_url) + getString(R.string.user_image_url) + data.memberImage
            Picasso.get().load(url).into(holder.memberImage)
            holder.memberName.text = data.memberName
            holder.memberStoreName.text = data.memberStoreName


            holder.stepStatus.text = data.stepStatusName


            holder.button.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("orderID", data.orderID)
                val fm = PaymentFragment()
                fm.arguments = bundle;

                val fragmentTransaction = requireActivity().
                supportFragmentManager.beginTransaction()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.nav_host_fragment, fm)
                fragmentTransaction.commit()
            }
        }
        override fun getItemCount(): Int {
            return list2.size
        }

        internal inner class ViewHolder2(itemView: View) :
                RecyclerView.ViewHolder(itemView) {
            var data: Data2? = null
            var memberImage: ImageView = itemView.findViewById(R.id.memberImage)
            var memberStoreName: TextView = itemView.findViewById(R.id.memberStoreName)
            var memberName: TextView = itemView.findViewById(R.id.memberName)
            var stepStatus: TextView = itemView.findViewById(R.id.stepStatus)
            var button: Button = itemView.findViewById(R.id.button)
        }
    }


}
