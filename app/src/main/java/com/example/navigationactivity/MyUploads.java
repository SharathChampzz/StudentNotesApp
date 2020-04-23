package com.example.navigationactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class MyUploads extends AppCompatActivity implements ImageAdapter.OnPostClickListener{

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef , profDatabaseref;
    private List<Upload> mUploads;

    private List<Upload> myUploads;

    private String[] pdfLinks;
    private String[] pdfNames;
    private String[] emails;

    private FirebaseStorage mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_uploads);

        mRecyclerView = findViewById(R.id.recycle);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress);

        mUploads = new ArrayList<>();
        //Intent inten = getIntent();
        myUploads = new ArrayList<>();
        mStorage = FirebaseStorage.getInstance();

        Context context = getApplicationContext();
        String[] collegeNames = context.getResources().getStringArray(R.array.college);
        String[] branches = context.getResources().getStringArray(R.array.branch);
        String[] sems = context.getResources().getStringArray(R.array.sem);
        String addr = "" ;
      /*  for(int i=0;i<collegeNames.length;i++){
            Log.i("Colleges : ", collegeNames[i]);
        }
        for(int i=0;i<branches.length;i++){
            Log.i("Branch : ", branches[i]);
        }
        for(int i=0;i<sems.length;i++){
            Log.i("Sem : ", sems[i]);
        }  */

        for (String collegeName : collegeNames) {
            for (String branch : branches) {
                for (String sem : sems) {
                    addr = collegeName + '/' + branch + '/' + sem;
                    Log.i("Sem : ",addr);
                    Toast.makeText(context, addr, Toast.LENGTH_SHORT).show();
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference(addr);
                }
            }
        }
                    mDatabaseRef.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Upload upload = postSnapshot.getValue(Upload.class);
                                mUploads.add(upload);
                            }
                           /* if(mUploads.size() == 0){
                                Toast.makeText(MyUploads.this, "Oops! No Notes Uploaded To this Section..!!", Toast.LENGTH_SHORT).show();
                            } */
                            pdfLinks = new String[mUploads.size()];
                            pdfNames = new String[mUploads.size()];
                            emails = new String[mUploads.size()];
                            // Links are stored wrt to position
                            for(int i=0;i<pdfLinks.length;i++){
                                pdfLinks[i] = mUploads.get(i).getImageUrl();
                                pdfNames[i] = mUploads.get(i).getName();
                                emails[i] = mUploads.get(i).getmMailId();
                                if(emails[i].equals(storeData.getEmailId())){
                                    Upload myUpload = new Upload(pdfNames[i], pdfLinks[i], emails[i]);
                                    myUploads.add(myUpload);
                                }
                                //Toast.makeText(ImagesActivity.this, pdfNames[i] + ": " +pdfLinks[i], Toast.LENGTH_SHORT).show();
                            }

                            mAdapter = new ImageAdapter(MyUploads.this, myUploads);
                            mRecyclerView.setAdapter(mAdapter);

                            mAdapter.setOnItemClickListener(MyUploads.this);

                            //mProgressCircle.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(MyUploads.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            mProgressCircle.setVisibility(View.INVISIBLE);
                        }
                    });

        //Log.i("Location : ", "Hai");
        //String storageLOcation = inten.getStringExtra("location");



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
        //Toast.makeText(this, "On Delete Click : " + position, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Deleting.. " + pdfNames[position] , Toast.LENGTH_SHORT).show();
        Upload selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(MyUploads.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

