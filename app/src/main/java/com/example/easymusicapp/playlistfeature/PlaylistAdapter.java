package com.example.easymusicapp.playlistfeature;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.easymusicapp.R;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    ArrayList<PlaylistModel> playlistList;

    public PlaylistAdapter(ArrayList<PlaylistModel> playlistList, Context context) {
        this.playlistList = playlistList;
        this.context = context;
    }

    Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new PlaylistAdapter.ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlaylistModel playlistData = playlistList.get(position);
        holder.titleTextView.setText(playlistData.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to another activity
                MyPlaylistPlayer.setCurrentIndex(position);
                Intent intent = new Intent(context, ShowSongInPlaylistActivity.class);
                intent.putExtra("PLAYLIST", playlistList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlistList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView;
        ImageView iconImageVIew;
        ImageView playlistBtnView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.music_title_text);
            iconImageVIew = itemView.findViewById(R.id.icon_view);
            iconImageVIew.setImageResource(R.drawable.baseline_featured_play_list_24);

            playlistBtnView = itemView.findViewById(R.id.playlist_add);
            playlistBtnView.setVisibility(View.GONE);
        }
    }
}
