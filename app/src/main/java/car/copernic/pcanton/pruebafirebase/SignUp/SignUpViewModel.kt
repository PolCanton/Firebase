//package car.copernic.pcanton.pruebafirebase.SignUp
//
//import android.content.Context
//import android.content.Intent
//import android.util.Patterns
//import android.widget.Toast
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import car.copernic.pcanton.pruebafirebase.SignIn.signinActivity
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.ktx.Firebase
//import java.util.regex.Pattern
//
//class SignUpViewModel: ViewModel(){
//
//
//    private var _mEmail = MutableLiveData<String>()
//    val mEmail : LiveData<String>
//        get() = _mEmail
//
//    private var _mPassword = MutableLiveData<String>()
//    val mPassword : LiveData<String>
//        get() = _mPassword
//
//    private var _mRepeatPassword = MutableLiveData<String>()
//    val mRepeatPassword : LiveData<String>
//        get() = _mRepeatPassword
//
//    private var _direccion = MutableLiveData<String>()
//    val direccion : LiveData<String>
//        get() = _direccion
//
//    private var _telefono = MutableLiveData<String>()
//    val telefono : LiveData<String>
//        get() = _telefono
//
//    fun updateData(
//        email: String,
//        password: String,
//        password2: String,
//        direccion: String,
//        telefono: String
//    ) {
//        _mEmail.value=email
//        _mPassword.value=password
//        _mRepeatPassword.value=password2
//        _direccion.value=direccion
//        _telefono.value=telefono
//    }
//    fun iniciarSession() {
//
//        val passwordRegex = Pattern.compile("(?=.*[a-z A-Z 0-9]).{8,}$")
//
//        if(mEmail.value.toString().isEmpty()|| !Patterns.EMAIL_ADDRESS.matcher(mEmail.toString()).matches()) {
//            Toast.makeText(this, "Ingrese un email valido.",
//                Toast.LENGTH_SHORT).show()
//        } else if (mPassword.value.toString().isEmpty()|| !passwordRegex.matcher(mPassword.toString()).matches()){
//            Toast.makeText(this, "La contraseña es debil.",
//                Toast.LENGTH_SHORT).show()
//        } else if (mPassword != mRepeatPassword){
//            Toast.makeText(this, "Confirma la contraseña.",
//                Toast.LENGTH_SHORT).show()
//
//        } else {
//            createAccount(mEmail.toString(), mPassword.toString())
//        }
//    }
//
//    private fun createAccount(email: String, password: String) {
//        var auth= Firebase.auth
//
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    insert_basedatos()
//                    val intent = Intent(this, signinActivity::class.java)
//                    this.startActivity(intent)
//                } else {
//                    Toast.makeText(this, "No se pudo crear la cuenta. Vuelva a intertarlo",
//                        Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
//    private fun insert_basedatos() {
//        val db = FirebaseFirestore.getInstance()
//
//        db.collection("user").document(mEmail.toString()).set(
//            hashMapOf("direccion" to direccion,
//                "telefono" to telefono,
//                "correo" to mEmail))
//    }
//}