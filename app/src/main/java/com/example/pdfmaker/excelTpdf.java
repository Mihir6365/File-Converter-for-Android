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
import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class excelTpdf extends AppCompatActivity {
    private final String storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMHHmmss");
    LocalDateTime now;
    String outputdoc;
    HSSFWorkbook workbook;
    ProgressBar progressBar;
    TextView text7;
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==10){
            if(resultCode==RESULT_OK&&null != data) {
                now = LocalDateTime.now();
                outputdoc=storageDir+dtf.format(now)+".pdf";
                try (InputStream inputStream = getContentResolver().openInputStream(data.getData())) {
                    workbook=new HSSFWorkbook(inputStream);
                    HSSFSheet worksheet = workbook.getSheetAt(0);
                    Iterator<Row> rowIterator = worksheet.iterator();
                    Document pdfdoc = new Document();
                    PdfWriter.getInstance(pdfdoc, new FileOutputStream(outputdoc));
                    pdfdoc.open();
                    PdfPTable my_table = new PdfPTable(worksheet.getRow(0).getLastCellNum());
                    PdfPCell table_cell;
                    while(rowIterator.hasNext()) {
                        Row row = rowIterator.next();
                        Iterator<Cell> cellIterator = row.cellIterator();
                        while(cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            switch(cell.getCellType()) {
                                case STRING:
                                    table_cell=new PdfPCell(new Phrase(cell.getStringCellValue()));
                                    my_table.addCell(table_cell);
                                    break;
                                case FORMULA:
                                    table_cell=new PdfPCell(new Phrase(cell.getCellFormula()));
                                    my_table.addCell(table_cell);
                                    break;
                                case BLANK:
                                    table_cell=new PdfPCell(new Phrase(" "));
                                    my_table.addCell(table_cell);
                                    break;
                                case NUMERIC:
                                    table_cell=new PdfPCell(new Phrase(String.valueOf(cell.getNumericCellValue())));
                                    my_table.addCell(table_cell);
                                    break;
                            }

                        }

                    }
                    pdfdoc.add(my_table);
                    pdfdoc.close();
                    pdfdoc.close();
                    text7.setText("File saved in downloads folder. You can convert more files by pressing upload button.");
                    progressBar.setVisibility(View.INVISIBLE);

                }catch (Exception e){
                   text7.setText("Error! Check storage permissions and file type.");
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel_tpdf);

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        text7=findViewById(R.id.text7);
        progressBar=findViewById(R.id.progressBar);
        ImageButton EXCELPDF=findViewById(R.id.imagebutton3);
        EXCELPDF.setOnClickListener(v->{
            Intent myfileopener = new Intent(Intent.ACTION_GET_CONTENT);
            myfileopener.setType("*/*");
            startActivityForResult(Intent.createChooser(myfileopener,"Choose File"),10);
            progressBar.setVisibility(View.VISIBLE);
        });
    }
}