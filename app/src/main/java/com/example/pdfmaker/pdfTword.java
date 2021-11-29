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

import com.aspose.pdf.Document;
import com.aspose.pdf.SaveFormat;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class pdfTword extends AppCompatActivity {
    private final String storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMHHmmss");
    LocalDateTime now;
    String outputdoc;
    ProgressBar progressBar;
    TextView text4;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==10){
            if(resultCode==RESULT_OK&&null != data) {
                now = LocalDateTime.now();
                outputdoc=storageDir+dtf.format(now)+".doc";
                try (InputStream inputStream = getContentResolver().openInputStream(data.getData())) {
                   Document pdf = new Document(inputStream);
                   pdf.save(outputdoc, SaveFormat.Doc);
                   progressBar.setVisibility(View.GONE);
                    text4.setText("File saved in Downloads folder. You can convert other files by pressing upload button");
                }catch (Exception e){
                    text4.setText("Error. Check Storage permission or Document format");
                    progressBar.setVisibility(View.GONE);
                }

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_tword);

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        progressBar=findViewById(R.id.progressBar);
        text4=findViewById(R.id.text4);
        ImageButton imgb=findViewById(R.id.imageButton4);
        imgb.setOnClickListener(v->{
            Intent myfileopener = new Intent(Intent.ACTION_GET_CONTENT);
            myfileopener.setType("*/*");
            startActivityForResult(Intent.createChooser(myfileopener,"Choose File"),10);
            progressBar.setVisibility(View.VISIBLE);
        });
    }
}