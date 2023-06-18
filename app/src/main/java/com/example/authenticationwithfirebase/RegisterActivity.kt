package com.example.authenticationwithfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.authenticationwithfirebase.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.LoginClickableText.setOnClickListener {
            val intent = Intent (this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.RegisterButton.setOnClickListener {
            val email = binding.etRegisterEmail.text.toString()
            val pass = binding.etRegisterPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()){

                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful){
                        val intent = Intent (this, LoginActivity::class.java)
                        startActivity(intent)

                    }else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

            }else{
                Toast.makeText(this, "Empty fields are not allowed!!!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
