package com.example.chatapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReceverActivity extends AppCompatActivity {


    private RecyclerView recycle;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager mlayout;
    private Bitmap bitmap;
    String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recever);
        recycle=findViewById(R.id.recycleerview);

        try {
            bitmap= BitmapFactory.decodeStream(getApplication().openFileInput("ImagetoSend"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            finish();
            return;
        }
        Uid=FirebaseAuth.getInstance().getUid();


        recycle.setNestedScrollingEnabled(false);
        recycle.setHasFixedSize(false);
        mlayout=new LinearLayoutManager(this);
        recycle.setLayoutManager(mlayout);
        adapter=new ReceverAdapter(getcontext(),this);
        recycle.setAdapter(adapter);


        FloatingActionButton fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendstory();
            }
        });
    }



    private ArrayList<ReceverDatas> result=new ArrayList<>();
    private ArrayList<ReceverDatas> getcontext() {
        listendata();
        return result;
    }

    private void listendata() {
        for (int i = 1 ; i < Followers.listuser.size() ; i++) {
            DatabaseReference dataref= FirebaseDatabase.getInstance().getReference().child("Users").child(Followers.listuser.get(i));
            dataref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String email="";
                    String uid= dataSnapshot.getRef().getKey();
                    if(dataSnapshot.child("Mail").getValue()!=null)
                    {
                        email=dataSnapshot.child("Mail").getValue().toString();
                    }
                    if (!email.contains(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                    {
                        ReceverDatas user=new ReceverDatas(email,uid,false);
                        if (!result.contains(user))
                        {
                            result.add(user);
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


    public void sendstory()
    {
        Uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference data= FirebaseDatabase.getInstance().getReference().child("Users").
                child(Uid).child("story");
        final String var=data.push().getKey();
        final StorageReference refstr= FirebaseStorage.getInstance().getReference().child("capture").child(var);
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
        byte[] bytes=out.toByteArray();
        UploadTask uptask=refstr.putBytes(bytes);
        uptask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri downloadUrl = urlTask.getResult();
                Long startstory=System.currentTimeMillis();
                Long endstory=startstory + (24*60*60*1000);

                CheckBox check=findViewById(R.id.checkboxrecever);
                if (check.isChecked())
                {
                    Map<String ,Object> map=new HashMap<>();
                    map.put("imageUrl",downloadUrl.toString());
                    map.put("starttime",startstory);
                    map.put("endtime",endstory);
                    data.child(var).setValue(map);
                }
                for (int i=0;i < result.size();i++)
                {
                    if (result.get(i).isReceiver())
                    {
                        DatabaseReference dataref=FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(result.get(i).getUid()).child("receved").
                                        child(Uid);
                        Map<String ,Object> map=new HashMap<>();
                        map.put("imageUrl",downloadUrl.toString());
                        map.put("starttime",startstory);
                        map.put("endtime",endstory);
                        dataref.child(var).setValue(map);
                    }
                }
                Intent i=new Intent(getApplicationContext(),Fragmentmain.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                return;

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReceverActivity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        });
    }
}
