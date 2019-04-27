package com.example.chatapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Statusfragment extends Fragment {
    private View v;

    private RecyclerView recycle;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager mlayout;
    private Button btn;

    public static Statusfragment newinstance() {
        Statusfragment stat=new Statusfragment();
        return stat;
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_statusfragment, container, false);
        recycle= v.findViewById(R.id.recycleviewforstatus);
        btn=v.findViewById(R.id.refresh);
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

    private void createnewone() {
        for (int i = 1 ; i < Followers.listuser.size() ; i++)
        {
            final DatabaseReference dataref= FirebaseDatabase.getInstance().getReference().
                    child("Users").child(Followers.listuser.get(i));
            dataref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String email=dataSnapshot.child("Mail").getValue().toString();
                    String uid=dataSnapshot.getRef().getKey();
                    long statusstart=0;
                    long statusend=0;
                    for (DataSnapshot datasnap:dataSnapshot.child("story").getChildren())
                    {
                        if (datasnap.child("starttime").getValue()!=null)
                        {
                            statusstart=Long.parseLong(datasnap.child("starttime").getValue().toString());
                        }
                        if (datasnap.child("endtime").getValue()!=null)
                        {
                            statusend=Long.parseLong(datasnap.child("endtime").getValue().toString());
                        }
                        long currenttime=System.currentTimeMillis();
                        if (currenttime >= statusstart && currenttime <=statusend)
                        {
                            StatusDatas dt=new StatusDatas(email,uid,"story");
                            if (!data.contains(dt)){
                                data.add(dt);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


}
