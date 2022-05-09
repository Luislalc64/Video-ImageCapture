package com.example.newvideorecord;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static int CAMERA_PERMISSION_CODE = 100;
    private static int VIDEO_RECORD_CODE = 101;
    private static final int REQUEST_IMAGE_CAPTURE  = 102;
    private Uri videoPath;
    ImageView imageView;
    Button saveimage;
    Button descartar;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.imageview);
        saveimage = (Button)findViewById(R.id.savegallery);
        descartar = (Button)findViewById(R.id.button3);

        if (isCameraPresent()){
            Log.i("Video_Record_Tag", " detected");
            getCameraPermission();
        }else{
            Log.i("Video_Record_Tag", "no detected");
        }
    }


    public void savetogallery(View view) throws IOException {saveToGallery();}
    public void recordVideoButtonPressed(View view){
        recordVideo();
    }
    public void captureImageButtonPressed(View view){
        captureImage();
    }
    public void restart(View view){
        Toast.makeText(this, "IMAGEN DESCARTADA", Toast.LENGTH_LONG).show();
        saveimage.setVisibility(View.GONE);
        descartar.setVisibility(View.GONE);
        imageView.setImageBitmap(null);
    }


    private boolean isCameraPresent(){
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            return  true;
        }else{
            return false;
        }
    }

    private void saveToGallery() throws IOException {
        BitmapDrawable draw = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = draw.getBitmap();
        FileOutputStream outStream = null;
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/Pictures");
        dir.mkdirs();
        String fileName = String.format("%d.jpg", System.currentTimeMillis());
        File outFile = new File(dir, fileName);
        outStream = new FileOutputStream(outFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        outStream.flush();
        outStream.close();
        Toast.makeText(this, "IMAGEN GUARDADA", Toast.LENGTH_LONG).show();
        saveimage.setVisibility(View.GONE);
        descartar.setVisibility(View.GONE);
        imageView.setImageBitmap(null);

    }

    private void getCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_CODE );
        }
    }
    private void recordVideo(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, VIDEO_RECORD_CODE);
    }
    private void captureImage(){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE );
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==VIDEO_RECORD_CODE){
            Toast.makeText(this, "Video Guardado en Galeria", Toast.LENGTH_LONG).show();


        }else if(requestCode==REQUEST_IMAGE_CAPTURE){
            videoPath = data.getData();
            Bitmap bm = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bm);
            saveimage.setVisibility(View.VISIBLE);
            descartar.setVisibility(View.VISIBLE);

        }

    }
}