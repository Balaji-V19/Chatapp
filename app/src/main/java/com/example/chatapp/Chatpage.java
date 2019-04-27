package com.example.chatapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Chatpage extends Fragment {
    private View v;

    private RecyclerView recycle;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager mlayout;
    private Button btn;

    public static Chatpage newinstance() {
        Chatpage chat=new Chatpage();
        return chat;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_chatpage, container, false);

        recycle= v.findViewById(R.id.recycleviewforchat);
        btn=v.findViewById(R.id.refreshforchat);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleardata();
                createnewone();
            }
        });
        recycle.setNestedScrollingEnabled(false);
        recycle.setHasFixedSize(false);
        mlayout=new LinearLayoutManager(getContext());
        recycle.setLayoutManager(mlayout);
        adapter=new StatusAdapter(getcontext(),getContext());
        recycle.setAdapter(adapter);
        return v;
    }


    private void cleardata() {
        int i=this.data.size();
        this.data.clear();
        adapter.notifyItemRangeChanged(0,i);
    }

    private ArrayList<StatusDatas> data=new ArrayList<>();
    private ArrayList<StatusDatas> getcontext() {
        createnewone();
        return data;
    }


    public void createnewone()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid()).child("receved");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot snap: dataSnapshot.getChildren())
                    {
                        getuserinfo(snap.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getuserinfo(String key) {
        DatabaseReference dataref=FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String email=dataSnapshot.child("Mail").getValue().toString();
                    String Uid=dataSnapshot.getRef().getKey();
                    StatusDatas dat=new StatusDatas(email,Uid,"chat");
                    if (!data.contains(dat))
                    {
                        data.add(dat);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
