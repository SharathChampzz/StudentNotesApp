package com.example.navigationactivity.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import androidx.lifecycle.ViewModelProviders;

import com.example.navigationactivity.ImagesActivity;
import com.example.navigationactivity.R;

public class GalleryFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentActivity mFrgAct;
    private Intent mIntent;
   private Button btn;
    private Spinner spinner,spinner2,spinner3;
    String concat ;
    String clg;
    String bran;
    String se;
    String d;

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        return inflater.inflate(R.layout.fragment_gallery, container, false);
            /*
        Button down = root.findViewById(R.id.downl);

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "New Activity is yet to be Created..!!", Toast.LENGTH_SHORT).show();
            }
        });

        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;

             */
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinner = view.findViewById(R.id.spinner);
        spinner2 = view.findViewById(R.id.spinner2);
        spinner3 = view.findViewById(R.id.spinner3);
        btn = view.findViewById(R.id.downl);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.college, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.branch, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(),R.array.sem, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clgName = spinner.getSelectedItem().toString();
                String branchName = spinner2.getSelectedItem().toString();
                String semester = spinner3.getSelectedItem().toString();
                d = clgName + '/' + branchName + '/' + semester;
                if(clgName.equals("") || branchName.equals("") || semester.equals("")){
                    Toast.makeText(getContext(), "Please Select ALL the Sections", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(),"Show Results For :" + d, Toast.LENGTH_SHORT).show();
                    Intent sendStuff = new Intent(getContext(), ImagesActivity.class);
                    sendStuff.putExtra("location", d);
                    startActivity(sendStuff);
                }

                //startActivity(new Intent(getContext(), ImagesActivity.class));
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFrgAct = getActivity();
        mIntent = mFrgAct.getIntent();
        //Intent intent = new Intent(getActivity().getIntent());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}