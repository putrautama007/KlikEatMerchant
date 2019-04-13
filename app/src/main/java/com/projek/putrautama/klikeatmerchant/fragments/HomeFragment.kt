package com.projek.putrautama.klikeatmerchant.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.google.firebase.database.*

import com.projek.putrautama.klikeatmerchant.R
import com.projek.putrautama.klikeatmerchant.adapters.ProdukAdapter
import com.projek.putrautama.klikeatmerchant.models.ProdukList
import com.projek.putrautama.klikeatmerchant.utils.SessionManager

class HomeFragment : Fragment() {

    lateinit var mProdukDatabase : FirebaseDatabase
    lateinit var mProdukInstance : DatabaseReference
    lateinit var progressBar: ProgressBar
    lateinit var rvProduk: RecyclerView
    lateinit var session: SessionManager
    lateinit var produkList : ArrayList<ProdukList>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        session = SessionManager(context)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.home_progressbar)
        rvProduk = view.findViewById(R.id.rv_home)
        loadData()
    }



    private fun loadData(){
        progressBar.visibility = View.VISIBLE
        mProdukDatabase = FirebaseDatabase.getInstance()
        mProdukInstance = mProdukDatabase.reference.child("produk")

        mProdukInstance.orderByChild("penjual_id").equalTo(session.pref.getString("userId",""))
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()){
                        produkList = ArrayList()
                        for(produk in p0.children){
                            val produkData = produk.getValue(ProdukList::class.java)
                            if (produkData != null) {
                                produkList.add(produkData)
                            }
                        }
                        rvProduk.layoutManager = LinearLayoutManager(context)
                        rvProduk.adapter = context?.let { ProdukAdapter(produkList, it) }
                        progressBar.visibility = View.INVISIBLE
                    }
                }

            })

    }


}
