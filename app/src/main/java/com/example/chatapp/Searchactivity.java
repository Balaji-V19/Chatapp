package com.example.chatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;


public class Searchactivity extends AppCompatActivity {
    private EditText Search;
    private Button searchbtn;
    private RecyclerView recycle;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager mlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchactivity);
        Search= findViewById(R.id.edittext);
        searchbtn= findViewById(R.id.searchbtn);
        recycle= findViewById(R.id.recycleview);
        recycle.setNestedScrollingEnabled(false);
        recycle.setHasFixedSize(false);
        mlayout=new LinearLayoutManager(getApplication());
        recycle.setLayoutManager(mlayout);
        adapter=new RecycleviewAdapter(getcontext(),getApplication());
        recycle.setAdapter(adapter);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleardata();
                DatabaseReference dataref= FirebaseDatabase.getInstance().getReference().child("Users");
                Query query=dataref.orderByChild("Mail").startAt(Search.getText().toString())
                        .endAt(Search.getText().toString()+"\uf8ff");
                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String email="";
                        String uid= dataSnapshot.getRef().getKey();
                        if(dataSnapshot.child("Mail").getValue()!=null)
                        {
                            email=dataSnapshot.child("Mail").getValue().toString();
                        }
                        if (!email.contains(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                        {
                            FollowingUsers user=new FollowingUsers(email,uid);
                            data.add(user);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


    }

    private void cleardata() {
        int i=this.data.size();
        this.data.clear();
        adapter.notifyItemRangeChanged(0,i);
    }

    private ArrayList<FollowingUsers> data=new ArrayList<>();
    private ArrayList<FollowingUsers> getcontext() {

        return data;
    }


}
