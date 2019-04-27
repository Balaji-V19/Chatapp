package com.example.chatapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class Camerafragment extends Fragment implements SurfaceHolder.Callback {
    private View v;
    private SurfaceView msurface;
    private SurfaceHolder msurfaceholder;
    private Camera camera;
    private Button capture;
    private Camera.PictureCallback capturedpic;
    final int Camera_access_code=1;

    public static Camerafragment newinstance() {
        Camerafragment cam=new Camerafragment();
        return cam;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_camerafragment, container, false);
        msurface=v.findViewById(R.id.surfaceview);
        msurfaceholder=msurface.getHolder();
        capture=(Button)v.findViewById(R.id.capture);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]
                    {Manifest.permission.CAMERA},Camera_access_code);
        }
        else {

            msurfaceholder.addCallback(this);
            msurfaceholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        }
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null,null,capturedpic);

            }
        });
        capturedpic=new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap bitmapdecode= BitmapFactory.decodeByteArray(data,0,data.length);
                Bitmap rotate=rotatebitmap(bitmapdecode);
                String location=saveimagetodatabase(rotate);
                if (location!=null)
                {
                    Intent i=new Intent(getActivity(),Capturedimage.class);
                    startActivity(i);
                    return;
                }

            }
        };

        return v;
    }

    public String saveimagetodatabase(Bitmap bitmap){
        String file="ImagetoSend";
                try
                {
                    ByteArrayOutputStream bytes=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
                    FileOutputStream fo=getContext().openFileOutput(file,Context.MODE_PRIVATE);
                    fo.write(bytes.toByteArray());
                    fo.close();
                }
                catch (Exception e)
                {
                    e.getStackTrace();
                    file=null;
                }
                return file;

    }



    private Bitmap rotatebitmap(Bitmap bitmapdecode) {
        int w=bitmapdecode.getWidth();
        int h=bitmapdecode.getHeight();
        Matrix matrix=new Matrix();
        matrix.setRotate(90);
        return Bitmap.createBitmap(bitmapdecode,0,0,w,h,matrix,true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera=Camera.open();
        Camera.Parameters parameters;
        parameters=camera.getParameters();
        camera.setDisplayOrientation(90);
        parameters.setPreviewFrameRate(30);
        camera.setParameters(parameters);
        Camera.Size bestsize=null;
        List<Camera.Size> sizeList=camera.getParameters().getSupportedPreviewSizes();
        bestsize=sizeList.get(0);
        for (int i=1;i<sizeList.size();i++)
        {
            if ((sizeList.get(i).width*sizeList.get(i).height)>(bestsize.width*bestsize.height))
            {
                bestsize=sizeList.get(i);
            }
        }
        parameters.setPreviewSize(bestsize.width,bestsize.height);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case Camera_access_code:{
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    msurfaceholder.addCallback(this);
                    msurfaceholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                }
                else {
                    Toast.makeText(getActivity(), "Please provide permission", Toast.LENGTH_SHORT).show();
                }


            }

        }
    }
}
