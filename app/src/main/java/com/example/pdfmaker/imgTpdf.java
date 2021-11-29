package com.example.pdfmaker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class imgTpdf extends AppCompatActivity {
    InputStream img;
    Uri[] uri;
    int num;
    final String directory = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMHHmmss");
    LocalDateTime now;
    String pdfFile;
    File myPDFFile;
    TextView textView1;

    @SuppressLint({"MissingSuperCall", "SetTextI18n"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                if (null != data) {
                    now = LocalDateTime.now();
                    pdfFile = directory + "/"+dtf.format(now)+".pdf";
                    ProgressBar progressBar=findViewById(R.id.progressBar);
                    myPDFFile = new File(pdfFile);
                    PdfDocument pdfDocument = new PdfDocument();

                    if (null != data.getClipData()) {
                        num = data.getClipData().getItemCount();
                        uri=new Uri[num];
                        progressBar.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), num + " Files selected", Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < num; i++) {
                            uri[i]=data.getClipData().getItemAt(i).getUri();
                            Bitmap bitmap = null;
                            try {
                                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri[i]));
                            } catch (FileNotFoundException e) {
                                textView1.setText("Error converting the files. please check the storage permissions.");
                            }

                            assert bitmap != null;
                            PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth()+4, bitmap.getHeight()+4, i+1).create();
                            PdfDocument.Page page = pdfDocument.startPage(myPageInfo);
                            page.getCanvas().drawBitmap(bitmap, 2, 2, null);
                            pdfDocument.finishPage(page);
                        }

                    } else {
                        try {
                            progressBar.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "1 file selected", Toast.LENGTH_SHORT).show();
                            img = getContentResolver().openInputStream(data.getData());
                            Bitmap bitmap = BitmapFactory.decodeStream(img);
                            PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth()+4, bitmap.getHeight()+4, 1).create();
                            PdfDocument.Page page = pdfDocument.startPage(myPageInfo);
                            page.getCanvas().drawBitmap(bitmap, 2, 2, null);
                            pdfDocument.finishPage(page);

                        } catch (FileNotFoundException e) {
                            textView1.setText("Error converting the files. please check the storage permissions.");
                        }
                    }
                    try {
                        pdfDocument.writeTo(new FileOutputStream(myPDFFile));
                        progressBar.setVisibility(View.GONE);
                        textView1.setText("File saved in downloads folder.You can convert other files by pressing upload button");
                    } catch (IOException e) {
                        textView1.setText("Error converting the files. please check the storage permissions.");
                    }
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_tpdf);
        textView1=findViewById(R.id.text1);
        

        ImageButton ims=findViewById(R.id.imageButton1);
        ims.setOnClickListener(v -> {
            Intent myfileopener = new Intent(Intent.ACTION_GET_CONTENT);
            myfileopener.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            myfileopener.setType("image/*");
            startActivityForResult(Intent.createChooser(myfileopener,"Choose File"),10);
        });
    }
}