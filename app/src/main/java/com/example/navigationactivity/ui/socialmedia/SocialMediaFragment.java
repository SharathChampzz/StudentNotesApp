package com.example.navigationactivity.ui.socialmedia;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationactivity.R;
import com.example.navigationactivity.WebViewActivty;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SocialMediaFragment extends Fragment {
    private ArrayList<ExampleItem> mExampleList;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";



    private Button buttonInsert;
    private Button buttonRemove;
    private EditText editTextInsert;
    private EditText editTextRemove;
    private EditText media;
    private EditText url;
    private TextView length;

    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FragmentActivity mFrgAct;
    private Intent mIntent;

    private SocialMediaViewModel mViewModel;

    public static SocialMediaFragment newInstance() {
        return new SocialMediaFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.social_media_fragment, container, false);
        /*
        View root = inflater.inflate(R.layout.social_media_fragment, container, false);
        Button btn = root.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "This a Social Media Activity", Toast.LENGTH_SHORT).show();
            }
        });
        return root; */
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SocialMediaViewModel.class);
        // TODO: Use the ViewModel
        mFrgAct = getActivity();
        mIntent = mFrgAct.getIntent();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createExampleList();
        buildRecyclerView();
        //mExampleList = new ArrayList<>();
        loadData();
        //mExampleList.add(new ExampleItem(R.drawable.profile, "Instagram", "https://www.instagram.com"));
        //mExampleList.add(new ExampleItem(R.drawable.profile, "Click On this", "https://sites.google.com/view/sjceec/vlsi"));
        //saveData();
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ExampleAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ExampleAdapter.OnPostClickListener() {
            @Override
            public void onItemClick(int position) {
               //changeItem(position,"Clicked!");
              String MediaName =  mExampleList.get(position).getText1();
              String Url =  mExampleList.get(position).getText2();


              try{
                  Toast.makeText(mFrgAct, "Media Name : " + MediaName + '\n' + "Link : " + Url, Toast.LENGTH_SHORT).show();
                  Intent sendStuff = new Intent(getContext(), WebViewActivty.class);
                  sendStuff.putExtra("url", Url);
                  startActivity(sendStuff);
              }
              catch (Exception e){
                  Toast.makeText(mFrgAct, e.toString(), Toast.LENGTH_SHORT).show();
              }

            }

            @Override
            public void onDownloadClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {

            }
        });



        buttonInsert = view.findViewById(R.id.button_insert);
        buttonRemove = view.findViewById(R.id.button_remove);
        editTextInsert = view.findViewById(R.id.edittext_insert);
        editTextRemove = view.findViewById(R.id.edittext_remove);
        media = view.findViewById(R.id.mediaName);
        url = view.findViewById(R.id.url);


        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(editTextInsert.getText().toString());
                insertItem(position-1);

            }
        });

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(editTextRemove.getText().toString());
                removeItem(position-1);

            }
        });


    }

    private void insertItem(int position) {
        String mediaName = media.getText().toString();
        String Link = url.getText().toString();
        if(mediaName.equals("")){
            Toast.makeText(mFrgAct, "Enter Media Name", Toast.LENGTH_SHORT).show();
        }
        else if(Link.equals("")){
            Toast.makeText(mFrgAct, "Enter Corresponding URL", Toast.LENGTH_SHORT).show();
        }
        else {

            try{
                mExampleList.add(position, new ExampleItem(R.drawable.profile, mediaName, Link));
                mAdapter.notifyItemInserted(position);
                saveData();
            }
            catch (Exception e){
                Toast.makeText(mFrgAct, "Enter Proper Position", Toast.LENGTH_SHORT).show();
            }

        }


    }
    private void saveData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mExampleList);
        editor.putString("task list", json);
        editor.apply();
        Toast.makeText(getContext(), "Data Saved!!", Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<ExampleItem>>() {}.getType();
        mExampleList = gson.fromJson(json, type);

        if (mExampleList == null) {
            mExampleList = new ArrayList<>();
        }
    }

    private void removeItem(int position) {
        try{
            mExampleList.remove(position);
            mAdapter.notifyItemRemoved(position);
            saveData();
        }
        catch (Exception e){
            Toast.makeText(mFrgAct, "Enter Proper Position", Toast.LENGTH_SHORT).show();
        }

    }

    public void changeItem(int position, String text) {
        mExampleList.get(position).changeText1(text);
        mAdapter.notifyItemChanged(position);
    }
    private void createExampleList() {

    }

    private void buildRecyclerView() {


    }
}
