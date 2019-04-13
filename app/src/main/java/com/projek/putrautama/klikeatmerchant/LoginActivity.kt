package com.projek.putrautama.klikeatmerchant

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.database.DataSnapshot
import com.google.gson.Gson
import com.projek.putrautama.klikeatmerchant.models.User
import com.projek.putrautama.klikeatmerchant.utils.SessionManager
import org.jetbrains.anko.startActivity


class LoginActivity : AppCompatActivity(),View.OnClickListener {
    lateinit var mUserDatabase : FirebaseDatabase
    lateinit var muserInstance : DatabaseReference
    lateinit var progressBar: ProgressBar
    lateinit var session: SessionManager
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_sign_in ->{
                loginUser()
            }
            R.id.lupa_password ->{

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressBar = findViewById(R.id.progressbar_login)
        session = SessionManager(applicationContext)
        btn_sign_in.setOnClickListener(this)
        lupa_password.setOnClickListener(this)

    }

    private fun loginUser(){
        progressBar.visibility = View.VISIBLE
        mUserDatabase = FirebaseDatabase.getInstance()
        muserInstance = mUserDatabase.reference.child("merchant")
        val username : String = username.text.toString()
        val password : String= password.text.toString()

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(applicationContext, "Mohon masukan email", Toast.LENGTH_LONG).show()
            progressBar.visibility = View.INVISIBLE
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Mohon masukan password", Toast.LENGTH_LONG).show()
            progressBar.visibility = View.INVISIBLE
            return
        }
        val loginQuerry : Query = muserInstance.orderByChild("email").equalTo(username)
        loginQuerry.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    progressBar.visibility = View.INVISIBLE
                    for (user in p0.children) {
                       val userData = user.getValue(User::class.java)
                        if (userData?.password?.equals(password)!!){
                            val gson = Gson()
                            val jsonString = gson.toJson(userData)
                            session.createLoginSession(userData.userId,jsonString)
                            startActivity<MainActivity>()
                        }else{
                            Toast.makeText(this@LoginActivity,"Password anda salah",Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this@LoginActivity,"Email anda salah",Toast.LENGTH_SHORT).show()
                }
            }

        })

    }
}
