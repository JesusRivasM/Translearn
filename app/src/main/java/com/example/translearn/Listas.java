package com.example.translearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.translearn.modelos.Traduccion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Listas extends AppCompatActivity {

    private ArrayList<Traduccion> arrayList;
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private AdaptadorRV adaptadorRV;

    //ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),Menu.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_listas);

        arrayList = new ArrayList<>();
        adaptadorRV = new AdaptadorRV(arrayList,this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptadorRV);

       //RECUPERACIÓN DE DATOS:
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
                        arrayList.add(traduccion);
                        adaptadorRV.notifyDataSetChanged();
                    }
                } else {
                    System.out.println("Path no válido");
                }
            } else {
                Exception exception = task.getException();
                    System.out.println("");
            }
        });
    }
}
