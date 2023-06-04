package com.example.translearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.translearn.modelos.Traduccion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class QuizTraduc extends AppCompatActivity {

    private int contadorCheck,nota;
    private TextView pregunta1,pregunta2,pregunta3,pregunta4,pregunta5,calificacion;
    private EditText respuesta1,respuesta2,respuesta3,respuesta4,respuesta5;
    private Button check1,check2,check3,check4,check5;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    private ArrayList<Traduccion> listaTraducciones;

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
        contadorCheck = 0;
        nota = 0;

        setContentView(R.layout.activity_quiz);

        calificacion = findViewById(R.id.puntos);

        pregunta1 = findViewById(R.id.pregunta1);
        pregunta2 = findViewById(R.id.pregunta2);
        pregunta3 = findViewById(R.id.pregunta3);
        pregunta4 = findViewById(R.id.pregunta4);
        pregunta5 = findViewById(R.id.pregunta5);

        respuesta1 = findViewById(R.id.respuesta1);
        respuesta2 = findViewById(R.id.respuesta2);
        respuesta3 = findViewById(R.id.respuesta3);
        respuesta4 = findViewById(R.id.respuesta4);
        respuesta5 = findViewById(R.id.respuesta5);

        check1 = findViewById(R.id.check1);
        check2 = findViewById(R.id.check2);
        check3 = findViewById(R.id.check3);
        check4 = findViewById(R.id.check4);
        check5 = findViewById(R.id.check5);

        listaTraducciones = new ArrayList<Traduccion>();
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
                        System.out.println(traduccion.getaTraducir());
                        listaTraducciones.add(traduccion);
                    }
                    Collections.shuffle(listaTraducciones);
                    rellenarPreguntas(listaTraducciones);
                } else {
                    System.out.println("Path no válido");
                }
            } else {
                Exception exception = task.getException();
            }
        });
        check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listaTraducciones.get(0).getTraducido().equals(respuesta1.getText().toString())){
                   respuesta1.setBackgroundColor(Color.GREEN);
                    nota = nota + 1;
                }else{
                    respuesta1.setBackgroundColor(Color.parseColor("#FC8282"));
                    respuesta1.setText(listaTraducciones.get(0).getTraducido());
                    respuesta1.setTypeface(null, Typeface.BOLD);
                }
                   respuesta1.setEnabled(false);
                   check1.setEnabled(false);
                   check1.setVisibility(View.GONE);
                   contadorCheck = contadorCheck + 1;
                   comprobarTerminado();
            }
        });
        check2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listaTraducciones.get(1).getTraducido().equals(respuesta2.getText().toString())){
                    respuesta2.setBackgroundColor(Color.GREEN);
                    nota = nota + 1;
                }else{
                    respuesta2.setBackgroundColor(Color.parseColor("#FC8282"));
                    respuesta2.setText(listaTraducciones.get(1).getTraducido());
                    respuesta2.setTypeface(null, Typeface.BOLD);
                }
                respuesta2.setEnabled(false);
                check2.setEnabled(false);
                check2.setVisibility(View.GONE);
                contadorCheck = contadorCheck + 1;
                comprobarTerminado();
            }
        });
        check3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listaTraducciones.get(2).getTraducido().equals(respuesta3.getText().toString())){
                    respuesta3.setBackgroundColor(Color.GREEN);
                    nota = nota + 1;
                }else{
                    respuesta3.setBackgroundColor(Color.parseColor("#FC8282"));
                    respuesta3.setText(listaTraducciones.get(2).getTraducido());
                    respuesta3.setTypeface(null, Typeface.BOLD);
                }
                respuesta3.setEnabled(false);
                check3.setEnabled(false);
                check3.setVisibility(View.GONE);
                contadorCheck = contadorCheck + 1;
                comprobarTerminado();
            }
        });
        check4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listaTraducciones.get(3).getTraducido().equals(respuesta4.getText().toString())){
                    respuesta4.setBackgroundColor(Color.GREEN);
                    nota = nota + 1;
                }else{
                    respuesta4.setBackgroundColor(Color.parseColor("#FC8282"));
                    respuesta4.setText(listaTraducciones.get(3).getTraducido());
                    respuesta4.setTypeface(null, Typeface.BOLD);
                }
                respuesta4.setEnabled(false);
                check4.setEnabled(false);
                check4.setVisibility(View.GONE);
                contadorCheck = contadorCheck + 1;
                comprobarTerminado();

            }
        });
        check5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listaTraducciones.get(4).getTraducido().equals(respuesta5.getText().toString())){
                    respuesta5.setBackgroundColor(Color.GREEN);
                    nota = nota + 1;
                }else{
                    respuesta5.setBackgroundColor(Color.parseColor("#FC8282"));
                    respuesta5.setText(listaTraducciones.get(4).getTraducido());
                    respuesta5.setTypeface(null, Typeface.BOLD);
                }
                respuesta5.setEnabled(false);
                check5.setEnabled(false);
                contadorCheck = contadorCheck + 1;
                check5.setVisibility(View.GONE);
                comprobarTerminado();
            }
        });
    }
    public void comprobarTerminado(){
        if (contadorCheck == 5){
            String stringNota = String.valueOf(nota);
            calificacion.setText(stringNota +"/5");
            if (nota<=2){calificacion.setTextColor(Color.parseColor("#FC8282"));}else{
                calificacion.setTextColor(Color.GREEN);
            }
            new CountDownTimer(2500, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    Intent irMenuQuiz = new Intent(getApplicationContext(),Menu.class);
                    startActivity(irMenuQuiz);
                    finish();
                }
            }.start();
        }
    }
    public void rellenarPreguntas(ArrayList<Traduccion> listaTraducciones){
        pregunta1.setText(listaTraducciones.get(0).getaTraducir());
        pregunta2.setText(listaTraducciones.get(1).getaTraducir());
        pregunta3.setText(listaTraducciones.get(2).getaTraducir());
        pregunta4.setText(listaTraducciones.get(3).getaTraducir());
        pregunta5.setText(listaTraducciones.get(4).getaTraducir());
    }
}
