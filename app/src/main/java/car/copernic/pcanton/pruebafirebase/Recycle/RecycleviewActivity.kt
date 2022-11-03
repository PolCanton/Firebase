package car.copernic.pcanton.pruebafirebase.Recycle

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import car.copernic.pcanton.pruebafirebase.Adapter.UserAdapter
import car.copernic.pcanton.pruebafirebase.Preuba.SubirImagen
import car.copernic.pcanton.pruebafirebase.databinding.FragmentRecycleviewActivityBinding
import car.copernic.pcanton.pruebafirebase.model.User
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class RecycleviewActivity : Fragment() {
    private lateinit var binding:FragmentRecycleviewActivityBinding
    lateinit var mAdapter: UserAdapter
    lateinit var mRecycler: RecyclerView
    lateinit var mFirestore: FirebaseFirestore
    lateinit var query: Query


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentRecycleviewActivityBinding.inflate(layoutInflater)
        mFirestore = FirebaseFirestore.getInstance()
        mRecycler = binding.recyclerView
        mRecycler.layoutManager =LinearLayoutManager(context)
        query = mFirestore.collection("user")

        val firestoreRecyclerOptions: FirestoreRecyclerOptions<User> =
            FirestoreRecyclerOptions.Builder<User>().setQuery(query,
                User::class.java).build()

        mAdapter = UserAdapter(firestoreRecyclerOptions)
        mAdapter.notifyDataSetChanged()
        mRecycler.adapter = mAdapter
        binding.botonDescargar.setOnClickListener {
            val i = Intent(activity, SubirImagen::class.java)
            startActivity(i)
        }
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        mAdapter.startListening()
    }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
    }
}
