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

class SalaryEmpFragment : Fragment() {

    var userID: String? = null

    var recyclerViewDataOrder: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_salary_emp, container, false)

        //To run network operations on a main thread or as an synchronous task.
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val sharedPrefer = requireContext().getSharedPreferences(
            LoginEmpActivity().appPreference, Context.MODE_PRIVATE)
        userID = sharedPrefer?.getString(LoginEmpActivity().userIdPreference, 0.toString())

        recyclerViewDataOrder = root.findViewById(R.id.recyclerViewDataOrder)
        viewSalaryEmpID()
        return root
    }


    private fun viewSalaryEmpID() {
        val data = ArrayList<Data>()
        val url: String = getString(R.string.root_url) + getString(R.string.viewSalaryEmpID_url) + userID
        val okHttpClient = OkHttpClient()
        Log.d("mytest1", url)
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
                            item.getString("salaryID"),
                            item.getString("orderID"),
                            item.getString("Salary"),
                            item.getString("statusSalary")

                        )
                        )
                        recyclerViewDataOrder!!.adapter = DataAdapter(data)
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
        var salaryID: String, var orderID: String,var Salary: String,var statusSalary: String
    )

    internal inner class DataAdapter(private val list: List<Data>) :
        RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_salary,
                parent, false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val data = list[position]
            holder.data = data
            holder.OrderName.text = data.orderID
            holder.salaryPrice.text = data.Salary
            if (data.statusSalary == "0"){
                holder.dateOrderFinish.text = "ยังไม่โอน"
            }else if (data.statusSalary == "1"){
                holder.dateOrderFinish.text = "โอนแล้ว"
            }

            holder.button.setOnClickListener {

                    val bundle = Bundle()
                    bundle.putString("orderID", data.orderID)
                    bundle.putString("salaryID", data.salaryID)
                    val fm = SalaryOrderEmpFragment()
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
            var OrderName: TextView = itemView.findViewById(R.id.OrderName)
            var salaryPrice: TextView = itemView.findViewById(R.id.salaryPrice)
            var dateOrderFinish: TextView = itemView.findViewById(R.id.dateOrderFinish)
            var button: Button = itemView.findViewById(R.id.button)

        }
    }
}
