package com.projek.putrautama.klikeatmerchant

import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_edit_produk.*
import kotlinx.android.synthetic.main.bottom_sheet_kategori.view.*

class AddProdukActivity : AppCompatActivity(),View.OnClickListener {
    lateinit var storageReference : StorageReference
    lateinit var databaseReference: DatabaseReference


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.back_edit_produk ->{
                finish()
            }
            R.id.et_kategori ->{
                initKategori()
            }
            R.id.iv_produk_edit ->{

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_produk)
        val activityName = intent.getStringExtra("namaActivity")
        edit_produk_text.text = activityName
        storageReference =FirebaseStorage.getInstance().getReference("Produk")
        databaseReference = FirebaseDatabase.getInstance().getReference("produk")
        back_edit_produk.setOnClickListener(this)
        iv_produk_edit.setOnClickListener(this)
        et_kategori.setOnClickListener(this)
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
}