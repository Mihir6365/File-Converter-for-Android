package com.example.pdfmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton b1=findViewById(R.id.info);
        b1.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),info.class)));

        ImageButton b2=findViewById(R.id.imgtopdf);
        b2.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),imgTpdf.class)));

        ImageButton b3=findViewById(R.id.mergepdf);
        b3.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MergePDF.class)));

        ImageButton b4=findViewById(R.id.exceltopdf);
        b4.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),excelTpdf.class)));

        ImageButton b5=findViewById(R.id.pdftoword);
        b5.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),pdfTword.class)));

        ImageButton b6=findViewById(R.id.pdftoexcel);
        b6.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),pdfTexcel.class)));

        ImageButton b7=findViewById(R.id.wordtopdf);
        b7.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),wordTpdf.class)));
    }
}