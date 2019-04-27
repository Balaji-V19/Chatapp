package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.viewholder> {
    private List<StatusDatas> data;
    private Context cts;

    public StatusAdapter(List<StatusDatas> data, Context cts) {
        this.data = data;
        this.cts = cts;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.statusrecycle,null);

        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder viewholder, int i) {
        viewholder.tv.setText(data.get(i).getEmail());
        viewholder.tv.setTag(data.get(i).getUid());
        viewholder.layoutlin.setTag(data.get(i).getChatorStory());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv;
        LinearLayout layoutlin;


        public viewholder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv=itemView.findViewById(R.id.userplaylist);
            layoutlin=itemView.findViewById(R.id.layoutlinear);
        }

        @Override
        public void onClick(View v) {
            Intent i=new Intent(v.getContext(),StatusviewActivity.class);
            Bundle bun=new Bundle();
            bun.putString("UserId",tv.getTag().toString());
            bun.putString("ChatorStory",layoutlin.getTag().toString());
            i.putExtras(bun);
            v.getContext().startActivity(i);
        }
    }
}
