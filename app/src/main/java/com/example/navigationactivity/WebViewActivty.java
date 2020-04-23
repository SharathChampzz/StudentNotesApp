package com.example.navigationactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.example.navigationactivity.ui.socialmedia.SocialMediaFragment;

public class WebViewActivty extends AppCompatActivity {
    private WebView web_view;
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_activty);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        web_view = findViewById(R.id.webv);
        exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                try{
                    startActivity(new Intent(WebViewActivty.this  , MainActivity.class));
                }
                catch (Exception e){
                    Toast.makeText(WebViewActivty.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        web_view.setVerticalScrollBarEnabled(true);
        web_view.requestFocus();
        web_view.getSettings().setDefaultTextEncodingName("utf-8");
        web_view.getSettings().setJavaScriptEnabled(true);
        Intent inten = getIntent();
        String url = inten.getStringExtra("url");
        try{
            web_view.loadUrl(url);
        }
        catch (Exception e){
           // web_view.loadUrl("https:www.google.com/");
            Toast.makeText(this, "Error In Loading URL \n" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        web_view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        web_view.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {
                    progressDialog.show();
                }
                if (progress == 100) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    long back_pressed;
    @Override
    public void onBackPressed() {
        if (back_pressed + 1000 > System.currentTimeMillis()){
            super.onBackPressed();
        }
        else{
            if(web_view!= null && web_view.canGoBack()){
                web_view.goBack();
            }
                // if there is previous page open it
            else{
                super.onBackPressed();//if there is no previous page, close app
                //finish();
                //startActivity(new Intent(WebViewActivty.this , SocialMediaFragment.class));
            }
        }
        back_pressed = System.currentTimeMillis();

    }


}
