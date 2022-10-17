package car.copernic.pcanton.pruebafirebase.SignIn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import car.copernic.pcanton.pruebafirebase.MainActivity
import car.copernic.pcanton.pruebafirebase.Recycle.RecycleviewActivity
import car.copernic.pcanton.pruebafirebase.SignUp.SignUpctivity
import car.copernic.pcanton.pruebafirebase.databinding.SigninActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class signinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding:SigninActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SigninActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.signInAppCompatButton.setOnClickListener{ signInAppCompatButtonOnClick() }
        binding.cuentaTextView.setOnClickListener{ volver() }

    }

    private fun volver() {
        val intent = Intent(this, SignUpctivity::class.java)
        this.startActivity(intent)
    }


    private fun signInAppCompatButtonOnClick() {
        val email=binding.emailEditText2.text.toString()
        val pswrd=binding.passwordEditText2.text.toString()

        when {
            pswrd.isEmpty() || email.isEmpty() -> {
                Toast.makeText(this, "Email o contraseña o incorrectos.",
                    Toast.LENGTH_SHORT).show()
            }
            else -> {
                signIn(email, pswrd)
            }
        }
    }

    private fun signIn(email: String, pswrd: String) {
        auth.signInWithEmailAndPassword(email, pswrd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithEmail:success")
                    val intent = Intent(this, MainActivity::class.java)
                    this.startActivity(intent)
                } else {
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Email o contraseña o incorrectos.",
                        Toast.LENGTH_SHORT).show()
                    reload()
                }
            }
    }

    private fun reload() {
        val intent = Intent(this, signinActivity::class.java)
        this.startActivity(intent)
    }
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            if(currentUser.isEmailVerified){
                val intent = Intent(this, RecycleviewActivity::class.java)
                this.startActivity(intent)
            }
        }
    }

}