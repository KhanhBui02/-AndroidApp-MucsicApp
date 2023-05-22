package com.example.easymusicapp.playlistfeature;

public class MyPlaylistPlayer {
    static MyPlaylistPlayer instance;

    public static MyPlaylistPlayer getInstance(){
        if(instance == null){
            instance = new MyPlaylistPlayer();
        }

        return instance;
    }

    private static int currentIndex = -1;

    public static int getCurrentIndex() {
        return currentIndex;
    }

    public static void setCurrentIndex(int currentIndex) {
        MyPlaylistPlayer.currentIndex = currentIndex;
    }
}
