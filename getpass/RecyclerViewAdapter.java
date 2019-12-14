package com.tomi5548319.getpass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private ArrayList<RecyclerViewItem> mRecyclerList;
    private OnItemClickListener mListener;

    RecyclerViewAdapter(ArrayList<RecyclerViewItem> recyclerList){
        mRecyclerList = recyclerList;
    }

    interface OnItemClickListener{
        void onItemClick(int position);
        void onViewClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        private int mID;
        private ImageView mImageView1;
        private TextView mTextView1;
        private ImageView mImageView2;
        private ImageView mImageView3;
        private ImageView mImageView4;

        MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mImageView1 = itemView.findViewById(R.id.recyclerImageView1);
            mTextView1 = itemView.findViewById(R.id.recyclerTextView1);
            mImageView2 = itemView.findViewById(R.id.recyclerImageView2);
            mImageView3 = itemView.findViewById(R.id.recyclerImageView3);
            mImageView4 = itemView.findViewById(R.id.recyclerImageView4);

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

            mImageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onViewClick(position);
                        }
                    }
                }
            });

            mImageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onEditClick(position);
                        }
                    }
                }
            });

            mImageView4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new MyViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        RecyclerViewItem currentItem = mRecyclerList.get(position);

        holder.mID = currentItem.getID();
        holder.mImageView1.setImageResource(currentItem.getImageResource1());
        holder.mTextView1.setText(currentItem.getText1());

    }

    @Override
    public int getItemCount() {
        return mRecyclerList.size();
    }
}