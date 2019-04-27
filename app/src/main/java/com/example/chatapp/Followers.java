package com.example.chatapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Followers {

    public static ArrayList<String> listuser=new ArrayList<>();

    public void  function(){
        listuser.clear();
        DatabaseReference data= FirebaseDatabase.getInstance().getReference().child("Users").
                child(FirebaseAuth.getInstance().getUid()).child("Followers");
        data.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists())
                {
                    String Uid=dataSnapshot.getRef().getKey();
                    if (Uid!=null && !listuser.contains(Uid))
                    {
                        listuser.add(Uid);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String Uid=dataSnapshot.getRef().getKey();
                    if (Uid!=null )
                    {
                        listuser.remove(Uid);
                    }
                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
