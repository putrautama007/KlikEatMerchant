package com.projek.putrautama.klikeatmerchant

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import com.google.firebase.database.*
import com.google.gson.Gson
import com.projek.putrautama.klikeatmerchant.adapters.UlasanAdapter
import com.projek.putrautama.klikeatmerchant.models.ProdukList
import com.projek.putrautama.klikeatmerchant.models.UlasanList
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_produk_detail.*
import org.jetbrains.anko.startActivity
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ProdukDetailActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_detail_produk -> {
                finish()
            }
            R.id.edit_produk -> {
                startActivity<EditProdukActivity>(
                    "produkId" to produk.produk_id,
                    "namaActivity" to "Edit Produk"

                )
            }
            R.id.ll_lihat_ulasan -> {
                startActivity<PenilaianActivity>(
                    "produkId" to produk.produk_id
                )
            }
        }
    }

    lateinit var mProdukDatabase: FirebaseDatabase
    lateinit var mProdukInstance: DatabaseReference
    lateinit var ulasanList: ArrayList<UlasanList>
    lateinit var progressBar: ProgressBar
    lateinit var rvUlasan: RecyclerView
    lateinit var produkData: String
    lateinit var produk: ProdukList
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produk_detail)
        progressBar = findViewById(R.id.progressbar_detail_makanan)
        rvUlasan = findViewById(R.id.rv_ulasan_produk)
        back_detail_produk.setOnClickListener(this)
        edit_produk.setOnClickListener(this)
        ll_lihat_ulasan.setOnClickListener(this)
        loadData()
    }

    private fun turnToIdr(harga: Int?): String? {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(harga)
    }

    private fun loadData() {
        produkData = intent.getStringExtra("produkData")
        produk = Gson().fromJson(produkData, ProdukList::class.java)
        nama_produk.text = produk.nama_produk
        Picasso.get().load(produk.foto).fit().centerCrop().into(iv_makanan_detail)
        tv_harga_detail.text = turnToIdr(produk.harga?.toInt())
        tv_foodName_detail.text = produk.nama_produk
        rating_produk_detail.rating = produk.rating?.toFloat()!!
        tv_jumlah_ulasan_detail.text = "(${produk.jumlahUlasan})"
        tv_deskripsi_produk.text = produk.deskripsi
        loadUlasan(produk.produk_id)
    }

    private fun loadUlasan(produkId: String?) {
        progressBar.visibility = View.VISIBLE
        mProdukDatabase = FirebaseDatabase.getInstance()
        mProdukInstance = mProdukDatabase.reference.child("produk")
        if (produkId != null) {
            mProdukInstance.child(produkId).child("ulasan").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        ulasanList = ArrayList()
                        for (ulasan in p0.children) {
                            val ulasans = ulasan.getValue(UlasanList::class.java)
                            if (ulasans != null) {
                                ulasanList.add(ulasans)
                            }
                            rvUlasan.layoutManager =
                                LinearLayoutManager(this@ProdukDetailActivity, LinearLayoutManager.VERTICAL, false)
                            val ulasanProdukAdapter = UlasanAdapter(ulasanList, this@ProdukDetailActivity)
                            rvUlasan.addItemDecoration(
                                DividerItemDecoration(
                                    applicationContext,
                                    DividerItemDecoration.VERTICAL
                                )
                            )
                            rvUlasan.adapter = ulasanProdukAdapter
                            rvUlasan.smoothScrollBy(0, 100)
                            ulasanProdukAdapter.notifyDataSetChanged()
                            progressBar.visibility = View.INVISIBLE
                        }
                    }
                }

            })
        }
    }
}
