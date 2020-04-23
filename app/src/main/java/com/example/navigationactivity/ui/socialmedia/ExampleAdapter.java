package com.example.navigationactivity.ui.socialmedia;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationactivity.ImageAdapter;
import com.example.navigationactivity.R;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<ExampleItem> mExampleList;

    private OnPostClickListener mListener;

    public interface OnPostClickListener{
        void onItemClick(int position);

        void onDownloadClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(ExampleAdapter.OnPostClickListener listener){
        mListener = listener;

    }



    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView count;
        public TextView mTextView1;
        public TextView mTextView2;

        public ExampleViewHolder(@NonNull View itemView, final OnPostClickListener listener ) {
            super(itemView);
            count = itemView.findViewById(R.id.itemNo);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }


                }
            });



        }
    }

    public ExampleAdapter(ArrayList<ExampleItem> exampleList) {
        mExampleList = exampleList;
    }
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);
        String elementNumber  = String.valueOf(position + 1);
        holder.count.setText(elementNumber);
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
