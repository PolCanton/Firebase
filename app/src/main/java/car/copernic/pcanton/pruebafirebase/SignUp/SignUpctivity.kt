package car.copernic.pcanton.pruebafirebase.SignUp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import car.copernic.pcanton.pruebafirebase.SignIn.signinActivity
import car.copernic.pcanton.pruebafirebase.databinding.ActivitySignUpctivityBinding
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.util.*
import java.util.regex.Pattern

class SignUpctivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivitySignUpctivityBinding
    private  var filePath: Uri? = null
    private val PICK_IMAGE_REQUEST = 71

    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpctivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = Firebase.auth
        binding.signUpButton2.setOnClickListener { signUpButton2OnClick() }
        binding.backImageView.setOnClickListener{ backImageViewOnClick() }
        binding.pickimage.setOnClickListener { chooseImage() }


    }

    private fun signUpButton2OnClick() {
        iniciarSession()
    }
    private fun backImageViewOnClick() {
        volver()
    }

    fun iniciarSession() {

        val mEmail = binding.emailEditText2.text.toString()
        val mPassword = binding.passwordEditText2.text.toString()
        val mRepeatPassword = binding.repeatPasswordEditText.text.toString()

        val passwordRegex = Pattern.compile("(?=.*[a-z A-Z 0-9]).{8,}$")


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
         auth= Firebase.auth

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    insert_basedatos()
                    uploadImage()
                    volver()
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
                "telefono" to telefono,
                "correo" to mEmail))
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun uploadImage(){
        if(filePath != null){
            val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    addUploadRecordToDb(downloadUri.toString())
                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }


    private fun addUploadRecordToDb(uri: String){
        val db = FirebaseFirestore.getInstance()

        val data = HashMap<String, Any>()
        data["imageUrl"] = uri

        db.collection("posts")
            .add(data)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Saved to DB", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving to DB", Toast.LENGTH_LONG).show()
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                binding.pickimage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}