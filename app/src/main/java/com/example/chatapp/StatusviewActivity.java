package com.example.chatapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StatusviewActivity extends AppCompatActivity {

    private String userid,ChatorStory;
    private ImageView img;
    private boolean chane=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusview);


        Bundle b=getIntent().getExtras();
        userid=b.getString("UserId");
        ChatorStory=b.getString("ChatorStory");
        img= findViewById(R.id.imageviewforstatus);

        switch (ChatorStory)
        {
            case "Chat":
                createnewforchat();
                break;
            case "Story":
                createnewone();
                break;
        }
        createnewone();


    }

    private void createnewforchat() {

        final DatabaseReference chat=FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid()).child("receved").child(userid);

        chat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imgsrc="";
                for (DataSnapshot datasnap:dataSnapshot.getChildren())
                {
                    if (datasnap.child("imageUrl:").getValue()!=null)
                    {
                        imgsrc=datasnap.child("imageUrl:").getValue().toString();
                    }
                    imgurl.add(imgsrc);
                    if (!chane)
                    {
                        chane=true;
                        displayuserstatus();
                    }
                    chat.child(datasnap.getKey()).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    ArrayList<String> imgurl=new ArrayList<>();
    private void createnewone() {
            final DatabaseReference dataref= FirebaseDatabase.getInstance().getReference().
                    child("Users").child(userid);
            dataref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String imgsrc="";
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
                        if (datasnap.child("imageUrl:").getValue()!=null)
                        {
                            imgsrc=datasnap.child("imageUrl:").getValue().toString();
                        }
                        long currenttime=System.currentTimeMillis();
                        if (currenttime >= statusstart && currenttime <=statusend)
                        {
                            imgurl.add(imgsrc);
                            if (!chane)
                            {
                                chane=true;
                                displayuserstatus();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    private int i=0;
    private void displayuserstatus() {
        Glide.with(getApplication()).load(imgurl.get(i)).into(img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picturevalue();
            }

        });

        final Handler handler=new Handler();
        final int wait=10000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                picturevalue();
                handler.postDelayed(this,wait);
            }
        },wait);
    }

    private void picturevalue() {
            if (i == imgurl.size() - 1)
            {
                finish();
                return;
            }
            i++;
            Glide.with(getApplication()).load(imgurl.get(i)).into(img);
        }

    }


