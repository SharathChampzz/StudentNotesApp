package com.example.navigationactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class InfoActivity extends AppCompatActivity {

    Button pick , finish;
    ImageView img;
    EditText name , info;

    Bitmap bitmap;
    public static final int PICK_IMAGE = 1;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private String defaulturl = "https://firebasestorage.googleapis.com/v0/b/fir-upload-6e24b.appspot.com/o/DefaultProfilePic%2Flogo.jpg?alt=media&token=8f33e2d7-e4d7-4ddb-8b52-c45d66f297c8";
    private FirebaseStorage mStorage;

    private StorageTask mUploadTask;

    private Uri uri;
    String emailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent inten = getIntent();
        emailId = inten.getStringExtra("email");

        storeData.setEmailId(emailId);
        pick =  findViewById(R.id.pickImage);
        finish = findViewById(R.id.finish);
        img = findViewById(R.id.profilePic);
        name = findViewById(R.id.user);
        info = findViewById(R.id.information);

        Intent intent = getIntent();
        String[] information = new String[2];
        information = intent.getStringArrayExtra("info");
        try{
            assert information != null;
            name.setText(information[0]);
            info.setText(information[1]);
        }
        catch (Exception e){
            name.setText("");
            info.setText("");
        }

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    selectImage();
                }
                catch (Exception e){
                    Toast.makeText(InfoActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String email = storeData.getEmailId();

                try{
                    String path = "users"; // + '/' + email;
                    mStorageRef = FirebaseStorage.getInstance().getReference(path);
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference(path);
                        uploadFile();

                }
                catch (Exception e){
                    Toast.makeText(InfoActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    private void uploadFile() {

        if (uri != null){

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(uri));

            mUploadTask = fileReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                }
                            }, 500);

                            Toast.makeText(InfoActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uri.isComplete());
                            Uri url = uri.getResult();
                            storeData.setUsername(name.getText().toString());
                            storeData.setProfileUrl(url.toString());
                            Info information = new Info(url.toString() , name.getText().toString(), info.getText().toString()  , storeData.getEmailId()) ;
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(information);
                            //startActivity(new Intent(InfoActivity.this , MainActivity.class));

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(InfoActivity.this, "Upload Failed!", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
            finish();
            Intent inten = new Intent(InfoActivity.this , MainActivity.class);
            inten.putExtra("email", emailId);
            startActivity(inten);
        }


        else{
             Toast.makeText(this, "Pick profile pic and give a try!", Toast.LENGTH_SHORT).show();
        }

    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    protected final void onActivityResult(final int requestCode, final int
            resultCode, final Intent i) {
        super.onActivityResult(requestCode, resultCode, i);

        // this matches the request code in the above call
        if (requestCode == 1) {
            uri = i.getData();
            // this will be null if no image was selected...
            if (uri != null) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    img.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
