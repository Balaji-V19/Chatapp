package com.example.chatapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewParent;

import com.google.firebase.auth.FirebaseAuth;

public class Fragmentmain extends AppCompatActivity {
    private ViewPager mypage;
    private FragmentPagerAdapter mfragment;
    private FirebaseAuth mauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmentmain);
        Followers follow=new Followers();
        follow.function();
        mauth=FirebaseAuth.getInstance();
        mypage=(ViewPager)findViewById(R.id.mypage);
        mfragment=new balaji(getSupportFragmentManager());
        mypage.setAdapter(mfragment);
        mypage.setCurrentItem(1);
    }
    public static class balaji extends FragmentPagerAdapter{

        public balaji(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i==0)
            {
                return Camerafragment.newinstance();
            }
            if (i==1)
            {
                return Chatpage.newinstance();
            }
            if (i==2)
            {
                return Statusfragment.newinstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.search){
            Intent i=new Intent(Fragmentmain.this,Searchactivity.class);
            startActivity(i);
        }
        if (id==R.id.LogOut)
        {
            mauth.signOut();
            Intent i=new Intent(Fragmentmain.this,MainActivity.class);
            startActivity(i);
            finish();
        }
        if (id==R.id.Settings)
        {

        }
        return super.onOptionsItemSelected(item);
    }
}
