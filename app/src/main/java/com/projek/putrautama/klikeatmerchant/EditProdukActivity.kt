package com.projek.putrautama.klikeatmerchant

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_produk.*
import kotlinx.android.synthetic.main.bottom_sheet_kategori.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger

class EditProdukActivity : AppCompatActivity(),View.OnClickListener {
    lateinit var mProdukDatabase: FirebaseDatabase
    lateinit var mProdukInstance: DatabaseReference

    private var fileUri: Uri? = null
    private var mediaPath: String? = null
    private var mImageFileLocation = ""
    private var postPath: String? = null
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.back_edit_produk ->{
                finish()
            }
            R.id.et_kategori ->{
                initKategori()
            }
            R.id.iv_produk_edit ->{
                MaterialDialog.Builder(this)
                    .title("Unggah Foto Produk")
                    .items(R.array.uploadImages)
                    .itemsIds(R.array.itemIds)
                    .itemsCallback { _, _, which, _ ->
                        when (which) {
                            0 -> {
                                if (checkPersmission()) {
                                    openGalery()
                                } else {
                                    requestPermissionStorage()
                                }

                            }
                            1 -> {
                                if (checkPersmission()) {
                                    captureImage()
                                } else {
                                    requestPermission()
                                }

                            }
                        }
                    }
                    .show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO || requestCode == REQUEST_PICK_PHOTO) {
                if (data != null) {
                    // Get the Image from data
                    val selectedImage = data.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                    val cursor = contentResolver.query(
                        selectedImage!!, filePathColumn,
                        null, null, null
                    )
                    assert(cursor != null)
                    cursor!!.moveToFirst()

                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    mediaPath = cursor.getString(columnIndex)
//                    iv_bukti_pembayaran.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
                    cursor.close()


                    postPath = mediaPath
                }


            } else if (requestCode == CAMERA_PIC_REQUEST) {
                if (Build.VERSION.SDK_INT > 21) {

//                    Glide.with(this).load(mImageFileLocation).into(iv_bukti_pembayaran)
                    postPath = mImageFileLocation

                } else {
//                    Glide.with(this).load(fileUri).into(iv_bukti_pembayaran)
                    postPath = fileUri!!.path

                }

            }

        } else if (resultCode != Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Maaf, terjadi error", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_produk)
        val activityName = intent.getStringExtra("namaActivity")
        val idProduk = intent.getStringExtra("produkId")
        edit_produk_text.text = activityName
        back_edit_produk.setOnClickListener(this)
        iv_produk_edit.setOnClickListener(this)
        et_kategori.setOnClickListener(this)
        loadData(idProduk)

    }
    private fun checkPersmission(): Boolean {
        return (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ), 101)
    }

    private fun openGalery(){
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO)
    }

    private fun captureImage() {
        if (Build.VERSION.SDK_INT > 21) {

            val callCameraApplicationIntent = Intent()
            callCameraApplicationIntent.action = MediaStore.ACTION_IMAGE_CAPTURE

            // We give some instruction to the intent to save the image
            var photoFile: File? = null

            try {
                // If the createImageFile will be successful, the photo file will have the address of the file
                photoFile = createImageFile()
                // Here we call the function that will try to catch the exception made by the throw function
            } catch (e: IOException) {
                Logger.getAnonymousLogger().info("Exception error in generating the file")
                e.printStackTrace()
            }

            // Here we add an extra file to the intent to put the address on to. For this purpose we use the FileProvider, declared in the AndroidManifest.
            val outputUri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".provider",
                photoFile!!
            )
            callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)

            // The following is a new line with a trying attempt
            callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)


            // The following strings calls the camera app and wait for his file in return.
            startActivityForResult(callCameraApplicationIntent, CAMERA_PIC_REQUEST)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE)

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_PIC_REQUEST)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable("file_uri", fileUri)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        fileUri = savedInstanceState.getParcelable("file_uri")
    }

    fun getOutputMediaFileUri(type: Int): Uri {
        return Uri.fromFile(getOutputMediaFile(type))
    }

    @Throws(IOException::class)
    internal fun createImageFile(): File {
        Logger.getAnonymousLogger().info("Generating the image - method started")

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmSS").format(Date())
        val imageFileName = "IMAGE_$timeStamp"
        val storageDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        if (!storageDirectory.exists()) storageDirectory.mkdir()

        val image = File(storageDirectory, "$imageFileName.jpg")



        mImageFileLocation = image.absolutePath
        return image
    }

    private fun uploadFile() {
        if (postPath == null || postPath == "") {
            Toast.makeText(this, "please select an image ", Toast.LENGTH_LONG).show()
            return
        } else {

            // Map is used to multipart the file using okhttp3.RequestBody
//            val file = File(postPath!!)
//            val compressedImageFile =  Compressor(this).setMaxWidth(640)
//                .setMaxHeight(480)
//                .setQuality(75)
//                .setCompressFormat(Bitmap.CompressFormat.JPEG)
//                .compressToFile(file)

            // Parsing any Media type file

        }
    }

    private fun requestPermissionStorage() {
        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ), 99)
    }
    private fun loadData(produkId : String){
        mProdukDatabase = FirebaseDatabase.getInstance()
        mProdukInstance = mProdukDatabase.reference.child("produk")
        mProdukInstance.child(produkId).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    Picasso.get().load(p0.child("foto").getValue(String::class.java)).fit().centerCrop().into(iv_produk_edit)
                    et_kategori.setText(p0.child("kategori").getValue(String::class.java))
                    et_nama_produk.setText(p0.child("kategori").getValue(String::class.java))
                    et_harga_produk.setText(p0.child("harga").getValue(String::class.java))
                    et_deskripsi.setText(p0.child("deskripsi").getValue(String::class.java))
                }
            }

        })
    }

    private fun initKategori(){
        val view = layoutInflater.inflate(R.layout.bottom_sheet_kategori, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(view)
        view.ll_frozen.setOnClickListener {
            et_kategori.setText("Frozen Food")
            dialog.dismiss()
        }
        view.ll_lauk.setOnClickListener {
            et_kategori.setText("Aneka Lauk")
            dialog.dismiss()
        }
        view.ll_minuman.setOnClickListener {
            et_kategori.setText("Minuman")
            dialog.dismiss()
        }
        view.ll_sambal.setOnClickListener {
            et_kategori.setText("Sambal")
            dialog.dismiss()
        }
        view.ll_snack.setOnClickListener {
            et_kategori.setText("Snack")
            dialog.dismiss()
        }
        view.ll_lain_lain.setOnClickListener {
            et_kategori.setText("Lain Lain")
            dialog.dismiss()
        }
        dialog.show()
    }

    companion object {
        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_PICK_PHOTO = 2
        private val CAMERA_PIC_REQUEST = 1111

        private val TAG = EditProdukActivity::class.java.getSimpleName()


        val MEDIA_TYPE_IMAGE = 1
        val IMAGE_DIRECTORY_NAME = "Android File Upload"

        private fun getOutputMediaFile(type: Int): File? {

            // External sdcard location
            val mediaStorageDir = File(
                Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME
            )

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(
                        TAG, "Oops! Failed create "
                                + IMAGE_DIRECTORY_NAME + " directory"
                    )
                    return null
                }
            }

            val mediaFile: File
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = File(
                    mediaStorageDir.path + File.separator
                            + "IMG_" + ".jpg"
                )
            } else {
                return null
            }

            return mediaFile
        }
    }
}
