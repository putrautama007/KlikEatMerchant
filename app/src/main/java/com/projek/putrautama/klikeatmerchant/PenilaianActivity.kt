package com.projek.putrautama.klikeatmerchant

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.google.firebase.database.*
import com.projek.putrautama.klikeatmerchant.adapters.UlasanDetailAdapter
import com.projek.putrautama.klikeatmerchant.models.UlasanList
import kotlinx.android.synthetic.main.activity_penilaian.*

class PenilaianActivity : AppCompatActivity(),View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_semua_penilaian ->{
                btn_semua_penilaian.setBackgroundResource(R.drawable.rounded_orange_background)
                btn_penialaian_1.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_2.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_3.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_4.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_5.setBackgroundResource(R.drawable.rounded_grey_background)
                loadUlasan(idProduk)
            }
            R.id.btn_penialaian_1 ->{
                btn_semua_penilaian.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_1.setBackgroundResource(R.drawable.rounded_orange_background)
                btn_penialaian_2.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_3.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_4.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_5.setBackgroundResource(R.drawable.rounded_grey_background)
                loadUlasanBerdasarkanBintang(idProduk,"1.0")
            }
            R.id.btn_penialaian_2 ->{
                btn_semua_penilaian.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_1.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_2.setBackgroundResource(R.drawable.rounded_orange_background)
                btn_penialaian_3.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_4.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_5.setBackgroundResource(R.drawable.rounded_grey_background)
                loadUlasanBerdasarkanBintang(idProduk,"2.0")
            }
            R.id.btn_penialaian_3 ->{
                btn_semua_penilaian.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_1.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_2.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_3.setBackgroundResource(R.drawable.rounded_orange_background)
                btn_penialaian_4.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_5.setBackgroundResource(R.drawable.rounded_grey_background)
                loadUlasanBerdasarkanBintang(idProduk,"3.0")
            }
            R.id.btn_penialaian_4 ->{
                btn_semua_penilaian.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_1.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_2.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_3.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_4.setBackgroundResource(R.drawable.rounded_orange_background)
                btn_penialaian_5.setBackgroundResource(R.drawable.rounded_grey_background)
                loadUlasanBerdasarkanBintang(idProduk,"4.0")
            }
            R.id.btn_penialaian_5 ->{
                btn_semua_penilaian.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_1.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_2.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_3.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_4.setBackgroundResource(R.drawable.rounded_grey_background)
                btn_penialaian_5.setBackgroundResource(R.drawable.rounded_orange_background)
                loadUlasanBerdasarkanBintang(idProduk,"5.0")
            }
            R.id.iv_back_penilaian ->{
                finish()
            }
        }
    }

    lateinit var mProdukDatabase : FirebaseDatabase
    lateinit var mProdukInstance : DatabaseReference
    lateinit var ulasanList: ArrayList<UlasanList>
    lateinit var progressBar: ProgressBar
    lateinit var rvUlasan: RecyclerView
    lateinit var idProduk : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penilaian)
        progressBar = findViewById(R.id.progressbar_penilaian)
        rvUlasan = findViewById(R.id.rv_penilaian_detail)
        idProduk = intent.getStringExtra("produkId")
        Log.d("TAG","$idProduk")
        btn_semua_penilaian.setOnClickListener(this)
        btn_penialaian_1.setOnClickListener(this)
        btn_penialaian_2.setOnClickListener(this)
        btn_penialaian_3.setOnClickListener(this)
        btn_penialaian_4.setOnClickListener(this)
        btn_penialaian_5.setOnClickListener(this)
        iv_back_penilaian.setOnClickListener(this)
        loadUlasan(idProduk)
    }

    private fun loadUlasanBerdasarkanBintang(produkId: String,bintang : String){
        progressBar.visibility = View.VISIBLE
        mProdukDatabase = FirebaseDatabase.getInstance()
        mProdukInstance = mProdukDatabase.reference.child("produk")
        if (produkId != null) {
            mProdukInstance.child(produkId).child("ulasan").orderByChild("ratingProduk").equalTo(bintang).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()){
                        ulasanList = ArrayList()
                        for (ulasan in p0.children){
                            val ulasans = ulasan.getValue(UlasanList::class.java)
                            if (ulasans != null) {
                                ulasanList.add(ulasans)
                            }
                        }

                        rvUlasan.layoutManager = LinearLayoutManager(this@PenilaianActivity, LinearLayoutManager.VERTICAL, false)
                        val ulasanProdukAdapter = UlasanDetailAdapter( ulasanList,this@PenilaianActivity)
                        rvUlasan.addItemDecoration(
                            DividerItemDecoration(
                                applicationContext,
                                DividerItemDecoration.VERTICAL
                            )
                        )
                        rvUlasan.adapter = ulasanProdukAdapter
                        ulasanProdukAdapter.notifyDataSetChanged()
                        progressBar.visibility = View.INVISIBLE
                    }
                }

            })
        }
    }

    private fun loadUlasan(produkId : String){
        progressBar.visibility = View.VISIBLE
        mProdukDatabase = FirebaseDatabase.getInstance()
        mProdukInstance = mProdukDatabase.reference.child("produk")
        if (produkId != null) {
            var penilaian1 = 0
            var penilaian2 = 0
            var penilaian3 = 0
            var penilaian4 = 0
            var penilaian5 = 0
            mProdukInstance.child(produkId).child("ulasan").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()){
                        ulasanList = ArrayList()
                        for (ulasan in p0.children){
                            val ulasans = ulasan.getValue(UlasanList::class.java)
                            if (ulasans != null) {
                                ulasanList.add(ulasans)
                            }
                            if (ulasans?.ratingProduk.equals("1.0")) {
                                penilaian1++
                            }
                            if (ulasans?.ratingProduk.equals("2.0")) {
                                penilaian2++
                            }
                            if (ulasans?.ratingProduk.equals("3.0")) {
                                penilaian3++
                            }
                            if (ulasans?.ratingProduk.equals("4.0")) {
                                penilaian4++
                            }
                            if (ulasans?.ratingProduk.equals("5.0")) {
                                penilaian5++
                            }
                        }
                        btn_semua_penilaian.text = "Semua (${ulasanList.size})"
                        btn_penialaian_1.text = "1 Bintang ($penilaian1)"
                        btn_penialaian_2.text = "2 Bintang ($penilaian2)"
                        btn_penialaian_3.text = "3 Bintang ($penilaian3)"
                        btn_penialaian_4.text = "4 Bintang ($penilaian4)"
                        btn_penialaian_5.text = "5 Bintang ($penilaian5)"
                        rvUlasan.layoutManager = LinearLayoutManager(this@PenilaianActivity, LinearLayoutManager.VERTICAL, false)
                        val ulasanProdukAdapter = UlasanDetailAdapter( ulasanList,this@PenilaianActivity)
                        rvUlasan.addItemDecoration(
                            DividerItemDecoration(
                                applicationContext,
                                DividerItemDecoration.VERTICAL
                            )
                        )
                        rvUlasan.adapter = ulasanProdukAdapter
                        ulasanProdukAdapter.notifyDataSetChanged()
                        progressBar.visibility = View.INVISIBLE
                    }
                }

            })
        }
    }
}
