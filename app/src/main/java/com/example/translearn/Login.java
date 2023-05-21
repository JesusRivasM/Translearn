package com.example.translearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button botonLogin;
    TextInputEditText editTextEmail,editTextContra;
    TextView textView;



    @Override
    public void onStart() {
        super.onStart();
        FirebaseApp.initializeApp(this);
        Intent main = new Intent(getApplicationContext(), Menu.class);
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(main);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        Intent intent = new Intent(getApplicationContext(),Register.class);
        mAuth = FirebaseAuth.getInstance();
        //setContentView(R.layout.activity_register);
        editTextEmail = findViewById(R.id.email);
        editTextContra = findViewById(R.id.contra);
        botonLogin = findViewById(R.id.btn_login);
        textView = findViewById(R.id.my_text_view);

        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
                finish();
            }
        });


        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,contra;
                email = String.valueOf(editTextEmail.getText());
                contra = String.valueOf(editTextContra.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Login.this, "Write your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(contra)){
                    Toast.makeText(Login.this, "Write your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, contra)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Login.this, "Login succesful.",
                                            Toast.LENGTH_SHORT).show();
                                    try {
                                        Thread.sleep(1500);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    Intent main = new Intent(getApplicationContext(), Menu.class);
                                    startActivity(main);
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}
