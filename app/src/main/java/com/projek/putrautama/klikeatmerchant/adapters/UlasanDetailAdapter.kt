package com.projek.putrautama.klikeatmerchant.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import com.projek.putrautama.klikeatmerchant.R
import com.projek.putrautama.klikeatmerchant.models.UlasanList

class UlasanDetailAdapter(val ulasanList: ArrayList<UlasanList>, val context: Context) :
    RecyclerView.Adapter<UlasanDetailAdapter.ViewHolder>()  {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.list_ulasan, p0, false))
    }

    override fun getItemCount(): Int {
        return ulasanList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.profile.text = ulasanList[p1].namaProfile
        p0.review.text = ulasanList[p1].isiUlasan
        p0.tglReview.text = ulasanList[p1].tglUlasan
        p0.ratingBar.rating = ulasanList[p1].ratingProduk?.toFloat()!!
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profile = itemView.findViewById<TextView>(R.id.tv_profile_penilai)
        val ratingBar = itemView.findViewById<RatingBar>(R.id.rating_produk_ulasan_item)
        val review = itemView.findViewById<TextView>(R.id.tv_review_produk)
        val tglReview = itemView.findViewById<TextView>(R.id.tv_tgl_review)
    }
}