package com.example.chatapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Capturedimage extends AppCompatActivity {
    String Uid;
    private FirebaseAuth mauth;
    private Button storybtn;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capturedimage);
        storybtn= findViewById(R.id.storybtn);
        ImageView img=findViewById(R.id.imageview);

        try {
            bitmap= BitmapFactory.decodeStream(getApplication().openFileInput("ImagetoSend"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            finish();
            return;
        }
        Uid=FirebaseAuth.getInstance().getUid();

        img.setImageBitmap(bitmap);


        storybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Capturedimage.this,ReceverActivity.class);
                startActivity(i);
                return;
            }
        });

    }



}
