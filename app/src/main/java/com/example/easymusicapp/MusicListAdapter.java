package com.example.easymusicapp;

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

import com.example.easymusicapp.database.DBSingleton;
import com.example.easymusicapp.playlistfeature.PlaylistModel;

import java.util.ArrayList;
import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {

    ArrayList<AudioModel> songList;
    Activity activity;

    public MusicListAdapter(ArrayList<AudioModel> songList, Context context, Activity activity) {
        this.songList = songList;
        this.context = context;
        this.activity = activity;
    }

    Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new MusicListAdapter.ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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

        holder.playlistBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowChoosePlaylistDialog(songData);
            }
        });
    }

    private void ShowChoosePlaylistDialog(AudioModel songData){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Chọn playlist");

        List<String> playlistNames = getPlaylistNames();

        boolean[] checkedItems = new boolean[playlistNames.size()];

        builder.setMultiChoiceItems(playlistNames.toArray(new String[0]), checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

            }
        });

        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveSelectedPlaylists(checkedItems, songData);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        AlertDialog dialog = builder.create();
        if (!activity.isFinishing()) {
            dialog.show();
        }
    }

    private List<String> getPlaylistNames() {
        List<String> playlistNames = new ArrayList<>();

        SQLiteDatabase db = DBSingleton.getInstance().dbHelper.getReadableDatabase();
        String[] projection = {"id", "name"};
        Cursor cursor = db.query("playlists", projection, null, null, null, null, null);

        while (cursor.moveToNext()) {
            //@SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));

            playlistNames.add(name);
        }

        cursor.close();
        db.close();

        return playlistNames;
    }

    private void saveSelectedPlaylists(boolean[] checkedItems, AudioModel songData) {
        // Tạo một danh sách để lưu trữ thông tin bài hát và playlist đã chọn
        List<String> selectedPlaylists = new ArrayList<>();

        // Lặp qua mảng checkedItems để tìm những playlist đã được chọn
        for (int i = 0; i < checkedItems.length; i++) {
            if (checkedItems[i]) {
                // Playlist thứ i đã được chọn
                String playlistName = getPlaylistNames().get(i);
                selectedPlaylists.add(playlistName);
            }
        }

        // Mở kết nối tới cơ sở dữ liệu SQLite
//        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase("songs.db", null);
        SQLiteDatabase database = activity.openOrCreateDatabase("songs.db", Context.MODE_PRIVATE, null);

        // Tạo bảng playlist (nếu chưa tồn tại)
        String createTableQuery = "CREATE TABLE IF NOT EXISTS SongInPlaylist (title TEXT, path TEXT, duaration TEXT,playlistName TEXT)";
        database.execSQL(createTableQuery);

//        // Xóa thông tin bài hát đã lưu trước đó trong cơ sở dữ liệu
//        String deleteQuery = "DELETE FROM SongWithPlaylist WHERE title = ?";
//        database.execSQL(deleteQuery, new Object[]{songData.getTitle()});

        // Lưu thông tin bài hát và playlist đã chọn vào cơ sở dữ liệu
        String insertQuery = "INSERT INTO SongInPlaylist (title,path,duaration,playlistName) VALUES (?,?,?,?)";
        for (String playlistName : selectedPlaylists) {
            database.execSQL(insertQuery, new Object[]{songData.getTitle(), songData.getPath(), songData.getDuration(),playlistName});
        }

        // Đóng kết nối tới cơ sở dữ liệu
        database.close();
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
            playlistBtnView.setVisibility(View.VISIBLE);
        }
    }
}
