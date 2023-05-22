package com.example.easymusicapp;

import android.media.MediaPlayer;

public class MyMediaPlayer {
    static MediaPlayer instance;

    public static MediaPlayer getInstance(){
        if(instance == null){
            instance = new MediaPlayer();
        }

        instance.setLooping(isLooping);

        return instance;
    }

    private static int currentIndex = -1;
    public static int previousIndex = -2;

    public static boolean isLooping = false;

    public static int getCurrentIndex() {
        return currentIndex;
    }

    public static void setCurrentIndex(int currentIndex) {
        previousIndex = currentIndex;
        MyMediaPlayer.currentIndex = currentIndex;
    }
}
