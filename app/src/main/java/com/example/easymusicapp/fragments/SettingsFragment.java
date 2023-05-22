package com.example.easymusicapp.fragments;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.easymusicapp.R;

public class SettingsFragment extends Fragment {

    private AudioManager audioManager;
    private SeekBar volumeSeekBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        // Tìm thanh SeekBar trong layout
        volumeSeekBar = view.findViewById(R.id.volume_seekbar);

        // Thiết lập giá trị ban đầu của SeekBar dựa trên âm lượng hiện tại
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(currentVolume);

        // Thiết lập bộ lắng nghe sự kiện cho SeekBar
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Thiết lập âm lượng mới cho âm thanh STREAM_MUSIC
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Không cần xử lý
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Không cần xử lý
            }
        });

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });

        return view;
    }
}