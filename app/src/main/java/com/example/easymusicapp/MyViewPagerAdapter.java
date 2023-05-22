package com.example.easymusicapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.easymusicapp.fragments.AllSongsFragment;
import com.example.easymusicapp.fragments.PlaylistFragment;
import com.example.easymusicapp.fragments.SettingsFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new AllSongsFragment();
            case 1:
                return new PlaylistFragment();
            case 2:
                return new SettingsFragment();
            default: return new PlaylistFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
