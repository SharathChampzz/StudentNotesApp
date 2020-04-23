package com.example.navigationactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;




import android.content.Intent;
import android.os.AsyncTask;

import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ViewPdf extends AppCompatActivity {

    private TextView txt;
    private PDFView pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);
        pdf = findViewById(R.id.ViewPdf);
        txt = findViewById(R.id.txt);

        Intent inten = getIntent();
        String pdfUrl = inten.getStringExtra("url");
        //Toast.makeText(this, pdfUrl, Toast.LENGTH_SHORT).show();


        try{
            new RetrievePdfStream().execute(pdfUrl);
        }
        catch (Exception e){
            Toast.makeText(this, "Failed to load Url :" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    class RetrievePdfStream extends AsyncTask<String, Void, InputStream>{
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());

                }
            } catch (IOException e) {
                return null;

            }
            return inputStream;
        }
        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdf.fromStream(inputStream).load();
        }
    }
}
