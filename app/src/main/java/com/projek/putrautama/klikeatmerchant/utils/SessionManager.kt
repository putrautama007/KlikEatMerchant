package com.projek.putrautama.klikeatmerchant.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.projek.putrautama.klikeatmerchant.LoginActivity

class SessionManager {
    var pref: SharedPreferences
    var editor: Editor
    var context: Context
    val PRIVATE_MODE = 0
    val PREF_NAME = "KlikEatMerchant"
    val IS_LOGIN = "IsLoggedIn"
    val KEY_USER_ID = "userId"
    val KEY_USER_DATA ="user"

    constructor(context: Context?){
        this.context = context!!
        pref = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE)
        editor = pref.edit()
    }

    fun createLoginSession(userId : String?,userData : String?){
        editor.putBoolean(IS_LOGIN,true)
        editor.putString(KEY_USER_DATA,userData)
        editor.putString(KEY_USER_ID,userId)
        editor.commit()
    }

    fun isLogin() : Boolean {
        return pref.getBoolean(IS_LOGIN,false)
    }

    fun checkLogin(){
        if (!this.isLogin()){
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    fun logoutUser(){
        editor.clear()
        editor.commit()
        val intent = Intent(context,LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}