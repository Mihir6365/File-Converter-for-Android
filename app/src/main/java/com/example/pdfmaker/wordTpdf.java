package com.example.pdfmaker;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.aspose.words.Document;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class wordTpdf extends AppCompatActivity {
    ProgressBar progressBar;

    private final String storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMHHmmss");
    LocalDateTime now;
    String outputPDF;
    TextView text5;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==10){
            if(resultCode==RESULT_OK&&null != data) {
                try (InputStream inputStream = getContentResolver().openInputStream(data.getData())) {
                    now = LocalDateTime.now();
                    outputPDF=storageDir+dtf.format(now)+".pdf";
                    Document doc=new Document(inputStream);
                    doc.save(outputPDF);
                    text5.setText("File saved in downloads folder. You can convert other files by pressing upload button");
                    progressBar.setVisibility(View.GONE);
                }catch (Exception e){
                    text5.setText("Error.Check the File type and storage permissions");
                    progressBar.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_tpdf);

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        progressBar=findViewById(R.id.progressBar);
        text5=findViewById(R.id.text5);
        ImageButton WORDPDF=findViewById(R.id.imagebutton3);
        WORDPDF.setOnClickListener(v->{
            Intent myfileopener = new Intent(Intent.ACTION_GET_CONTENT);
            myfileopener.setType("*/*");

            startActivityForResult(Intent.createChooser(myfileopener,"Choose File"),10);
            progressBar.setVisibility(View.VISIBLE);
        });
    }
}