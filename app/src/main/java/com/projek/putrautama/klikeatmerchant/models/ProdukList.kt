package com.projek.putrautama.klikeatmerchant.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ProdukList(
    var nama_produk: String? = "",
    var deskripsi: String? = "",
    var foto: String? = "",
    var harga: String? = "",
    var lokasi_penjual: String? = "",
    var penjual: String? = "",
    var rating: String? = "",
    var jumlahUlasan: String? = "",
    var produk_id: String? = "",
    var foto_penjual: String? = "",
    var ulasan: ArrayList<UlasanList>? = ArrayList(),
    var penjual_id : String? = ""
)