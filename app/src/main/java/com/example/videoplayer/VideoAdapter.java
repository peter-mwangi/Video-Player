package com.example.videoplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>
{
    private ArrayList<VideoModel> videoModelArrayList;
    private Context context;
    private VideoClickInterface videoClickInterface;

    public VideoAdapter(ArrayList<VideoModel> videoModelArrayList, Context context, VideoClickInterface videoClickInterface)
    {
        this.videoModelArrayList = videoModelArrayList;
        this.context = context;
        this.videoClickInterface = videoClickInterface;
    }

    @NonNull
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_layout, parent, false);
        return new VideoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        VideoModel model = videoModelArrayList.get(position);
        holder.videoThumbnail.setImageBitmap(model.getThumbNail());
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                videoClickInterface.onVideoClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView videoThumbnail;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            videoThumbnail = itemView.findViewById(R.id.video_thumb_nail);
        }
    }

    public interface VideoClickInterface
    {
        void onVideoClick(int position);
    }
}
