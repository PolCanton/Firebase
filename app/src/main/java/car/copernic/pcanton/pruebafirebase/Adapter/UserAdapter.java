package car.copernic.pcanton.pruebafirebase.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import car.copernic.pcanton.pruebafirebase.R;
import car.copernic.pcanton.pruebafirebase.model.User;


public class UserAdapter extends FirestoreRecyclerAdapter<User, UserAdapter.ViewHolder> {

    public UserAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull User model) {
        holder.correo.setText(model.getCorreo());
        holder.direccion.setText(model.getDireccion());
        holder.telefono.setText(model.getTelefono());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.view_user_single,parent,false);
       return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView correo,direccion,telefono;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            correo=itemView.findViewById(R.id.correoTV);
            direccion=itemView.findViewById(R.id.direccionTV);
            telefono=itemView.findViewById(R.id.telefonoTV);
        }
    }
}
