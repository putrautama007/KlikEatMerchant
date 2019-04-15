package com.projek.putrautama.klikeatmerchant

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
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
import android.support.v4.content.MimeTypeFilter
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
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
    lateinit var storageReference : StorageReference
    lateinit var databaseReference: DatabaseReference
    val IMAGE_REQUEST: Int = 1
    var uri: Uri? = null


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.back_edit_produk ->{
                finish()
            }
            R.id.et_kategori ->{
                initKategori()
            }
            R.id.iv_produk_edit ->{
                openFileChooser()
            }
            R.id.konfirm_edit_akun ->{
                uploadFile()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_produk)
        val activityName = intent.getStringExtra("namaActivity")
        val idProduk = intent.getStringExtra("produkId")
        storageReference = FirebaseStorage.getInstance().getReference("Produk")
        databaseReference = FirebaseDatabase.getInstance().getReference("produk")
        edit_produk_text.text = activityName
        back_edit_produk.setOnClickListener(this)
        iv_produk_edit.setOnClickListener(this)
        et_kategori.setOnClickListener(this)
        konfirm_edit_akun.setOnClickListener(this)
        loadData(idProduk)
    }

    private fun openFileChooser(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_REQUEST)
    }

    private fun getFileExtension(uri: Uri) : String?{
        val contentResolver = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun uploadFile(){
        if (uri != null){
            val fileReference = storageReference.child("${System.currentTimeMillis()}.${getFileExtension(uri!!)}")
            fileReference.putFile(uri!!).addOnCompleteListener { task ->
                val imageUrl = task.result.toString()
                Log.d("TAG",imageUrl)

            }
        }else{
            Toast.makeText(this,"Belum ada foto yang dipilih",Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null){
            uri =data.data
            Picasso.get().load(uri).fit().centerCrop().into(iv_produk_edit)
        }
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
