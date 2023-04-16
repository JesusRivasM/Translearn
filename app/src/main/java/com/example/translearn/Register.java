package com.example.translearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button botonRegistro;
    TextInputEditText editTextEmail,editTextContra;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_register);
        editTextEmail = findViewById(R.id.emailr);
        editTextContra = findViewById(R.id.contrar);
        botonRegistro = findViewById(R.id.btn_registro);
        textView = findViewById(R.id.irLogin);
        Intent intent = new Intent(getApplicationContext(),Login.class);
        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(intent);
                finish();
            }
        });

        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,contra;
                email = String.valueOf(editTextEmail.getText());
                contra = String.valueOf(editTextContra.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Write your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(contra)){
                    Toast.makeText(Register.this, "Write your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, contra)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this, "Acount created succesfully.",
                                            Toast.LENGTH_SHORT).show();
                                    try {
                                        Thread.sleep(1500);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });
    }
}