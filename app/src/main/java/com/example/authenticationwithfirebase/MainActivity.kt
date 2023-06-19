package com.example.authenticationwithfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.authenticationwithfirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val email = intent.getStringExtra("email")
        val displayName = intent.getStringExtra("name")
        val photo = intent.getStringExtra("photo")

        findViewById<TextView>(R.id.tvUserInfo).text = email + "\n" + displayName

        val profilePhotoView = findViewById<ImageView>(R.id.ivUserPhoto)
        Picasso.get().load(photo).into(profilePhotoView)


        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            if (firebaseAuth.currentUser == null){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Logout failed!", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }
}