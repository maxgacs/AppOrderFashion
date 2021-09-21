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

class ProductDetailFragment : Fragment() {
    var userID: String? = null

    var recyclerView: RecyclerView? = null

    var txtname: TextView? = null
    var txtproductSold: TextView? = null
    var txtPricesizeF: TextView? = null
    var txtPricesizeS: TextView? = null
    var txtPricesizeM: TextView? = null
    var txtPricesizeL: TextView? = null
    var txtPricesizeXL: TextView? = null
    var txtPricesize2XL: TextView? = null
    var txtPricesize3XL: TextView? = null
    var txtQuansizeF: EditText? = null
    var txtQuansizeS: EditText? = null
    var txtQuansizeM: EditText? = null
    var txtQuansizeL: EditText? = null
    var txtQuansizeXL: EditText? = null
    var txtQuansize2XL: EditText? = null
    var txtQuansize3XL: EditText? = null
    var btnSum: Button? = null
    var btnComfirm: Button? = null

    var txtTotalQuan: TextView? = null
    var txtTotalPrice: TextView? = null
    var imageProduct: ImageView? = null

    var spinnerColor: Spinner? = null
    private var Color = java.util.ArrayList<material>()

    var sumF: String? = "0"
    var sumS: String? = "0"
    var sumM: String? = "0"
    var sumL: String? = "0"
    var sumXL: String? = "0"
    var sum2XL: String? = "0"
    var sum3XL: String? = "0"

    var MATSize: String? = "0"
    var sumPriceF: String? = "0"
    var sumPriceS: String? = "0"
    var sumPriceM: String? = "0"
    var sumPriceL: String? = "0"
    var sumPriceXL: String? = "0"
    var sumPrice2XL: String? = "0"
    var sumPrice3XL: String? = "0"

    var MatID:String?=null
    var MatQu:String?=null
    var MatQuan:String?=null

    var sizeRateF:String?=null
    var sizeRateS:String?=null
    var sizeRateM:String?=null
    var sizeRateL:String?=null
    var sizeRateXL:String?=null
    var sizeRate2XL:String?=null
    var sizeRate3XL:String?=null

