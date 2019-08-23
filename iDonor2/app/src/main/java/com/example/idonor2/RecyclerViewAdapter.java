package com.example.idonor2;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    List<CardContent> results;
    int buttonvalue=0;
    String email=null,phone=null;
    private OnNoteListener monNoteListener;
    RecyclerViewAdapter(List<CardContent> results, OnNoteListener onNoteListener)
    {
        this.monNoteListener=onNoteListener;
        this.results=results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout,viewGroup,false);
        ViewHolder cvh=new ViewHolder(view,monNoteListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.nameofuser.setText(results.get(i).name);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView nameofuser;
        Button emailtouser,phonetouser,messagetouser;
        OnNoteListener onNoteListener;
        public ViewHolder(@NonNull View itemView, final OnNoteListener onNoteListener) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cardView);
            nameofuser=itemView.findViewById(R.id.nameofUser);
            emailtouser=itemView.findViewById(R.id.emailtouser);
            phonetouser=itemView.findViewById(R.id.phonetouser);
            messagetouser=itemView.findViewById(R.id.messagetouser);
            messagetouser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phone= results.get(getAdapterPosition()).phone;
                    buttonvalue=3;
                    onNoteListener.onNoteClick(getAdapterPosition(),buttonvalue,email,phone);
                }
            });
            emailtouser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    email= results.get(getAdapterPosition()).email;
                    buttonvalue=1;
                    onNoteListener.onNoteClick(getAdapterPosition(),buttonvalue,email,phone);
                }
            });
            phonetouser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phone= results.get(getAdapterPosition()).phone;
                    buttonvalue=2;
                    onNoteListener.onNoteClick(getAdapterPosition(),buttonvalue,email,phone);
                }
            });
            itemView.setOnClickListener(this);
            this.onNoteListener=onNoteListener;
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition(),buttonvalue,email,phone);
        }
    }
    public interface OnNoteListener
    {

        void onNoteClick(int adapterPosition, int buttonvalue, String email, String phone);
    }
}
