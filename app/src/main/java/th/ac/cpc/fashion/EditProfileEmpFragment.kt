package th.ac.cpc.fashion

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class EditProfileEmpFragment : Fragment() {
    var userID: String? =null
    var editName: EditText? = null
    var editEmail: EditText? = null
    var editPhone: EditText? = null
    var editAddress: EditText? = null
    var UserShow: TextView? = null
    var txtChangePassword: TextView? = null

    var editradioMan: RadioButton? = null
    var editradioWoman: RadioButton? = null
    var imageFilePath: String? = null
    var imageViewSlip: ImageView? = null
    var btnUpdate: Button? = null
    var btnCancel: Button? = null
    var imagepro: ImageView? = null

    var file: File? = null


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_edit_profile_emp, container, false)

        val sharedPrefer = requireContext().getSharedPreferences(
                LoginActivity().appPreference, Context.MODE_PRIVATE)


        userID = sharedPrefer?.getString(LoginActivity().userIdPreference, null)

        //To run network operations on a main thread or as an synchronous task.
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val bundle = this.arguments
        //find to widgets on a layout


        //viewUser(userID )

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {

            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.nav_host_fragment,ProfileEmpFragment())
            fragmentTransaction.commit()
        }
        callback.isEnabled


        editName = root.findViewById(R.id.editName)

        editPhone = root.findViewById(R.id.editPhone)
        editAddress = root.findViewById(R.id.editAddress)
        editradioMan = root.findViewById(R.id.editradioMan)
        editradioWoman = root.findViewById(R.id.editradioWoman)
        UserShow = root.findViewById(R.id.UserShow)
        txtChangePassword = root.findViewById(R.id.txtChangePassword)
        /*txtChangePassword?.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.nav_host_fragment,PasswordFragment())
            fragmentTransaction.commit()
        }*/

        btnCancel = root.findViewById(R.id.btnCa)
        btnCancel?.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.nav_host_fragment,ProfileEmpFragment())
            fragmentTransaction.commit()
        }


        imageViewSlip = root.findViewById(R.id.imageViewSlip)
        imagepro = root.findViewById(R.id.imagepro)
        /*imagepro?.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.nav_host_fragment,EditImageFragment())
            fragmentTransaction.commit()
        }*/


        btnUpdate = root.findViewById(R.id.btnC)
        btnUpdate?.setOnClickListener {
            update()
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.nav_host_fragment,ProfileEmpFragment())
            fragmentTransaction.commit()
        }
        permission()
        viewUser()
        return root

    }

    private fun permission()
    {
        //Set permission to open camera and access a directory
        if ((ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE), 225)
        }
    }


    private fun createImageFile(): File? {
        // Create an image file name
        val storageDir = File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "")
        val image = File.createTempFile(
                SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()), ".png",
                storageDir)
        imageFilePath = image.absolutePath
        return image
    }
    private fun viewUser()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.viewOneEmp_url) + userID
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
                                getString(R.string.user_image_url) +
                                data.getString("empImage")

                        Picasso.get().load(imgUrl).into(imagepro)
                        editName?.setText(data.getString("empName"))
                        UserShow?.text = data.getString("empUsername")
                        editEmail?.setText(data.getString("empEmail"))
                        editPhone?.setText(data.getString("empPhone"))
                        editAddress?.setText(data.getString("empAddress"))


                        if(data.getString("empGender")=="1"){
                            editradioMan?.isChecked = true
                        }else{
                            editradioWoman?.isChecked = true
                        }

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

    private fun update()
    {
        var url: String = getString(R.string.root_url) + getString(R.string.updateEmp_url) + userID
        val okHttpClient = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("empName", editName?.text.toString())
                .addFormDataPart("empUsername", UserShow?.text.toString())
                .addFormDataPart("empEmail", editEmail?.text.toString())
                .addFormDataPart("empPhone", editPhone?.text.toString())
                .addFormDataPart("empAddress", editAddress?.text.toString())
                .addFormDataPart("empGender", if (editradioWoman!!.isChecked) "0" else "1")
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
                        Toast.makeText(requireContext(), "สำเร็จ", Toast.LENGTH_LONG).show()
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


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && null != intent) {
            val uri = intent.data
            file = File(getFilePath(uri))
            val bitmap: Bitmap
            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                //show image
                imagepro?.setImageBitmap(bitmap)
                imagepro?.setImageURI(uri)
                imagepro?.visibility = View.VISIBLE
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            val imageUri = Uri.parse("file:$imageFilePath")
            file = File(imageUri.path)
            try {
                val ims: InputStream = FileInputStream(file)
                var imageBitmap = BitmapFactory.decodeStream(ims)
                imageBitmap = resizeImage(imageBitmap, 1024, 1024) //resize image
                imageBitmap = resolveRotateImage(imageBitmap, imageFilePath!!) //Resolve auto rotate image

                //show image
                imagepro?.setImageBitmap(imageBitmap)
                imagepro?.visibility = View.VISIBLE
                getFileName(imageUri)

            } catch (e: FileNotFoundException) {
                return
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun getFilePath(uri: Uri?): String? {
        var path = ""
        val wholeID = DocumentsContract.getDocumentId(uri)
        // Split at colon, use second item in the arraygetDocumentId(uri)
        val id = wholeID.split(":".toRegex()).toTypedArray()[1]
        val column = arrayOf(MediaStore.Images.Media.DATA)
        // where id is equal to
        val sel = MediaStore.Images.Media._ID + "=?"
        val cursor = requireActivity().contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, arrayOf(id), null)
        var columnIndex = 0
        if (cursor != null) {
            columnIndex = cursor.getColumnIndex(column[0])
            if (cursor.moveToFirst()) {
                path = cursor.getString(columnIndex)
            }
            cursor.close()
        }
        return path
    }

    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = requireActivity().contentResolver.query(
                    uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(
                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    private fun resizeImage(bm: Bitmap?, newWidth: Int, newHeight: Int): Bitmap? {
        val width = bm!!.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP
        val resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false)
        bm.recycle()
        return resizedBitmap
    }

    private fun resolveRotateImage(bitmap: Bitmap?, photoPath: String): Bitmap? {
        val ei = ExifInterface(photoPath)
        val orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED)
        var rotatedBitmap: Bitmap? = null
        rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }
        return rotatedBitmap
    }

    private fun rotateImage(source: Bitmap?, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source!!, 0, 0, source.width, source.height,
                matrix, true)
    }

}