package com.projek.putrautama.klikeatmerchant.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UlasanList (
    var namaProfile: String? = "",
    var isiUlasan: String? = "",
    var ratingProduk: String? = "",
    var idUlasan: String? = "",
    var tglUlasan: String? = "",
    var produk_id: String? = "",
    var userId: String? = ""
)