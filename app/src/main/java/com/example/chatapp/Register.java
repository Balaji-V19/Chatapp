package com.example.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Register extends AppCompatActivity {
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private EditText email,password,name,phone;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mauth=FirebaseAuth.getInstance();
        email=(EditText)findViewById(R.id.regemail);
        password=(EditText)findViewById(R.id.regpassword);
        name=(EditText)findViewById(R.id.regname);
        phone=(EditText)findViewById(R.id.regphone);
        login=(Button)findViewById(R.id.reglogin);
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=mauth.getCurrentUser();
                if (user!=null)
                {
                    Intent i=new Intent(Register.this,Fragmentmain.class);
                    startActivity(i);
                    finish();
                    return;
                }
            }
        };
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SpotsDialog dialog=new SpotsDialog(Register.this);
                dialog.show();
                final String memail,mpass,mname,mphone;
                memail=email.getText().toString();
                mpass=password.getText().toString();
                mname=name.getText().toString();
                mphone=phone.getText().toString();
                mauth.createUserWithEmailAndPassword(memail,mpass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        dialog.dismiss();
                        String userid=mauth.getCurrentUser().getUid();
                        DatabaseReference data= FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(userid);
                        Map map=new HashMap();
                        map.put("Mail",memail);
                        map.put("Name",mname);
                        map.put("phone",mphone);
                        data.updateChildren(map);
                        Intent i=new Intent(Register.this,Fragmentmain.class);
                        startActivity(i);
                        finish();
                        return;

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(Register.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mauth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mauth.removeAuthStateListener(authStateListener);
    }
}
