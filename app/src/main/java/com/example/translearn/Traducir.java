package com.example.translearn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.translearn.modelos.Traduccion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;


public class Traducir extends AppCompatActivity {

    private Button botonTraducir;
    private ImageButton botonGuardar,botonFotoPermisos;
    private TextInputEditText textoATraducir,textoTraducido;
    private Bitmap bitmap;
    private DatabaseReference reference;
    static  final int REQUEST_IMAGE_CAPTURE=1;
    @Override
    public void onStart() {
        super.onStart();

    }
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

        setContentView(R.layout.activity_traducir);
        botonTraducir = findViewById(R.id.botonTraducir);
        textoATraducir = findViewById(R.id.texATraducir);
        textoTraducido = findViewById(R.id.texTraducido);
        botonGuardar = findViewById(R.id.guardarTraduc);
        botonFotoPermisos = findViewById(R.id.fotoYPermisos);
        reference = FirebaseDatabase.getInstance().getReference();
        botonTraducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Traducir("fr","es");
            }
        });

        botonFotoPermisos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (verificarPermisos()){
                    hacerFoto();
                }else{
                    pedirPermisos();
                }
                new CountDownTimer(2500, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                    }
                }.start();
            }
        });

    botonGuardar.setOnClickListener(new View.OnClickListener() {
        @Override
                public void onClick(View v){
                Guardar();
        }
        });
    }
    public void Guardar(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Traduccion traduccion = new Traduccion(textoATraducir.getText().toString(),textoTraducido.getText().toString());
        String idTraduccion = reference.child("usuarios").child(uid).child("traducciones").push().getKey();
        reference.child("usuarios").child(uid).child("traducciones").child(idTraduccion).setValue(traduccion);
        Toast.makeText(Traducir.this, "guardado con exito", Toast.LENGTH_SHORT).show();
    }

    //METODO OBTENIDO EN PARTE GRACIAS A LA LIBRERÍA DE FIREBASE ML KIT.
    public void Traducir(String idiomaBase, String idiomaFinal){
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
                                                        Toast.makeText(Traducir.this, "FALLO AL TRADUCIR", Toast.LENGTH_SHORT).show();
                                                        Traductor.close();
                                                    }
                                                });

                            }
                        })
                .addOnFailureListener(
                        e -> {
                            // Model couldn’t be downloaded or other internal error.
                            // ...
                            Toast.makeText(Traducir.this, "Error al descargar el modelo", Toast.LENGTH_SHORT).show();
                        });

    }

    private  boolean verificarPermisos() {
        int camerPermission= ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA);
        return camerPermission == PackageManager.PERMISSION_GRANTED;

    }
    private  void pedirPermisos(){
        int PERMISSION_CODE=200;
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PERMISSION_CODE);

    }

    private  void hacerFoto(){
        Intent hacerFoto= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (hacerFoto.resolveActivity(getPackageManager())!=null) {
            startActivityForResult(hacerFoto,REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0){
            boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if(cameraPermission){
                Toast.makeText(this, "Permisos adquiridos", Toast.LENGTH_SHORT).show();
                hacerFoto();
            }else{
                Toast.makeText(this, "Permissions denegados", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras= data.getExtras();
            bitmap=(Bitmap)  extras.get("data");
            detectarTexto(bitmap);
        }

    }

    private void detectarTexto(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap,0);
        TextRecognizer textRecognizer= TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> result= textRecognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {
                StringBuilder result = new StringBuilder();
                for(Text.TextBlock block: text.getTextBlocks()){
                    String blockText = block.getText();
                    for (Text.Line line : block.getLines()){
                        for (Text.Element element : line.getElements()){
                            String elementText = element.getText();
                            result.append(elementText);
                        }
                        textoATraducir.setText(blockText);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Traducir.this, "No se pudo detectar una imagen" +e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