    var TotalPrice: String? = "0"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_product_detail, container, false)

        val bundle = this.arguments

        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val sharedPrefer = requireContext().getSharedPreferences(
            LoginActivity().appPreference, Context.MODE_PRIVATE)
        userID = sharedPrefer?.getString(LoginActivity().userIdPreference, 0.toString())

        recyclerView = root.findViewById(R.id.recyclerView)
        txtname = root.findViewById(R.id.txtname)
        txtproductSold = root.findViewById(R.id.txtproductSold)
        txtPricesizeF = root.findViewById(R.id.txtPricesizeF)
        txtPricesizeS = root.findViewById(R.id.txtPricesizeS)
        txtPricesizeM = root.findViewById(R.id.txtPricesizeM)
        txtPricesizeL = root.findViewById(R.id.txtPricesizeL)
        txtPricesizeXL = root.findViewById(R.id.txtPricesizeXL)
        txtPricesize2XL = root.findViewById(R.id.txtPricesize2XL)
        txtPricesize3XL = root.findViewById(R.id.txtPricesize3XL)
        txtQuansizeF = root.findViewById(R.id.txtQuansizeF)
        txtQuansizeS = root.findViewById(R.id.txtQuansizeS)
        txtQuansizeM = root.findViewById(R.id.txtQuansizeM)
        txtQuansizeL = root.findViewById(R.id.txtQuansizeL)
        txtQuansizeXL = root.findViewById(R.id.txtQuansizeXL)
        txtQuansize2XL = root.findViewById(R.id.txtQuansize2XL)
        txtQuansize3XL = root.findViewById(R.id.txtQuansize3XL)

        txtTotalQuan = root.findViewById(R.id.txtTotalQuan)
        txtTotalPrice = root.findViewById(R.id.txtTotalPrice)
        btnSum = root.findViewById(R.id.btnSum)
        btnSum?.setOnClickListener {

            sumF = txtQuansizeF?.text.toString()
            sumS = txtQuansizeS?.text.toString()
            sumM = txtQuansizeM?.text.toString()
            sumL = txtQuansizeL?.text.toString()
            sumXL = txtQuansizeXL?.text.toString()
            sum2XL = txtQuansize2XL?.text.toString()
            sum3XL = txtQuansize3XL?.text.toString()


            if (txtQuansizeF?.text.toString().isEmpty()){
                sumF = "0"
            }
            if (Integer.valueOf(sumF) in 1..5){
                sumF = "6"
                txtQuansizeF?.text = Editable.Factory.getInstance().newEditable(sumF)
                Toast.makeText(context, "ต้องสั่ง 6 ชื้นขึ้นไป", Toast.LENGTH_LONG).show()
            }

            if (txtQuansizeS?.text.toString().isEmpty()){
                sumS = "0"
            }
            if (Integer.valueOf(sumS) in 1..5){
                sumS = "6"
                txtQuansizeS?.text = Editable.Factory.getInstance().newEditable(sumS)
                Toast.makeText(context, "ต้องสั่ง 6 ชื้นขึ้นไป", Toast.LENGTH_LONG).show()
            }

            if (txtQuansizeM?.text.toString().isEmpty()){
                sumM = "0"
            }
            if (Integer.valueOf(sumM) in 1..5){
                sumM = "6"
                txtQuansizeM?.text = Editable.Factory.getInstance().newEditable(sumM)
                Toast.makeText(context, "ต้องสั่ง 6 ชื้นขึ้นไป", Toast.LENGTH_LONG).show()
            }

            if (txtQuansizeL?.text.toString().isEmpty()){
                sumL = "0"
            }
            if (Integer.valueOf(sumL) in 1..5){
                sumL = "6"
                txtQuansizeL?.text = Editable.Factory.getInstance().newEditable(sumL)
                Toast.makeText(context, "ต้องสั่ง 6 ชื้นขึ้นไป", Toast.LENGTH_LONG).show()
            }

            if (txtQuansizeXL?.text.toString().isEmpty()){
                sumXL = "0"
            }
            if (Integer.valueOf(sumXL) in 1..5){
                sumXL = "6"
                txtQuansizeXL?.text = Editable.Factory.getInstance().newEditable(sumXL)
                Toast.makeText(context, "ต้องสั่ง 6 ชื้นขึ้นไป", Toast.LENGTH_LONG).show()
            }

            if (txtQuansize2XL?.text.toString().isEmpty()){
                sum2XL = "0"
            }
            if (Integer.valueOf(sum2XL) in 1..5){
                sum2XL = "6"
                txtQuansize2XL?.text = Editable.Factory.getInstance().newEditable(sum2XL)
                Toast.makeText(context, "ต้องสั่ง 6 ชื้นขึ้นไป", Toast.LENGTH_LONG).show()
            }

            if (txtQuansize3XL?.text.toString().isEmpty()){
                sum3XL = "0"
            }
            if (Integer.valueOf(sum3XL) in 1..5){
                sum3XL = "6"
                txtQuansize3XL?.text = Editable.Factory.getInstance().newEditable(sum3XL)
                Toast.makeText(context, "ต้องสั่ง 6 ชื้นขึ้นไป", Toast.LENGTH_LONG).show()
            }



             txtTotalQuan?.text = (Integer.valueOf(sumF.toString()) + Integer.valueOf(sumS.toString()) +
                    Integer.valueOf(sumM.toString()) + Integer.valueOf(sumL.toString()) +
                    Integer.valueOf(sumXL.toString()) + Integer.valueOf(sum2XL.toString()) +
                    Integer.valueOf(sum3XL.toString())).toString()

            txtTotalPrice?.text = (((txtPricesizeF?.text.toString().toFloat()) * (sumF.toString()).toFloat()) +
                    ((txtPricesizeS?.text.toString().toFloat()) * (sumS.toString()).toFloat()) +
                    ((txtPricesizeM?.text.toString().toFloat()) * (sumM.toString()).toFloat()) +
                    ((txtPricesizeL?.text.toString().toFloat()) * (sumL.toString()).toFloat()) +
                    ((txtPricesizeXL?.text.toString().toFloat()) * (sumXL.toString()).toFloat()) +
                    ((txtPricesize2XL?.text.toString().toFloat()) * (sum2XL.toString()).toFloat()) +
                    ((txtPricesize3XL?.text.toString().toFloat()) * (sum3XL.toString()).toFloat())).toString()

            if(MatID == ""){
                Toast.makeText(context, "เลือกสีที่ต้องการ", Toast.LENGTH_LONG).show()
            }else{
                MatQuan = (sizeRateF.toString().toFloat() * sumF.toString().toFloat() +
                        sizeRateS.toString().toFloat() * sumS.toString().toFloat() +
                        sizeRateM.toString().toFloat() * sumM.toString().toFloat() +
                        sizeRateL.toString().toFloat() * sumL.toString().toFloat() +
                        sizeRateXL.toString().toFloat() * sumXL.toString().toFloat() +
                        sizeRate2XL.toString().toFloat() * sum2XL.toString().toFloat() +
                        sizeRate3XL.toString().toFloat() * sum3XL.toString().toFloat()).toString()
                if(MATSize.toString().toFloat() < MatQuan.toString().toFloat()){
                    Toast.makeText(context, "เรามีวัตถุดิบไม่เพียงพอ", Toast.LENGTH_LONG).show()
                }else{
                    if(Integer.valueOf(viewChoose_Mat_SpOne(bundle?.get("productID").toString())) < Integer.valueOf(txtTotalQuan?.text.toString())){
                        Toast.makeText(context, "เรามีวัตถุดิบไม่เพียงพอ", Toast.LENGTH_LONG).show()
                    }

                }

            }

        }

        btnComfirm = root.findViewById(R.id.btnComfirm)
        btnComfirm?.setOnClickListener {

            if(userID == "0"){
                val editor = sharedPrefer.edit()
                editor.clear() // ทำการลบข้อมูลทั้งหมดจาก preferences

                editor.commit() // ยืนยันการแก้ไข preferences

                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }else{

                sumF = txtQuansizeF?.text.toString()
                sumS = txtQuansizeS?.text.toString()
                sumM = txtQuansizeM?.text.toString()
                sumL = txtQuansizeL?.text.toString()
                sumXL = txtQuansizeXL?.text.toString()
                sum2XL = txtQuansize2XL?.text.toString()
                sum3XL = txtQuansize3XL?.text.toString()

                if (txtQuansizeF?.text.toString().isEmpty()){
                    sumF = "0"
                }
                if (Integer.valueOf(sumF) in 1..5){
                    sumF = "6"
                    txtQuansizeF?.text = Editable.Factory.getInstance().newEditable(sumF)
                    Toast.makeText(context, "ต้องสั่ง 6 ชื้นขึ้นไป", Toast.LENGTH_LONG).show()
                }

                if (txtQuansizeS?.text.toString().isEmpty()){
                    sumS = "0"
                }
                if (Integer.valueOf(sumS) in 1..5){
                    sumS = "6"
                    txtQuansizeS?.text = Editable.Factory.getInstance().newEditable(sumS)
                    Toast.makeText(context, "ต้องสั่ง 6 ชื้นขึ้นไป", Toast.LENGTH_LONG).show()
                }

                if (txtQuansizeM?.text.toString().isEmpty()){
                    sumM = "0"
                }
                if (Integer.valueOf(sumM) in 1..5){
                    sumM = "6"
                    txtQuansizeM?.text = Editable.Factory.getInstance().newEditable(sumM)
                    Toast.makeText(context, "ต้องสั่ง 6 ชื้นขึ้นไป", Toast.LENGTH_LONG).show()
                }

                if (txtQuansizeL?.text.toString().isEmpty()){
                    sumL = "0"
                }
                if (Integer.valueOf(sumL) in 1..5){
                    sumL = "6"
                    txtQuansizeL?.text = Editable.Factory.getInstance().newEditable(sumL)
                    Toast.makeText(context, "ต้องสั่ง 6 ชื้นขึ้นไป", Toast.LENGTH_LONG).show()
                }

                if (txtQuansizeXL?.text.toString().isEmpty()){
                    sumXL = "0"
                }
                if (Integer.valueOf(sumXL) in 1..5){
                    sumXL = "6"
                    txtQuansizeXL?.text = Editable.Factory.getInstance().newEditable(sumXL)
                    Toast.makeText(context, "ต้องสั่ง 6 ชื้นขึ้นไป", Toast.LENGTH_LONG).show()
                }

                if (txtQuansize2XL?.text.toString().isEmpty()){
                    sum2XL = "0"
                }
                if (Integer.valueOf(sum2XL) in 1..5){
                    sum2XL = "6"
                    txtQuansize2XL?.text = Editable.Factory.getInstance().newEditable(sum2XL)
                    Toast.makeText(context, "ต้องสั่ง 6 ชื้นขึ้นไป", Toast.LENGTH_LONG).show()
                }

                if (txtQuansize3XL?.text.toString().isEmpty()){
                    sum3XL = "0"
                }
                if (Integer.valueOf(sum3XL) in 1..5){
                    sum3XL = "6"
                    txtQuansize3XL?.text = Editable.Factory.getInstance().newEditable(sum3XL)
                    Toast.makeText(context, "ต้องสั่ง 6 ชื้นขึ้นไป", Toast.LENGTH_LONG).show()
                }


                txtTotalQuan?.text = (Integer.valueOf(sumF.toString()) + Integer.valueOf(sumS.toString()) +
                        Integer.valueOf(sumM.toString()) + Integer.valueOf(sumL.toString()) +
                        Integer.valueOf(sumXL.toString()) + Integer.valueOf(sum2XL.toString()) +
                        Integer.valueOf(sum3XL.toString())).toString()

                txtTotalPrice?.text = (((txtPricesizeF?.text.toString().toFloat()) * (sumF.toString()).toFloat()) +
                        ((txtPricesizeS?.text.toString().toFloat()) * (sumS.toString()).toFloat()) +
                        ((txtPricesizeM?.text.toString().toFloat()) * (sumM.toString()).toFloat()) +
                        ((txtPricesizeL?.text.toString().toFloat()) * (sumL.toString()).toFloat()) +
                        ((txtPricesizeXL?.text.toString().toFloat()) * (sumXL.toString()).toFloat()) +
                        ((txtPricesize2XL?.text.toString().toFloat()) * (sum2XL.toString()).toFloat()) +
                        ((txtPricesize3XL?.text.toString().toFloat()) * (sum3XL.toString()).toFloat())).toString()


                sumPriceF = ((txtPricesizeF?.text.toString().toFloat()) * (sumF.toString()).toFloat()).toString()
                sumPriceS = ((txtPricesizeS?.text.toString().toFloat()) * (sumS.toString()).toFloat()).toString()
                sumPriceM = ((txtPricesizeM?.text.toString().toFloat()) * (sumM.toString()).toFloat()).toString()
                sumPriceL = ((txtPricesizeL?.text.toString().toFloat()) * (sumL.toString()).toFloat()).toString()
                sumPriceXL = ((txtPricesizeXL?.text.toString().toFloat()) * (sumXL.toString()).toFloat()).toString()
                sumPrice2XL = ((txtPricesize2XL?.text.toString().toFloat()) * (sum2XL.toString()).toFloat()).toString()
                sumPrice3XL = ((txtPricesize3XL?.text.toString().toFloat()) * (sum3XL.toString()).toFloat()).toString()

                //AddOrder_Details(txtTotalPrice?.text.toString(), txtTotalQuan?.text.toString(), bundle?.get("productID").toString())

                if(txtTotalQuan?.text.toString() == "0"){
                    Toast.makeText(context, "ใส่จำนวณที่ต้องการ", Toast.LENGTH_LONG).show()
                }else{
                    if(MatID == ""){
                        Toast.makeText(context, "เลือกสีที่ต้องการ", Toast.LENGTH_LONG).show()
                    }else{
                        MatQuan = (sizeRateF.toString().toFloat() * sumF.toString().toFloat() +
                                sizeRateS.toString().toFloat() * sumS.toString().toFloat() +
                                sizeRateM.toString().toFloat() * sumM.toString().toFloat() +
                                sizeRateL.toString().toFloat() * sumL.toString().toFloat() +
                                sizeRateXL.toString().toFloat() * sumXL.toString().toFloat() +
                                sizeRate2XL.toString().toFloat() * sum2XL.toString().toFloat() +
                                sizeRate3XL.toString().toFloat() * sum3XL.toString().toFloat()).toString()
                        if(MATSize.toString().toFloat() < MatQuan.toString().toFloat()){
                            Toast.makeText(context, "เรามีวัตถุดิบไม่เพียงพอ", Toast.LENGTH_LONG).show()
                        }else{
                            if(Integer.valueOf(viewChoose_Mat_SpOne(bundle?.get("productID").toString()).toString()) < Integer.valueOf(txtTotalQuan?.text.toString())){
                                Toast.makeText(context, "เรามีวัตถุดิบไม่เพียงพอ", Toast.LENGTH_LONG).show()
                            }else{
                                AddCart(txtTotalPrice?.text.toString(), txtTotalQuan?.text.toString(), bundle?.get("productID").toString(), userID,
                                        sumPriceF,sumPriceS,sumPriceM,sumPriceL,sumPriceXL,sumPrice2XL,sumPrice3XL,
                                        sumF,sumS,sumM,sumL,sumXL,sum2XL,sum3XL)
                            }

                        }

                    }
                }



            }

        }

        imageProduct = root.findViewById(R.id.imageProduct)
        spinnerColor = root.findViewById(R.id.spinnerColor)


        viewProduct(bundle?.get("productID").toString())

        Color.add(material("","กรุณาเลือกสี",""))
        listColor(bundle?.get("productID").toString())
        val adapterBand = ArrayAdapter(
                requireContext(),android.R.layout.simple_spinner_item, Color)
        spinnerColor!!.adapter = adapterBand
        spinnerColor!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                val mat = spinnerColor!!.selectedItem as material
                MatID = mat.materialID
                MatQu = mat.materialQuan
                showDataListMat(bundle?.get("productID").toString(), mat.materialID)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //showDataListMatSp(bundle?.get("productID").toString())



        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {

            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.nav_host_fragment,HomeCusFragment())
            fragmentTransaction.commit()
        }
        callback.isEnabled

        return root
    }

    private fun viewChoose_Mat_SpOne(productID: String?): String?
    {
        var id :String?=null
        var url: String = getString(R.string.root_url) + getString(R.string.viewChoose_Mat_SpOne_url) + productID
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

                        id = data.getString("checkSP")
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

    private fun viewProduct(productID: String?)
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewProductOne_url) + productID
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

                        var imgUrl = getString(R.string.root_url) +
                                getString(R.string.product_image_url) +
                                data.getString("productImage")

                        Picasso.get().load(imgUrl).into(imageProduct)

                        txtname?.text = data.getString("productName")
                        txtproductSold?.text = data.getString("productSold")
                        txtPricesizeF?.text = data.getString("productPriceSizeF")
                        txtPricesizeS?.text = data.getString("productPriceSizeS")
                        txtPricesizeM?.text = data.getString("productPriceSizeM")
                        txtPricesizeL?.text = data.getString("productPriceSizeL")
                        txtPricesizeXL?.text = data.getString("productPriceSizeXL")
                        txtPricesize2XL?.text = data.getString("productPriceSize2XL")
                        txtPricesize3XL?.text = data.getString("productPriceSize3XL")


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
    private fun showDataListMat(productID: String?, MatID: String?) {
        val data = ArrayList<Data>()
        val url: String = getString(R.string.root_url) + getString(R.string.viewChoose_MatOne_url) + productID + "/" + MatID
        Log.d("tagshow",url)
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
                                    item.getString("materialImage"),
                                    item.getString("materialName"),
                                    item.getString("materialSize")
                            )
                            )
                            recyclerView!!.adapter = DataAdapter(data)
                            MATSize = item.getString("materialSize")
                            sizeRateF = item.getString("sizeRateF")
                            sizeRateS = item.getString("sizeRateS")
                            sizeRateM = item.getString("sizeRateM")
                            sizeRateL = item.getString("sizeRateL")
                            sizeRateXL = item.getString("sizeRateXL")
                            sizeRate2XL = item.getString("sizeRate2XL")
                            sizeRate3XL = item.getString("sizeRate3XL")
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
            var materialImage: String, var materialName: String, var materialSize: String
    )

    internal inner class DataAdapter(private val list: List<Data>) :
            RecyclerView.Adapter<DataAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_material,
                    parent, false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val data = list[position]
            holder.data = data
            var url = getString(R.string.root_url) + getString(R.string.product_image_url) + data.materialImage
            Picasso.get().load(url).into(holder.imageView8)
            holder.txtNameMaterial.text = data.materialName
            holder.txtStock.text = data.materialSize.toFloat().toString()

        }


        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) :
                RecyclerView.ViewHolder(itemView) {
            var data: Data? = null
            var imageView8: ImageView = itemView.findViewById(R.id.imageView8)
            var txtNameMaterial: TextView = itemView.findViewById(R.id.txtNameMaterial)
            var txtStock: TextView = itemView.findViewById(R.id.txtStock)
        }
    }

    private fun AddOrder_Details(TotalPrice: String?, TotalQuan: String?, productID: String?)
    {

        var url: String = getString(R.string.root_url) + getString(R.string.AddOrder_Details_url)
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = FormBody.Builder()

                .add("priceTotal", TotalPrice.toString())
                .add("priceSizeF", txtPricesizeF?.text.toString())
                .add("priceSizeS", txtPricesizeS?.text.toString())
                .add("priceSizeM", txtPricesizeM?.text.toString())
                .add("priceSizeL", txtPricesizeL?.text.toString())
                .add("priceSizeXL", txtPricesizeXL?.text.toString())
                .add("priceSize2XL", txtPricesize2XL?.text.toString())
                .add("priceSize3XL", txtPricesize3XL?.text.toString())
                .add("quanTotal", TotalQuan.toString())
                .add("quanSizeF", txtQuansizeF?.text.toString())
                .add("quanSizeS", txtQuansizeS?.text.toString())
                .add("quanSizeM", txtQuansizeM?.text.toString())
                .add("quanSizeL", txtQuansizeL?.text.toString())
                .add("quanSizeXL", txtQuansizeXL?.text.toString())
                .add("quanSize2XL", txtQuansize2XL?.text.toString())
                .add("quanSize3XL", txtQuansize3XL?.text.toString())
                .add("productID", productID.toString())
                .build()
        Log.d("txt","x2")
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
                        Log.d("txt","x3")
                        Toast.makeText(context, "แอดเข้าตะกร้าแล้ว", Toast.LENGTH_LONG).show()
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

    private fun AddCart(TotalPrice: String?, TotalQuan: String?, productID: String?, userID: String?,
                        sumPriceF : String?,sumPriceS : String?,sumPriceM : String?,sumPriceL : String?,
                        sumPriceXL : String?,sumPrice2XL : String?,sumPrice3XL : String?,
                        sumF : String?,sumS : String?,sumM : String?,sumL : String?,
                        sumXL : String?,sum2XL : String?,sum3XL : String?)
    {

        var url: String = getString(R.string.root_url) + getString(R.string.AddCart_url)
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = FormBody.Builder()

                .add("priceTotal", TotalPrice.toString())
                .add("priceSizeF", sumPriceF.toString())
                .add("priceSizeS", sumPriceS.toString())
                .add("priceSizeM", sumPriceM.toString())
                .add("priceSizeL", sumPriceL.toString())
                .add("priceSizeXL", sumPriceXL.toString())
                .add("priceSize2XL", sumPrice2XL.toString())
                .add("priceSize3XL", sumPrice3XL.toString())
                .add("quanTotal", TotalQuan.toString())
                .add("quanSizeF", sumF.toString())
                .add("quanSizeS", sumS.toString())
                .add("quanSizeM", sumM.toString())
                .add("quanSizeL", sumL.toString())
                .add("quanSizeXL", sumXL.toString())
                .add("quanSize2XL", sum2XL.toString())
                .add("quanSize3XL", sum3XL.toString())
                .add("productID", productID.toString())
                .add("memberID", userID.toString())
                .add("materialID", MatID.toString())
                .build()
        Log.d("txt","x2")
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
                        Log.d("txt","x3")
                        Toast.makeText(context, "แอดเข้าตะกร้าแล้ว", Toast.LENGTH_LONG).show()
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



    /*//show a data list
    private fun showDataListMatSp(productID: String?) {
        val data2 = ArrayList<Data2>()
        val url: String = getString(R.string.root_url) + getString(R.string.viewChoose_Mat_Sp_url) + productID
        //Log.d("tagshow",url)
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
                                    item.getString("material_spName")
                            )
                            )
                            recyclerView!!.adapter = DataAdapter2(data2)
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
            var txtMatSpName: String
    )

    internal inner class DataAdapter2(private val list: List<Data2>) :
            RecyclerView.Adapter<DataAdapter2.ViewHolder2>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder2 {
            val view: View = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_material_sp,
                    parent, false
            )
            return ViewHolder2(view)
        }

        override fun onBindViewHolder(holder: ViewHolder2, position: Int) {

            val data2 = list[position]
            holder.data2 = data2
            holder.txtMatSpName.text = data2.txtMatSpName
        }


        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder2(itemView: View) :
                RecyclerView.ViewHolder(itemView) {
            var data2: Data2? = null
            var txtMatSpName: TextView = itemView.findViewById(R.id.txtMatSpName)
        }
    }*/



    private fun listColor(productID: String?) {
        val urlProvince: String = getString(R.string.root_url) + getString(R.string.viewChoose_Mat_url) + productID
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url(urlProvince).get().build()
        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val res = JSONArray(response.body!!.string())
                    if (res.length() > 0) {
                        for (i in 0 until res.length()) {
                            val item: JSONObject = res.getJSONObject(i)
                            Color.add(
                                    material(
                                            item.getString("materialID"),
                                            item.getString("materialColor"),
                                            item.getString("materialSize")
                                    )
                            )
                        }
                    }
                } catch (e: JSONException) { e.printStackTrace() }
            } else { response.code }
        } catch (e: IOException) { e.printStackTrace() }
    }

    internal class material(var materialID: String, var materialColor: String, var materialQuan: String) {
        override fun toString(): String {
            return materialColor
        }

    }

}