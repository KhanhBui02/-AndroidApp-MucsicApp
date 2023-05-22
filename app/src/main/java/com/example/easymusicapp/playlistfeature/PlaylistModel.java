package com.example.easymusicapp.playlistfeature;

import java.io.Serializable;

public class PlaylistModel implements Serializable {
    String Name = "";

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public PlaylistModel(String name) {
        Name = name;
    }
}
