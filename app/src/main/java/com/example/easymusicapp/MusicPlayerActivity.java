package com.example.easymusicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {

    TextView titleTv, currentTimeTv, totalTimeTv;
    SeekBar seekBar;
    ImageView pausePlayBtn, skipNextBtn, skipPreviousBtn, musicIcon, repeatBtn;
    ArrayList<AudioModel> songList;
    AudioModel currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        titleTv = findViewById(R.id.song_title);
        currentTimeTv = findViewById(R.id.current_time);
        totalTimeTv = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seek_bar);
        pausePlayBtn = findViewById(R.id.pause_play);
        skipNextBtn = findViewById(R.id.skip_next);
        skipPreviousBtn = findViewById(R.id.skip_previous);
        musicIcon = findViewById(R.id.music_icon_big);
        repeatBtn = findViewById(R.id.repeat);

        titleTv.setSelected(true);

        songList = (ArrayList<AudioModel>) getIntent().getSerializableExtra("LIST");

        setResourcesWithMusic();

        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTimeTv.setText(convertToMMSS(mediaPlayer.getCurrentPosition() + ""));

                    if(mediaPlayer.isPlaying()){
                        pausePlayBtn.setImageResource(R.drawable.baseline_pause_circle_outline_24);
                    }else{
                        pausePlayBtn.setImageResource(R.drawable.baseline_play_circle_outline_24);
                    }

                    if(mediaPlayer.isLooping()){
                        repeatBtn.setImageResource(R.drawable.baseline_repeat_on_24);
                    }else{
                        repeatBtn.setImageResource(R.drawable.baseline_repeat_24);
                    }
                }

                new Handler().postDelayed(this, 100);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    void setResourcesWithMusic(){
        currentSong = songList.get(MyMediaPlayer.getCurrentIndex());

        titleTv.setText(currentSong.getTitle());

        totalTimeTv.setText(convertToMMSS(currentSong.getDuration()));

        pausePlayBtn.setOnClickListener(v->pausePlay());
        skipNextBtn.setOnClickListener(v->playNextSong());
        skipPreviousBtn.setOnClickListener(v->playPreviousSong());
        repeatBtn.setOnClickListener(v->musicRepeat());

        //if(MyMediaPlayer.getCurrentIndex() != MyMediaPlayer.previousIndex){
            playMusic();
        //}
    }

    private void playMusic(){
        mediaPlayer.reset();

        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void playNextSong(){
        //MyMediaPlayer.currentIndex += 1;
        MyMediaPlayer.setCurrentIndex(MyMediaPlayer.getCurrentIndex() + 1);

        if(MyMediaPlayer.getCurrentIndex() > songList.size() - 1)
            MyMediaPlayer.setCurrentIndex(0);

        mediaPlayer.reset();
        setResourcesWithMusic();
    }

    private void playPreviousSong(){
        if(MyMediaPlayer.getCurrentIndex() <= 0)
            return;

        //MyMediaPlayer.currentIndex -= 1;
        MyMediaPlayer.setCurrentIndex(MyMediaPlayer.getCurrentIndex() - 1);

        mediaPlayer.reset();
        setResourcesWithMusic();
    }

    private void pausePlay(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }else {
            mediaPlayer.start();
        }
    }

    private void musicRepeat(){
        boolean isLooping = false;
        if(mediaPlayer.isLooping()){
            isLooping = false;
            mediaPlayer.setLooping(false);
        }else{
            isLooping = true;
            mediaPlayer.setLooping(true);
        }

        MyMediaPlayer.isLooping = isLooping;
    }

    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}