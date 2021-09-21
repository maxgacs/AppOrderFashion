package th.ac.cpc.fashion

import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.DecimalFormat
import java.text.NumberFormat

class DataEmpFragment : Fragment() {
    var userID: String? = null
    var recyclerView: RecyclerView? = null
    var empID : String? =null
    private val client = OkHttpClient()
    private val data = ArrayList<Data>()
    var orderID : String? =null
    var btnHOB: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_data_emp, container, false)

        val bundle = this.arguments
        //To run network operations on a main thread or as an synchronous task.
        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val sharedPrefer = requireContext().getSharedPreferences(
            LoginActivity().appPreference, Context.MODE_PRIVATE)
        userID = sharedPrefer?.getString(LoginActivity().userIdPreference, 0.toString())

        //List data
        recyclerView = root.findViewById(R.id.recyclerViewDataEmp)
        showDataList()
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {

            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.nav_host_fragment,HomeAdminFragment())
            fragmentTransaction.commit()
        }
        callback.isEnabled

        return root
    }

    private fun showDataList() {
        val data = ArrayList<Data>()
        val url: String = getString(R.string.root_url) + getString(R.string.viewWaitingEmp_url)
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
                                item.getString("empID"),
                                item.getString("empName"),
                                item.getString("nameJob"),
                                item.getString("updated_at"),
                                item.getString("empImage")
                            )
                            )
                            recyclerView!!.adapter = DataAdapter(data)
                        }
                    } else {
                        Toast.makeText(context, "ยังไม่มีพนักงานสมัครสมาชิก.",
                            Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) { e.printStackTrace() }
            } else { response.code }
        } catch (e: IOException) { e.printStackTrace() }
    }


    internal class Data(
        var empID: String, var empName: String, var nameJob: String, var updated_at: String, var image: String
    )

    internal inner class DataAdapter(private val list: List<Data>) :
        RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_waiting_for_approval,
                parent, false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val data = list[position]
            holder.data = data
            var url = getString(R.string.root_url) +
                    getString(R.string.user_image_url) + data.image
            Picasso.get().load(url).into(holder.imageView)
            holder.txtempName.text = data.empName
            holder.txtnameJob.text = data.nameJob
            holder.txtupdated_at.text = data.updated_at

            holder.button.setOnClickListener {
                empID = data.empID
                val bundle = Bundle()
                bundle.putString("empID", empID)
                val fm = DataEmpWaitingFragment()
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
            var txtempName: TextView = itemView.findViewById(R.id.txtempName)
            var txtnameJob: TextView = itemView.findViewById(R.id.txtnameJob)
            var txtupdated_at: TextView = itemView.findViewById(R.id.txtupdated_at)
            var button: Button = itemView.findViewById(R.id.button)
            var imageView: ImageView = itemView.findViewById(R.id.imageView2)

        }

    }
}