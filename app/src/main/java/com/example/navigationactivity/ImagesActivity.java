package com.example.navigationactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity implements ImageAdapter.OnPostClickListener {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;

    private String[] pdfLinks;
    private String[] pdfNames;
    private String[] emailId;

    private FirebaseStorage mStorage;
    //private FirebaseDatabase mFirebaseStorage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();

        mAdapter = new ImageAdapter(ImagesActivity.this, mUploads);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(ImagesActivity.this);

        Intent inten = getIntent();
        String storageLOcation = inten.getStringExtra("location");

        mStorage = FirebaseStorage.getInstance();
        String path = "uploads/" + storageLOcation ;
        try{
            mDatabaseRef = FirebaseDatabase.getInstance().getReference(path);

            //Toast.makeText(getApplicationContext(), "Data Base Reference : " + storageLOcation, Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            //mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                if(mUploads.size() == 0){
                    Toast.makeText(ImagesActivity.this, "Oops! No Notes Uploaded To this Section..!!", Toast.LENGTH_SHORT).show();
                }

                pdfLinks = new String[mUploads.size()];
                pdfNames = new String[mUploads.size()];
                emailId = new String[mUploads.size()];
                // Links are stored wrt to position
                for(int i=0;i<pdfLinks.length;i++){
                    pdfLinks[i] = mUploads.get(i).getImageUrl();
                    pdfNames[i] = mUploads.get(i).getName();
                    try {
                        emailId[i] = mUploads.get(i).getmMailId();
                    }
                    catch (Exception e){
                        Log.i("Exception",e.toString());
                        emailId[i] = "";
                    }

                }


                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ImagesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Opening Document.. : " + pdfNames[position], Toast.LENGTH_SHORT).show();
        Intent sendStuff = new Intent(this, ViewPdf.class);
        sendStuff.putExtra("url", pdfLinks[position]);
        startActivity(sendStuff);
        //Toast.makeText(this, pdfLinks[position], Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloadClick(int position) {
        Toast.makeText(getApplicationContext(), "Directing to Browser... FileName : " + pdfNames[position], Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(pdfLinks[position]));
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {

        String user = storeData.getEmailId();

        //Toast.makeText(this, "On Delete Click : " + position, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Deleting.. " + pdfNames[position] , Toast.LENGTH_SHORT).show();
        Upload selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKey();
        final String mailId = selectedItem.getmMailId();
        if(mailId.equals("")){
            Toast.makeText(this, "No Mail ID Found For this", Toast.LENGTH_SHORT).show();
        }
        if(mailId.equals(user) || (user.equals("admin123@gmail.com"))){
            StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mDatabaseRef.child(selectedKey).removeValue();
                    Toast.makeText(ImagesActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                }
            });


            StorageReference storref = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
            storref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ImagesActivity.this, "Also Deleted from Storage! You cant retrieved that back!", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ImagesActivity.this, "Failed to Delete from Storage!", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
        else{
            Toast.makeText(this, "You Have No Permission To delete!", Toast.LENGTH_SHORT).show();
        }



    }
}
