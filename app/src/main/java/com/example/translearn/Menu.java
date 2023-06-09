package com.example.translearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu extends AppCompatActivity {

    FirebaseAuth auth;
    ImageButton bLogout,bListas,bQuiz,bTraducir;
    FirebaseUser usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        bLogout = findViewById(R.id.botonLogout);
        bTraducir = findViewById(R.id.botonTraducir);
        bListas = findViewById(R.id.botonLista);
        bQuiz = findViewById(R.id.botonQuiz);
        usuario = auth.getCurrentUser();

        if(usuario == null){
            Intent irLogin = new Intent(getApplicationContext(), Login.class);
            startActivity(irLogin);
            finish();
        }

        bListas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irListas = new Intent(getApplicationContext(),Listas.class);
                startActivity(irListas);
                finish();
            }
        });
        bTraducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irTraducir = new Intent(getApplicationContext(), Traducir.class);
                startActivity(irTraducir);
                finish();
            }
        });
        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent irLogin = new Intent(getApplicationContext(), Login.class);
                startActivity(irLogin);
                finish();
            }
        });

        bQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irQuiz = new Intent(getApplicationContext(), MenuQuiz.class);
                startActivity(irQuiz);
                finish();
            }
        });
    }
}
