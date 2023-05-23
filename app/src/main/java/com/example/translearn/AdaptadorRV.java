package com.example.translearn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdaptadorRV extends RecyclerView.Adapter<AdaptadorRV.ViewHolder> {

    static ArrayList<Traduccion> arrayList;
    Context context;

    public AdaptadorRV(ArrayList<Traduccion> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.modelo_traduccion,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Traduccion traduccion = arrayList.get(position);
        holder.aTraducir.setText(traduccion.getaTraducir());
        holder.traducido.setText(traduccion.getTraducido());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        Button borrar;
        TextView traducido,aTraducir;
        FirebaseDatabase database;
        DatabaseReference reference;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            traducido = itemView.findViewById(R.id.texto1);
            aTraducir = itemView.findViewById(R.id.texto2);
            borrar = itemView.findViewById(R.id.borrar);


            borrar.setOnClickListener(v -> {
                int position = getAdapterPosition();
                borrarModelo(position);
                notifyDataSetChanged();

                //BORRADO EN DB
                database = FirebaseDatabase.getInstance();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String path = "/usuarios/"+uid+"/traducciones";
                reference = database.getReference(path);
                reference.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DataSnapshot snapshot = task.getResult();
                        if (snapshot.exists()) {
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                Traduccion traduccion = childSnapshot.getValue(Traduccion.class);
                                System.out.println(traduccion.getaTraducir()+"  =  "+aTraducir.getText());
                                System.out.println(traduccion.getTraducido()+"  =  "+traducido.getText());
                                if((traduccion.getaTraducir() == aTraducir.getText()) && (traduccion.getTraducido() == traducido.getText())){
                                    System.out.println("EUREKAAAAAAAAA");
                                    reference.removeValue();
                                }
                            }
                        } else {
                            System.out.println("Path no válido");
                        }
                    } else {
                        Exception exception = task.getException();
                        System.out.println("");
                    }
                });
            });

        }
    }
    private void borrarModelo(int pos){
        arrayList.remove(pos);
    }
}
