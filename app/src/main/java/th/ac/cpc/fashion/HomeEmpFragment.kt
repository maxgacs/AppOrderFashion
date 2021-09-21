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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class HomeEmpFragment : Fragment() {

    var userID: String? = null
    var empTypeID: String? = null
    var SOrder1: String = "0"
    var OrderID1: String = "0"

    var SOrder2: String = "0"
    var OrderID2: String = "0"

    var SOrder3: String = "0"
    var OrderID3: String = "0"

    var SOrder4: String = "0"
    var OrderID4: String = "0"
    var recyclerViewDataOrder: RecyclerView? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home_emp, container, false)

        //To run network operations on a main thread or as an synchronous task.
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val sharedPrefer = requireContext().getSharedPreferences(
                LoginEmpActivity().appPreference, Context.MODE_PRIVATE)
        userID = sharedPrefer?.getString(LoginEmpActivity().userIdPreference, 0.toString())

        val swipeRefreshLayout = root.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)

        recyclerViewDataOrder = root.findViewById(R.id.recyclerViewDataOrder)

        viewEmpType()
        swipeRefreshLayout.setOnRefreshListener{

        viewEmpType()

        if(empTypeID == "1"){
            viewOrderStepStatusIDWaitingASC1()
            if (SOrder1 == "0"){

                updateSalaryLogin1()
                updateOrderComfirmLogin1()
                updateEmpStat1()
            }

        }else if(empTypeID == "2"){
            viewOrderStepStatusIDWaitingASC2()
            if (SOrder2 == "0"){

                updateSalaryLogin2()
                updateOrderComfirmLogin2()
                updateEmpStat1()
            }
        }else if(empTypeID == "3"){
            viewOrderStepStatusIDWaitingASC3()
            if (SOrder3 == "0"){

                updateSalaryLogin3()
                //updateOrderComfirmLogin3()
                updateEmpStat1()
            }
        }else if(empTypeID == "4"){
            viewOrderStepStatusIDWaitingASC4()
            if (SOrder4 == "0"){

                updateSalaryLogin4()
                updateOrderComfirmLogin4()
                updateEmpStat1()
            }
        }

        viewOrder()

            swipeRefreshLayout.isRefreshing = false
        }
        viewOrder()
        return root
    }
    private fun viewSalaryWaiting4(): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewSalaryWaiting_url) + OrderID4 + "/0"
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

                        id = data.getString("salaryID")
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

    private fun updateSalaryLogin4()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateSalaryWaiting_url) + viewSalaryWaiting4().toString()
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("statusOrder", "0")
                .addFormDataPart("empID", userID.toString())
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

    private fun updateOrderComfirmLogin4()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderComfirm_url) + OrderID4
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("stepStatusID", "11")
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
    private fun viewSalaryWaiting3(): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewSalaryWaiting_url) + OrderID3 + "/0"
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

                        id = data.getString("salaryID")
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

    private fun updateSalaryLogin3()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateSalaryWaiting_url) + viewSalaryWaiting3().toString()
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("statusOrder", "0")
                .addFormDataPart("empID", userID.toString())
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

    private fun updateOrderComfirmLogin3()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderComfirm_url) + OrderID3
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("stepStatusID", "8")
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
    private fun viewSalaryWaiting2(): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewSalaryWaiting_url) + OrderID2 + "/0"
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

                        id = data.getString("salaryID")
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

    private fun updateSalaryLogin2()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateSalaryWaiting_url) + viewSalaryWaiting2().toString()
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("statusOrder", "0")
                .addFormDataPart("empID", userID.toString())
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

    private fun updateOrderComfirmLogin2()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderComfirm_url) + OrderID2
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("stepStatusID", "5")
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

    private fun viewSalaryWaiting1(): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewSalaryWaiting_url) + OrderID1 + "/0"
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

                        id = data.getString("salaryID")
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

    private fun updateSalaryLogin1()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateSalaryWaiting_url) + viewSalaryWaiting1().toString()
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("statusOrder", "0")
                .addFormDataPart("empID", userID.toString())
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

    private fun updateOrderComfirmLogin1()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateOrderComfirm_url) + OrderID1
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("stepStatusID", "2")
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

    private fun updateEmpStat1()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateEmpStat_url) + userID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("empStat", "1")
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

    private fun viewOrderStepStatusIDWaitingASC4()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewOrderStepStatusIDWaitingASC_url) + "10"
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

                        val data: JSONObject = res.getJSONObject(0)
                        OrderID4 = data.getString("orderID")

                    } else {
                        SOrder4 = ""
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

    /*private fun viewOrderStepStatusIDWaitingASC3()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewOrderStepStatusIDWaitingASC_url) + "7"
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

                        val data: JSONObject = res.getJSONObject(0)
                        OrderID3 = data.getString("orderID")

                    } else {
                        SOrder3 = ""
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
    }*/

    private fun viewOrderStepStatusIDWaitingASC3()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewOrderStepStatusIDWaitingASCType3_url)
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

                        val data: JSONObject = res.getJSONObject(0)
                        OrderID3 = data.getString("orderID")

                    } else {
                        SOrder3 = ""
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

    private fun viewOrderStepStatusIDWaitingASC2()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewOrderStepStatusIDWaitingASC_url) + "4"
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

                        val data: JSONObject = res.getJSONObject(0)
                        OrderID2 = data.getString("orderID")

                    } else {
                        SOrder2 = ""
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

    private fun viewOrderStepStatusIDWaitingASC1()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewOrderStepStatusIDWaitingASC_url) + "1"
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

                        val data: JSONObject = res.getJSONObject(0)
                        OrderID1 = data.getString("orderID")

                    } else {
                        SOrder1 = ""
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

    private fun viewEmpType()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewOneEmp_url) + userID
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
                .url(url)
                .get()
                .build()
        Log.d("mytest1", url)
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val data = JSONObject(response.body!!.string())
                    if (data.length() > 0) {

                        empTypeID = data.getString("empTypeID")



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



    private fun viewOrder() {
        val data = ArrayList<Data>()
        val url: String = getString(R.string.root_url) + getString(R.string.viewOrderEmpStepStatusASC_url) + empTypeID + "/" + userID
        val okHttpClient = OkHttpClient()
        Log.d("mytest1", url)
        val request: Request = Request.Builder().url(url).get().build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {
                            val item: JSONObject = res.getJSONObject(0)
                            data.add(Data(
                                    item.getString("salaryID"),
                                    item.getString("orderID"),
                                    item.getString("memberID"),
                                    item.getString("statusOrder"),
                                    item.getString("memberName"),
                                    item.getString("memberStoreName"),
                                    item.getString("memberImage")

                            )
                            )
                            recyclerViewDataOrder!!.adapter = DataAdapter(data)

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
        var salaryID: String, var orderID: String,var memberID: String,var statusOrder: String,var memberName: String,var memberStoreName: String,var memberImage: String
    )

    internal inner class DataAdapter(private val list: List<Data>) :
            RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_job,
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
            if (data.statusOrder == "0"){
                holder.stepStatus.text = "รอตรวจสอบ"

            }else if (data.statusOrder == "1"){
                holder.stepStatus.text = "คุณกำลังทำงานนี้"
            }

            holder.button.setOnClickListener {
                if (data.statusOrder == "0"){
                    val bundle = Bundle()
                    bundle.putString("orderID", data.orderID)
                    bundle.putString("salaryID", data.salaryID)
                    val fm = StepEmpOrderFragment()
                    fm.arguments = bundle;

                    val fragmentTransaction = requireActivity().
                    supportFragmentManager.beginTransaction()
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.replace(R.id.nav_host_fragment, fm)
                    fragmentTransaction.commit()
                }else if(data.statusOrder == "1"){
                    val bundle = Bundle()
                    bundle.putString("orderID", data.orderID)
                    bundle.putString("salaryID", data.salaryID)
                    val fm = StepEmpWrokOrderFragment()
                    fm.arguments = bundle;

                    val fragmentTransaction = requireActivity().
                    supportFragmentManager.beginTransaction()
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.replace(R.id.nav_host_fragment, fm)
                    fragmentTransaction.commit()
                }
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
            var button: Button = itemView.findViewById(R.id.button)

        }
    }
}
