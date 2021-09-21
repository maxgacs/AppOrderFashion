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

class DataEmpSalaryFragment : Fragment() {

    var userID: String? = null
    var recyclerViewDataEmp: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_data_emp_salary, container, false)

        //To run network operations on a main thread or as an synchronous task.
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val sharedPrefer = requireContext().getSharedPreferences(
            LoginEmpActivity().appPreference, Context.MODE_PRIVATE)
        userID = sharedPrefer?.getString(LoginEmpActivity().userIdPreference, 0.toString())

        recyclerViewDataEmp = root.findViewById(R.id.recyclerViewDataEmp)
        viewSalary()
        return root
    }


    private fun viewSalary() {
        val data = ArrayList<Data>()
        val url: String = getString(R.string.root_url) + getString(R.string.viewSalaryStatusOrder2_url)
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
                                    item.getString("empName"),
                                    item.getString("nameEmpType"),
                                    item.getString("empImage")

                            )
                            )
                            recyclerViewDataEmp!!.adapter = DataAdapter(data)
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
        var salaryID: String, var orderID: String,var Salary: String,var empName: String,var nameEmpType: String,var empImage: String
    )

    internal inner class DataAdapter(private val list: List<Data>) :
        RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_emp,
                parent, false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val data = list[position]
            holder.data = data
            var url = getString(R.string.root_url) + getString(R.string.user_image_url) + data.empImage
            Picasso.get().load(url).into(holder.imageView2)
            holder.txtempName.text = data.empName
            holder.txtnameJob.text = data.nameEmpType
            holder.orderName.text = data.orderID
            holder.salaryPrice2.text = data.Salary


            holder.button.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("orderID", data.orderID)
                    bundle.putString("salaryID", data.salaryID)
                    val fm = SalaryOrderManaFragment()
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
            var imageView2: ImageView = itemView.findViewById(R.id.imageView2)
            var txtempName: TextView = itemView.findViewById(R.id.txtempName)
            var txtnameJob: TextView = itemView.findViewById(R.id.txtnameJob)
            var orderName: TextView = itemView.findViewById(R.id.orderName)
            var salaryPrice2: TextView = itemView.findViewById(R.id.salaryPrice2)
            var button: Button = itemView.findViewById(R.id.button)

        }
    }
}
