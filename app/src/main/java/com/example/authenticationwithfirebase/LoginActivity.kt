package com.example.authenticationwithfirebase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.authenticationwithfirebase.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.squareup.picasso.Picasso

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSigninClient : GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSigninClient = GoogleSignIn.getClient(this, gso)

        binding.btnGoogleSignIn.setOnClickListener {
            signInGoogle()
        }

        binding.RegisterClickableText.setOnClickListener {
            val intent = Intent (this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {

            val email = binding.etLoginEmail.text.toString()
            val pass = binding.etLoginPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()){

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful){
                        val intent = Intent (this, MainActivity::class.java)
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

    private fun signInGoogle() {
        val signInIntent = googleSigninClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if (result.resultCode == Activity.RESULT_OK){

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account: GoogleSignInAccount? = task.result
            if (account != null){
                updateUI(account)
            }

        }else{
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                val intent: Intent = Intent(this, MainActivity::class.java)
                intent.putExtra("email", account.email)
                intent.putExtra("name", account.displayName)
                intent.putExtra("photo", account.photoUrl.toString())
                startActivity(intent)

            }else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}