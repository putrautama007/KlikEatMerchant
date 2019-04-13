package com.projek.putrautama.klikeatmerchant.adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.google.gson.Gson
import com.projek.putrautama.klikeatmerchant.ProdukDetailActivity
import com.projek.putrautama.klikeatmerchant.R
import com.projek.putrautama.klikeatmerchant.models.ProdukList
import com.projek.putrautama.klikeatmerchant.utils.RoundedCornersTransformation
import com.squareup.picasso.Picasso
import org.jetbrains.anko.startActivity
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ProdukAdapter (val produkList: ArrayList<ProdukList>, val context: Context) : RecyclerView.Adapter<ProdukAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.list_food, p0, false))
    }

    override fun getItemCount(): Int {
        return produkList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val roundedCornersTransformation = RoundedCornersTransformation(40, 0)
        Picasso.get().load(produkList[p1].foto).fit()
            .centerCrop().transform(roundedCornersTransformation).into(p0.ivProduk)
        p0.produkName.text = produkList[p1].nama_produk
        p0.harga.text = turnToIdr(produkList[p1].harga?.toInt())
        p0.rating.rating = produkList[p1].rating!!.toFloat()
        p0.jumlahUlasan.text = "(${produkList[p1].jumlahUlasan})"
        p0.cvProduk.setOnClickListener {
            context.startActivity<ProdukDetailActivity>(
                "produkData" to Gson().toJson(produkList[p1])
            )
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProduk = itemView.findViewById<ImageView>(R.id.iv_produk)
        val produkName = itemView.findViewById<TextView>(R.id.tv_foodName)
        val harga = itemView.findViewById<TextView>(R.id.tv_harga)
        val rating = itemView.findViewById<RatingBar>(R.id.rating_makanan)
        val jumlahUlasan = itemView.findViewById<TextView>(R.id.tv_jumlah_ulasan)
        val cvProduk = itemView.findViewById<CardView>(R.id.cv_produk)
    }

    private fun turnToIdr(harga: Int?): String? {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(harga)
    }
}