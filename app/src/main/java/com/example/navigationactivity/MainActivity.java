package com.example.navigationactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";

    String emailId;

    public TextView usernam , Mailid ;
    public ImageView profile;

    FirebaseAuth mAuth;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        //NavigationView navigationView = findViewById(R.id.nav_view);



        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={"sharathkumar.mskj.1999@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,"Regarding Students Notes App");
                intent.putExtra(Intent.EXTRA_TEXT,"");
                //intent.putExtra(Intent.EXTRA_CC,"mailcc@gmail.com");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();  */
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.upload, R.id.download,  R.id.socialMedia,
                R.id.nav_tools,  R.id.nav_share, R.id.logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                int menuId = destination.getId();

                //Toast.makeText(MainActivity.this, "Default Section is Opening..!!", Toast.LENGTH_SHORT).show();
                if (menuId == R.id.logout) {//startActivity(new Intent( , LoginActivity.class));
                    FirebaseAuth.getInstance().signOut();
                    Intent sendStuff = new Intent(MainActivity.this, LoginActivity.class);
                    //sendStuff.putExtra("logstatus", "logout");
                    startActivity(sendStuff);
                    finish();
                    //Toast.makeText(MainActivity.this, "Upload Section is Opening..!!", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(MainActivity.this , GalleryFragment.class));
                    /*
                    case R.id.download:{
                        Toast.makeText(MainActivity.this, "Download Section is Opening..!!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.chat:{
                        Toast.makeText(MainActivity.this, "Chat Section is Opening..!!", Toast.LENGTH_SHORT).show();
                        //Intent intent = new Intent(MainActivity.this , SocialMedia.class);
                        //startActivity(intent);
                        break;
                    }
                    case R.id.socialMedia:{
                        Toast.makeText(MainActivity.this, "Social Media Activity is Opening..!", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(MainActivity.this , SocialMedia.class));
                        break;
                    }
                    case R.id.nav_tools:{
                        Toast.makeText(MainActivity.this, "Tools Section is Opening..!!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.features:{
                        Toast.makeText(MainActivity.this, "Features Section is Opening..!!", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(MainActivity.this , FeaturesActivity.class));
                        break;
                    } */
                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        Intent inten = getIntent();
       emailId = inten.getStringExtra("email");
       if(emailId == null ){
           //Toast.makeText(this, "Welcome Again!", Toast.LENGTH_SHORT).show();
       }else{
           saveData();
       }
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(TEXT, "");
        storeData.setEmailId(text);

        return true;
    }

    private void saveData() {
        Toast.makeText(this, emailId, Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, emailId);
        editor.apply();
        //Toast.makeText(this, "Data Saved!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void saveDatas(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, "logout");
        editor.apply();
    }
}
