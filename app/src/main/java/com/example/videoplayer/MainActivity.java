package com.example.videoplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements VideoAdapter.VideoClickInterface
{
    private RecyclerView mainRV;
    private ArrayList<VideoModel> videoModelArrayList;
    private VideoAdapter videoAdapter;

    private static final int STORAGE_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoModelArrayList = new ArrayList<>();
        videoAdapter = new VideoAdapter(videoModelArrayList, this, this::onVideoClick);
        mainRV = findViewById(R.id.main_recycler_view);
        mainRV.setLayoutManager(new GridLayoutManager(this, 2));
        mainRV.setAdapter(videoAdapter);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        }
        else
        {
            getVideos();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION)
        {
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                getVideos();
            }
            else
            {
                Toast.makeText(this, "The App needs Storage Permission to work", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void getVideos()
    {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst())
        {
            do {
                @SuppressLint("Range") String videoTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                @SuppressLint("Range") String videoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                Bitmap vThumbnail = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);

                videoModelArrayList.add(new VideoModel(videoTitle, videoPath, vThumbnail));

            }while (cursor.moveToNext());
        }
        videoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onVideoClick(int position)
    {
        Intent intent = new Intent(MainActivity.this, VideoPlayerActivity.class);
        intent.putExtra("videoName", videoModelArrayList.get(position).getVideoName());
        intent.putExtra("videoPath", videoModelArrayList.get(position).getVideoPath());
        startActivity(intent);

    }
}