package com.projek.putrautama.klikeatmerchant.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User (
    var nama: String? = "",
    var email : String = "",
    var password : String? = "",
    var notlp : String? = "",
    var alamat: String? = "",
    var foto : String? = "",
    var userId : String = ""
)