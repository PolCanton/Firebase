package car.copernic.pcanton.pruebafirebase.SignUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import car.copernic.pcanton.pruebafirebase.SignIn.signinActivity
import car.copernic.pcanton.pruebafirebase.databinding.ActivitySignUpctivityBinding
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
        binding.backImageView.setOnClickListener{ backImageViewOnClick() }


    }

    //OnClick
    private fun signUpButtonOnClick() {
        iniciarSession()
    }
    private fun backImageViewOnClick() {
        volver()
    }

    fun iniciarSession() {
        val mEmail = binding.emailEditText2.text.toString()

        val mPassword = binding.passwordEditText2.text.toString()
        val mRepeatPassword = binding.repeatPasswordEditText.text.toString()

        val passwordRegex = Pattern.compile(
            "(?=.*[a-z A-z 0-9])" +
                    ".{8,}" +
                    "$")
        if(mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            Toast.makeText(this, "Ingrese un email valido.",
                Toast.LENGTH_SHORT).show()
        } else if (mPassword.isEmpty()|| !passwordRegex.matcher(mPassword).matches()){
            Toast.makeText(this, "La contraseña es debil.",
                Toast.LENGTH_SHORT).show()
        } else if (mPassword != mRepeatPassword){
            Toast.makeText(this, "Confirma la contraseña.",
                Toast.LENGTH_SHORT).show()
        } else {
            createAccount(mEmail, mPassword)
        }
    }
    private fun volver() {
        val intent = Intent(this, signinActivity::class.java)
        this.startActivity(intent)
    }
    private fun createAccount(email: String, password: String) {
        var auth= Firebase.auth

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
        db.collection("user").document(mEmail.toString()).set(
            hashMapOf("direccion" to direccion,
                "telefono" to telefono,
                "correo" to mEmail))
    }







}