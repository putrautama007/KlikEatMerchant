package com.projek.putrautama.klikeatmerchant

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.projek.putrautama.klikeatmerchant.fragments.HomeFragment
import com.projek.putrautama.klikeatmerchant.fragments.ProfileFragment
import com.projek.putrautama.klikeatmerchant.fragments.TransaksiFragment
import com.projek.putrautama.klikeatmerchant.utils.SessionManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    lateinit var session: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        session = SessionManager(applicationContext)
        session.checkLogin()

        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadHomeFragment(savedInstanceState)
                }
                R.id.navigation_transaksi -> {
                    loadTransaksiFragment(savedInstanceState)
                }

                R.id.navigation_profile -> {
                    loadProfileFragment(savedInstanceState)
                }
                R.id.navigation_tambah_barang ->{
                    startActivity<AddProdukActivity>(
                        "namaActivity" to "Tambah Produk"
                    )
                }
            }
            true
        }
        navigation.selectedItemId = R.id.navigation_home
    }

    private fun loadHomeFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val homeFragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeFragment, HomeFragment::class.simpleName)
                .commit()
        }
    }

    private fun loadProfileFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val inboxFragment = ProfileFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, inboxFragment, ProfileFragment::class.simpleName)
                .commit()
        }
    }

    private fun loadTransaksiFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val historyFragment = TransaksiFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, historyFragment, TransaksiFragment::class.simpleName)
                .commit()
        }
    }
}
