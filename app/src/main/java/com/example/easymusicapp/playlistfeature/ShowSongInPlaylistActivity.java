package com.example.easymusicapp.playlistfeature;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import com.example.easymusicapp.AudioModel;
import com.example.easymusicapp.MusicListAdapter;
import com.example.easymusicapp.R;
import com.example.easymusicapp.database.DBSingleton;

import java.io.File;
import java.util.ArrayList;

public class ShowSongInPlaylistActivity extends AppCompatActivity {

    TextView playlistTitle;
    RecyclerView recyclerView;
    TextView noMusicTextView;
    PlaylistModel currentPlaylist;
    ArrayList<PlaylistModel> playlistList;
    ArrayList<AudioModel> songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_song_in_playlist);

        playlistTitle = findViewById(R.id.playlist_title_text);
        recyclerView = findViewById(R.id.recycler_view);
        noMusicTextView = findViewById(R.id.no_songs_text);

        playlistList = (ArrayList<PlaylistModel>) getIntent().getSerializableExtra("PLAYLIST");

        setResourceWithPlaylist();

        if(songList.size() == 0){
            noMusicTextView.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new ShowSongInPlayListAdapter(songList, getApplicationContext(), this));
        }
    }

    private void setResourceWithPlaylist() {
        currentPlaylist = playlistList.get(MyPlaylistPlayer.getCurrentIndex());

        playlistTitle.setText(currentPlaylist.getName());

        getSongsByPlaylistTitle(currentPlaylist.getName());
    }

    private void getSongsByPlaylistTitle(String playlistTitle) {
        songList = new ArrayList<>();

        SQLiteDatabase db = DBSingleton.getInstance().dbHelper.getReadableDatabase();
        String[] projection = {"title", "path", "duaration","playlistName"};
        String selection = "playlistName LIKE ?";
        String[] selectionArgs = { "%" + playlistTitle + "%" };
        Cursor cursor = db.query("SongInPlaylist", projection, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            //int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
            @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex("path"));
            @SuppressLint("Range") String duaration = cursor.getString(cursor.getColumnIndex("duaration"));
            @SuppressLint("Range") String playlistName = cursor.getString(cursor.getColumnIndex("playlistName"));

            AudioModel audioModel = new AudioModel(path, title, duaration);

            songList.add(audioModel);
//            // Do something with the data (e.g., display it)
//            System.out.println("ID: " + id + ", Title: " + title + ", Playlist Name: " + playlistName);
        }

        cursor.close();
        db.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(recyclerView!=null){
            recyclerView.setAdapter(new MusicListAdapter(songList, getApplicationContext(), this));
        }
    }
}