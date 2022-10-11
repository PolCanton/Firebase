package car.copernic.pcanton.pruebafirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import car.copernic.pcanton.pruebafirebase.databinding.ActivitySignUpctivityBinding
import car.copernic.pcanton.pruebafirebase.databinding.SigninActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class SignUpctivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivitySignUpctivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpctivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        binding.signUpButton2.setOnClickListener { signUpButtonOnClick() }

    }

    //OnClick
    private fun signUpButtonOnClick() {
        iniciarSession()
    }

    private fun iniciarSession() {
        signUpButtonOnClick()
        val mEmail = binding.emailEditText2.text.toString()
        val mPassword = binding.passwordEditText2.text.toString()
        val mRepeatPassword = binding.repeatPasswordEditText.text.toString()

        val passwordRegex = Pattern.compile(
            "(?=.*[‐@#$%^&+=])" +     // Al menos 1 carácter especial
                    ".{6,}" +                // Al menos 4 caracteres
                    "$")
        if(mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            Toast.makeText(this, "Ingrese un email valido.",
                Toast.LENGTH_SHORT).show()
        } else if (mPassword.isEmpty() || !passwordRegex.matcher(mPassword).matches()){
            Toast.makeText(this, "La contraseña es debil.",
                Toast.LENGTH_SHORT).show()
        } else if (mPassword != mRepeatPassword){
            Toast.makeText(this, "Confirma la contraseña.",
                Toast.LENGTH_SHORT).show()
        } else {
            createAccount(mEmail, mPassword)
        }    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    insert_basedatos()
                    val intent = Intent(this, signinActivity::class.java)
                    this.startActivity(intent)
                } else {
                    Toast.makeText(this, "No se pudo crear la cuenta. Vuelva a intertarlo",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun insert_basedatos() {
        val mEmail = binding.emailEditText2.text.toString()
        val direccion=binding.direccionEditText.text.toString()
        val telefono=binding.telefonoEditText.text.toString()
        db.collection("user").document(mEmail).set(
            hashMapOf("direccion" to direccion,
                "telefono" to telefono))
    }

}