package com.example.easymusicapp.fragments;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.easymusicapp.playlistfeature.PlaylistAdapter;
import com.example.easymusicapp.playlistfeature.PlaylistModel;
import com.example.easymusicapp.R;

import java.util.ArrayList;

public class PlaylistFragment extends Fragment {

    Button addPlaylistButton;
    TextView noPlaylistTextView;
    RecyclerView recyclerView;
    ArrayList<PlaylistModel> playlistList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_playlist, container, false);

        // Ánh xạ các thành phần UI
        addPlaylistButton = rootView.findViewById(R.id.add_playlist_button);
        noPlaylistTextView = rootView.findViewById(R.id.no_playlists_text);
        recyclerView = rootView.findViewById(R.id.playlist_recycler_view);

        // Xử lý sự kiện nhấn nút "Add Playlist"
        addPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị dialog hoặc activity để người dùng nhập tên playlist và lưu vào database
                showAddPlaylistDialog();
            }
        });

        updatePlaylistsFromDatabase();

        updatePlaylistView();

        return rootView;
    }

    private void showAddPlaylistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Playlist");

        // Tạo một EditText để người dùng nhập tên playlist
        final EditText playlistNameEditText = new EditText(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(32, 16, 32, 16);
        playlistNameEditText.setLayoutParams(layoutParams);
        builder.setView(playlistNameEditText);

        // Thêm nút "Add" vào dialog
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String playlistName = playlistNameEditText.getText().toString();
                // Lưu tên playlist vào database
                savePlaylistToDatabase(playlistName);
            }
        });

        // Thêm nút "Cancel" để người dùng có thể hủy bỏ việc thêm playlist
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void savePlaylistToDatabase(String playlistName) {
        // Tạo hoặc mở CSDL
        SQLiteDatabase database = getActivity().openOrCreateDatabase("songs.db", Context.MODE_PRIVATE, null);

        // Tạo bảng nếu chưa tồn tại
        String createTableQuery = "CREATE TABLE IF NOT EXISTS playlists (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)";
        database.execSQL(createTableQuery);

        // Chèn tên playlist vào CSDL
        ContentValues values = new ContentValues();
        values.put("name", playlistName);
        database.insert("playlists", null, values);

        // Đóng CSDL
        database.close();

        updatePlaylistsFromDatabase();
        updatePlaylistView();
    }

    private boolean isTableExists(SQLiteDatabase db, String tableName) {
        boolean isExists = false;
        Cursor cursor = null;

        try {
            // Thực hiện truy vấn kiểm tra xem tableName đã tồn tại trong database hay chưa
            cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'", null);
            isExists = cursor.moveToFirst();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return isExists;
    }


    private void updatePlaylistsFromDatabase() {
        playlistList.clear();

        // Mở hoặc tạo CSDL
        SQLiteDatabase database = getActivity().openOrCreateDatabase("songs.db", Context.MODE_PRIVATE, null);

        if(!isTableExists(database, "playlists"))
            return;

        // Truy vấn danh sách các playlist từ bảng "playlists"
        String query = "SELECT name FROM playlists";
        Cursor cursor = database.rawQuery(query, null);

        // Lặp qua các dòng kết quả
        if (cursor.moveToFirst()) {
            do {
                // Lấy tên playlist từ cột "name"
                if(cursor.getColumnIndex("name") < 0)
                    break;
                String playlistName = cursor.getString(cursor.getColumnIndex("name"));

                PlaylistModel playlistModel = new PlaylistModel(playlistName);
                // Thêm tên playlist vào danh sách
                playlistList.add(playlistModel);
            } while (cursor.moveToNext());
        }

        // Đóng cursor và CSDL
        cursor.close();
        database.close();
    }

    private void updatePlaylistView(){
        if(playlistList.size() == 0){
            noPlaylistTextView.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new PlaylistAdapter(playlistList, getActivity().getApplicationContext()));
        }
    }
}