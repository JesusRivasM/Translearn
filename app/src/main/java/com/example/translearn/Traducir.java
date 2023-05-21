package com.example.translearn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
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

    private Button botonTraducir, botonPasar,botonFotoPermisos,botonGuardar;
    private TextInputEditText textoATraducir,textoTraducido;
    private Bitmap imageBitmap;

    private DatabaseReference reference;

    static  final int REQUEST_IMAGE_CAPTURE=1;
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
        botonPasar = findViewById(R.id.modoFoto);
        botonPasar = findViewById(R.id.modoFoto);
        botonFotoPermisos = findViewById(R.id.fotoYPermisos);
        botonGuardar = findViewById(R.id.guardarTraduc);
        reference = FirebaseDatabase.getInstance().getReference();
        botonTraducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Traducir("es","fr");
            }
        });

        botonFotoPermisos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (verificarPermisos()){
                    hacerFoto();
                    hacerFoto();
                }else{
                    pedirPermisos();
                }
            }
        });
        botonPasar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                detectarTexto();
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
            imageBitmap=(Bitmap)  extras.get("data");
            //captureIV.setImageBitmap(imageBitmap);
        }
    }

    private void detectarTexto() {
        InputImage image = InputImage.fromBitmap(imageBitmap,0);
        TextRecognizer recognizer= TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> result= recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {
                StringBuilder result = new StringBuilder();
                for(Text.TextBlock block: text.getTextBlocks()){
                    String blockText = block.getText();
                    Point[] blockCornerPoint = block.getCornerPoints();
                    Rect blockframe = block.getBoundingBox();
                    for (Text.Line line : block.getLines()){
                        String lineText = line.getText();
                        Point[] lineCornerPoiint = line.getCornerPoints();
                        Rect linRect = line.getBoundingBox();
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
                Toast.makeText(Traducir.this, "Fail to detect text from image" +e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
