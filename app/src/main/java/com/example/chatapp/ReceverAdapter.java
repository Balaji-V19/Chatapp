package com.example.chatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ReceverAdapter extends RecyclerView.Adapter<ReceverAdapter.viewholder> {
    List<ReceverDatas> data;
    Context cts;

    public ReceverAdapter(List<ReceverDatas> data, Context cts) {
        this.data = data;
        this.cts = cts;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(cts).inflate(R.layout.recever_adapter_layout,null);

        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder viewholder, int i) {
        viewholder.email.setText(data.get(i).getEmail());
        viewholder.recever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean state=!data.get(viewholder.getLayoutPosition()).isReceiver();
                data.get(viewholder.getLayoutPosition()).setReceiver(state);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView email;
        CheckBox recever;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            email=itemView.findViewById(R.id.layout);
            recever=itemView.findViewById(R.id.checkboxrecever);
        }
    }
}
