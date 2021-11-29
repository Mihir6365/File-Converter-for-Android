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

import com.aspose.pdf.facades.PdfFileEditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MergePDF extends AppCompatActivity {
    private final String storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMHHmmss");
    LocalDateTime now;
    String outputPDF;
    ProgressBar progressBar;
    TextView text2;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==10){
            if(resultCode==RESULT_OK&&null != data) {
                now = LocalDateTime.now();
                outputPDF=storageDir+dtf.format(now)+".pdf";
                if (null != data.getClipData()) {
                    InputStream[] list =new InputStream[data.getClipData().getItemCount()];
                    PdfFileEditor fileEditor=new PdfFileEditor();
                    for(int i=0;i<data.getClipData().getItemCount();i++){
                        try {
                            list[i]=getContentResolver().openInputStream(data.getClipData().getItemAt(i).getUri());
                        } catch (FileNotFoundException e) {
                            text2.setText("Error. please check the app storage permissions");
                        }
                    }
                    try {
                        OutputStream out=new FileOutputStream(outputPDF);
                        fileEditor.concatenate(list,out);
                        text2.setText("File saved in downloads folder. you can convert more files by pressing upload button");
                    } catch (FileNotFoundException e) {
                        text2.setText("Error. please check the app storage permissions");
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mergepdf);

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        text2=findViewById(R.id.text3);
        progressBar=findViewById(R.id.progressBar);
        ImageButton PDFEXCEL=findViewById(R.id.imagebutton3);
        PDFEXCEL.setOnClickListener(v -> {
            Intent myfileopener = new Intent(Intent.ACTION_GET_CONTENT);
            myfileopener.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            myfileopener.setType("*/*");
            startActivityForResult(Intent.createChooser(myfileopener,"Choose File"),10);
            progressBar.setVisibility(View.VISIBLE);
        });

    }
}