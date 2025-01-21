package com.example.inventariomonas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.inventariomonas.databinding.ActivityMainBinding
import com.example.inventariomonas.fragmentos.FragmentPerfil
import com.example.inventariomonas.fragmentos.FragmentProductos
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //revision de firebase
        firebaseAuth = FirebaseAuth.getInstance()

        if(firebaseAuth.currentUser == null)
        {
            irOpcionesLogin()
        }

        //fragmento por defecto
        verFragmentoPerfil()

        binding.bottomNV.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.perfil->{
                    //Visualizar el fragmento perfil
                    verFragmentoPerfil()
                    true
                }
                R.id.productos->{
                    //Visualizar el fragmento usuarios
                    verFragmentoProductos()
                    true
                }
                else->{
                    false
                }
            }
        }
    }

    private fun irOpcionesLogin() {
        startActivity(Intent(applicationContext, OpcionesLoginActivity::class.java))
    }

    private fun verFragmentoPerfil(){
        binding.tvTitulo.text = "Perfil"
        val fragment = FragmentPerfil()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentoFL.id, fragment, "Fragment Perfil")
        fragmentTransaction.commit()

    }
    private fun verFragmentoProductos(){
        binding.tvTitulo.text = "Productos"
        val fragment = FragmentProductos()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentoFL.id, fragment, "Fragment productos")
        fragmentTransaction.commit()
    }
}