package com.example.chatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecycleviewAdapter extends RecyclerView.Adapter<RecycleviewAdapter.viewholder> {
    private List<FollowingUsers> data;
    private Context cts;

    public RecycleviewAdapter(List<FollowingUsers> data, Context cts) {
        this.data = data;
        this.cts = cts;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(cts).inflate(R.layout.recycleview,viewGroup,false);

        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder viewholder, int i) {
        viewholder.tv.setText(data.get(i).getEmail());
        if (!Followers.listuser.contains(data.get(viewholder.getLayoutPosition()).getUid()))
        {
            viewholder.btn.setText("Follow");

        }
        else {
            viewholder.btn.setText("Following");
        }
        viewholder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (!Followers.listuser.contains(data.get(viewholder.getLayoutPosition()).getUid()))
                {
                    viewholder.btn.setText("Following");
                    FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(userid).child("Followers").child(data.get(viewholder.getLayoutPosition())
                            .getUid()).setValue(true);
                }
                else {
                    viewholder.btn.setText("Follow");
                    FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(userid).child("Followers").child(data.get(viewholder.getLayoutPosition())
                            .getUid()).removeValue();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView tv;
        Button btn;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.userlist);
            btn=itemView.findViewById(R.id.searchuserinfo);
        }
    }
}
