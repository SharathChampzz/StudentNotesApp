package com.example.navigationactivity.ui.profile;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.navigationactivity.DeleteDetails;
import com.example.navigationactivity.ImagesActivity;
import com.example.navigationactivity.Info;
import com.example.navigationactivity.InfoActivity;
import com.example.navigationactivity.MyUploads;
import com.example.navigationactivity.R;
import com.example.navigationactivity.Upload;
import com.example.navigationactivity.storeData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private DatabaseReference mDatabaseRef;
    private FirebaseStorage mStorage;

    private ProfileViewModel mViewModel;
    private String emailId;

    Button edit ;
    ImageView profile;
    TextView name ,  info;

    String username , information , profilepicurl;

    //private FirebaseStorage mStorage;
    //mStorage =


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edit = view.findViewById(R.id.edit);
        profile = view.findViewById(R.id.profilePic);
        name = view.findViewById(R.id.infoName);
        info = view.findViewById(R.id.inform);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getContext(), InfoActivity.class));
                try{
                    Intent sendStuff = new Intent(getContext(), InfoActivity.class);
                    String[] information = new String[2];
                    information[0] = name.getText().toString();
                    information[1] = info.getText().toString();
                    sendStuff.putExtra("info", information);
                    startActivity(sendStuff);
                }
                catch (Exception e){
                    startActivity(new Intent(getContext() , InfoActivity.class));
                }

            }
        });

        mStorage = FirebaseStorage.getInstance();
        //String email = storeData.getEmailId();
        String path = "users"; // + '/' + email;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(path);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    int count = (int) dataSnapshot.getChildrenCount();
                    String[] keys = new String[count];
                    String[] urls = new String[count];
                    int i=0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Info informat = postSnapshot.getValue(Info.class);
                        if(storeData.getEmailId().equals(informat.getMail())){
                            profilepicurl = informat.getUrl();
                            keys[i] = postSnapshot.getKey();
                            urls[i] = profilepicurl;
                            i++;
                            information = informat.getInfo();
                            username = informat.getUsername();
                            storeData.setUsername(username);
                            storeData.setProfileUrl(profilepicurl);
                            name.setText(username);
                            info.setText(information);
                            Picasso
                                    .with(getContext())
                                    .load(profilepicurl)
                                    .into(profile);
                        }

                    }
                    for (int x=0;x<i-1;x++){
                        delete(keys[x]);
                        Toast.makeText(getContext(), "Previous Info Deleted!", Toast.LENGTH_SHORT).show();

                        StorageReference storref = mStorage.getReferenceFromUrl(urls[x]);
                        storref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Also Deleted from Storage! You cant retrieved that back!", Toast.LENGTH_SHORT).show();
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Failed to Delete from Storage!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
                catch (Exception e){
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void delete(String selectedKey) {
        mDatabaseRef.child(selectedKey).removeValue();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
           emailId = storeData.getEmailId();
            Toast.makeText(getContext(), emailId , Toast.LENGTH_SHORT).show();


        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel


    }

}
