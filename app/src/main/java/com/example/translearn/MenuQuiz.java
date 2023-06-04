package com.example.translearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;

public class MenuQuiz extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    Button bQuizTrad,bQuizOrig;
    int numGuardados;

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

        setContentView(R.layout.activity_menu_quiz);
        bQuizOrig = findViewById(R.id.botonQuizOriginal);
        bQuizTrad = findViewById(R.id.botonQuizTraduccion);

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
                        numGuardados = numGuardados + 1;
                    }
                } else {
                    System.out.println("Path no válido");
                }
            } else {
                Exception exception = task.getException();
            }
        });

        bQuizTrad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numGuardados >= 5){
                Intent irQuizTraducir = new Intent(getApplicationContext(),QuizOriginal.class);
                startActivity(irQuizTraducir);
                finish();
            }else{
                    Toast.makeText(MenuQuiz.this, "DEBES TENER AL MENOS 5 TRADUCCIONES GARDADAS", Toast.LENGTH_SHORT).show();}
            }
        });

        bQuizOrig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numGuardados >= 5){
                Intent irQuizOrig = new Intent(getApplicationContext(),QuizTraduc.class);
                startActivity(irQuizOrig);
                finish();
                }else{
                    Toast.makeText(MenuQuiz.this, "DEBES TENER AL MENOS 5 TRADUCCIONES GARDADAS", Toast.LENGTH_SHORT).show();}
            }
        });

    }
}