package com.example.navigationactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.navigationactivity.ui.gallery.GalleryFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";

    FirebaseAuth mAuth;
    String username;

    Button login,signup , forgot;
    EditText mail, password;
    ProgressBar loading;

    private String loginStatus;
    //private FirebaseAuth mAuth;



    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            finish();
            Intent inten = new Intent(LoginActivity.this, MainActivity.class);
            //inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           // inten.putExtra("email", username);
            startActivity(inten);
            //startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        login = findViewById(R.id.login);
        signup = findViewById(R.id.noAccount);
        mail = findViewById(R.id.pdfname);
        password = findViewById(R.id.password);
        loading = findViewById(R.id.pbar);
        forgot = findViewById(R.id.forgot);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = mail.getText().toString().trim();
                String pword = password.getText().toString().trim();

                if(username.equals("")){
                    mail.setError("Email is Required..!!");
                    mail.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
                    mail.setError("PLease Enter Valid Email Address");
                    mail.requestFocus();
                }
                else if(pword.equals("")){
                    password.setError("Password is required..!!");
                    password.requestFocus();
                }
                else if(pword.length() < 6){
                    password.setError("Minimum Password Length is 6 Characters");
                    password.requestFocus();
                }
                else{
                    loading.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(username, pword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            loading.setVisibility(View.GONE);
                            if(task.isSuccessful()){
                                storeData.setEmailId(username);
                                finish();
                                Toast.makeText(getApplicationContext(), "Login Successfull", Toast.LENGTH_SHORT).show();
                                loginStatus = "login";
                                saveData();
                                Intent inten = new Intent(LoginActivity.this, MainActivity.class);
                                inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                inten.putExtra("email", username);
                                startActivity(inten);
                                finish();

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String err = e.getLocalizedMessage();
                            Toast.makeText(getApplicationContext(), err, Toast.LENGTH_SHORT).show();
                        }
                    });

                }


            }
        });




        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent inten = new Intent(LoginActivity.this , SignUpActivity.class);
                startActivity(inten);
            }
        });
    }

    private void sendEmail() {
        String usermail = mail.getText().toString();
        if(usermail.equals("")){
            Toast.makeText(this, "Please Enter Your Email ID and then try Again!", Toast.LENGTH_SHORT).show();
        }
        else{
            mAuth.sendPasswordResetEmail(usermail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Check Your Mail to reset Password!", Toast.LENGTH_SHORT).show();
                    }
                    else if (task.isCanceled()){
                        Toast.makeText(LoginActivity.this, "An Error Occured Try Later!!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, loginStatus);
        editor.apply();
    }
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(TEXT, "");
        storeData.setLogStatus(loginStatus);
    }
}
