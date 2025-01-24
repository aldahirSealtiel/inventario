package com.example.inventariomonas

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast

import com.example.inventariomonas.databinding.ActivityRegistroEmailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.HashMap

class RegistroEmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroEmailBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private var nombres :String = ""
    private var email = ""
    private var password = ""
    private var repeatPassword = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnRegistrar.setOnClickListener {
            validarInformacion()
        }
    }


    private fun validarInformacion()
    {
        nombres = binding.etNombres.text.toString()
        email = binding.etEmail.text.toString()
        password = binding.etPassword.text.toString()
        repeatPassword = binding.etRPassword.text.toString()
        if(nombres == "")
        {
            binding.etNombres.error = "Ingrese nombre de usuario"
            binding.etNombres.requestFocus()
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmail.error = "Correo invalido"
            binding.etEmail.requestFocus()
        }
        else if(email == "")
        {
            binding.etEmail.error = "Ingrese correo electrónico"
            binding.etEmail.requestFocus()
        }
        else if(password =="")
        {
            binding.etPassword.error = "Ingrese contraseña"
            binding.etPassword.requestFocus()
        }
        else if(repeatPassword ==""){
            binding.etRPassword.error = "Repita contraseña"
            binding.etRPassword.requestFocus()
        }
        else if(password != repeatPassword)
        {
            binding.etRPassword.error = "No coinciden las contraseñas"
            binding.etRPassword.requestFocus()
        }
        else{
            registrarUsuario()
        }
    }
    private fun registrarUsuario(){
        progressDialog.setMessage("Creando cuenta")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                actualizarInformacion()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Fallo la creación de la cuenta debido a ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun actualizarInformacion() {
        progressDialog.setMessage("Guardando información")
        val uidU = firebaseAuth.uid
        val nombresU = nombres
        val emailU = firebaseAuth.currentUser!!.email
        val tiempoRegistro = Constantes.obtenerTiempoD()

        val datosUsuario = HashMap<String, Any>()

        datosUsuario["uid"] = "$uidU"
        datosUsuario["nombres"] = "$nombresU"
        datosUsuario["email"] = "$emailU"
        datosUsuario["tiempoR"] = "$tiempoRegistro"
        datosUsuario["proveedor"] = "Email"
        datosUsuario["estado"] = "Online"

        val reference = FirebaseDatabase.getInstance().getReference("Usuarios")
        reference.child(uidU!!)
            .setValue(datosUsuario)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Fallo la creación de la cuenta debido a ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

}