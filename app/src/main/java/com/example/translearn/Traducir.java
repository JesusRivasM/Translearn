package com.example.translearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class Traducir extends AppCompatActivity {

    Button botonTraducir;

    TextInputEditText textoATraducir,textoTraducido;

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traducir);
        botonTraducir = findViewById(R.id.botonTraducir);
        textoATraducir = findViewById(R.id.texATraducir);
        textoTraducido = findViewById(R.id.texTraducido);


        botonTraducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Traducir("es","fr");
            }
        });
    }



    public void Traducir(String idiomaBase,String idiomaFinal){
        String aTraducir;
        aTraducir = String.valueOf(textoATraducir.getText());

        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.fromLanguageTag(idiomaBase))
                        .setTargetLanguage(TranslateLanguage.fromLanguageTag(idiomaFinal))
                        .build();
        final Translator Traductor =
                Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        Traductor.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                // Model downloaded successfully. Okay to start translating.
                                // (Set a flag, unhide the translation UI, etc.)
                                Toast.makeText(Traducir.this, "MODELO DESCARGADO", Toast.LENGTH_SHORT).show();
                                Traductor.translate(aTraducir)
                                        .addOnSuccessListener(
                                                new OnSuccessListener<String>() {
                                                    @Override
                                                    public void onSuccess(@NonNull String translatedText) {
                                                        // Translation successful.
                                                        Toast.makeText(Traducir.this, translatedText, Toast.LENGTH_SHORT).show();
                                                        textoTraducido.setText(translatedText);
                                                        Traductor.close();
                                                    }
                                                })
                                        .addOnFailureListener(
                                                new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Error.
                                                        // ...
                                                        Toast.makeText(Traducir.this, "FALLO AL TRADUCIR", Toast.LENGTH_SHORT).show();
                                                        Traductor.close();
                                                    }
                                                });

                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Model couldn’t be downloaded or other internal error.
                                // ...
                                Toast.makeText(Traducir.this, "Error al descargar el modelo", Toast.LENGTH_SHORT).show();
                            }
                        });

    }
}
