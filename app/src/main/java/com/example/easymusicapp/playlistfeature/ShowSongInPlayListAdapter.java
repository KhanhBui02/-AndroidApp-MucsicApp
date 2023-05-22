package com.example.easymusicapp.playlistfeature;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easymusicapp.AudioModel;
import com.example.easymusicapp.MusicPlayerActivity;
import com.example.easymusicapp.MyMediaPlayer;
import com.example.easymusicapp.R;
import com.example.easymusicapp.database.DBSingleton;
import com.example.easymusicapp.playlistfeature.PlaylistModel;

import java.util.ArrayList;
import java.util.List;

public class ShowSongInPlayListAdapter extends RecyclerView.Adapter<ShowSongInPlayListAdapter.ViewHolder> {

    ArrayList<AudioModel> songList;
    Activity activity;

    public ShowSongInPlayListAdapter(ArrayList<AudioModel> songList, Context context, Activity activity) {
        this.songList = songList;
        this.context = context;
        this.activity = activity;
    }

    Context context;

    @Override
    public ShowSongInPlayListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new ShowSongInPlayListAdapter.ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(ShowSongInPlayListAdapter.ViewHolder holder, int position) {
        AudioModel songData = songList.get(position);
        holder.titleTextView.setText(songData.getTitle());

        if(MyMediaPlayer.getCurrentIndex() == position){
            holder.titleTextView.setTextColor(Color.parseColor("#FF0000"));
        }else{
            holder.titleTextView.setTextColor(Color.parseColor("#000000"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to another activity
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.setCurrentIndex(position);
                Intent intent = new Intent(context, MusicPlayerActivity.class);
                intent.putExtra("LIST", songList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView;
        ImageView iconImageVIew;
        ImageView playlistBtnView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.music_title_text);
            iconImageVIew = itemView.findViewById(R.id.icon_view);
            playlistBtnView = itemView.findViewById(R.id.playlist_add);
            playlistBtnView.setVisibility(View.GONE);
        }
    }
}
