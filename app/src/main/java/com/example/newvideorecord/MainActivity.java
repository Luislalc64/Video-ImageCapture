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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static int CAMERA_PERMISSION_CODE = 100;
    private static int VIDEO_RECORD_CODE = 101;
    private static final int REQUEST_IMAGE_CAPTURE  = 102;
    private Uri videoPath;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isCameraPresent()){
            Log.i("Video_Record_Tag", " detected");
            getCameraPermission();
        }else{
            Log.i("Video_Record_Tag", "no detected");
        }
    }
    public void openFolder()
    {
        // location = "/sdcard/my_folder";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri mydir = Uri.parse("/Android/data/com.example.newvideorecord/files/Pictures/");
        intent.setDataAndType(mydir,"*/*");    // or use */*
        startActivity(intent);
    }
    public void recordVideoButtonPressed(View view){
        recordVideo();
    }
    public void captureImageButtonPressed(View view){
        captureImage();
    }
    public void openImageDirectory(View view){
        openFolder();
    }
    private boolean isCameraPresent(){
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            return  true;
        }else{
            return false;
        }
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
        File imageFile = null;
        try {
            imageFile = getImage();

        } catch (IOException e){
            e.printStackTrace();
        }
        if (imageFile!=null){
            Uri imageUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", imageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE );
        }
    }
    private File getImage() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageName = "jpg_"+timeStamp+"_";
        File storagedir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageName, ".jpg", storagedir);
        return imageFile;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==VIDEO_RECORD_CODE){
            if (resultCode == RESULT_OK){
                videoPath = data.getData();
            Log.i("VIDEO_RECORD_TAG", "Video is recorded" + videoPath);
            }else if (resultCode == RESULT_CANCELED){
                Log.i("VIDEO_RECORD_TAG", "Video is CANCELLED");

            }else{
                Log.i("VIDEO_RECORD_TAG", "Video got some error");

            }
        }

    }
}