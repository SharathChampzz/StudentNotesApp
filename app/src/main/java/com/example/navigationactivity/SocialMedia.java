package com.example.navigationactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.navigationactivity.ui.socialmedia.SocialMediaFragment;

public class SocialMedia extends AppCompatActivity {
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_media_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SocialMediaFragment.newInstance())
                    .commitNow();
        }
        txt = findViewById(R.id.textView2);
        txt.setText("Hai -- From Social Media");
    }
}
