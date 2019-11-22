package com.example.meditation.ui.relax;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.meditation.MainActivity;
import com.example.meditation.R;

public class RelaxFragment extends Fragment {

    private RelaxViewModel relaxViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        relaxViewModel =
                ViewModelProviders.of(this).get(RelaxViewModel.class);
        View root = inflater.inflate(R.layout.fragment_relax, container, false);
        final TextView songTitleTextView = root.findViewById(R.id.songTitle);
        relaxViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                songTitleTextView.setText(s);
            }
        });

        final ImageButton play = root.findViewById(R.id.play_button);
        final MediaPlayer mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.relax_quiet_time_david_fesliyan);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        play.setImageResource(R.drawable.play);
                    }
                });
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    play.setImageResource(R.drawable.play);
                }
                else {
                    mediaPlayer.start();
                    play.setImageResource(R.drawable.pause);
                }
            }
        });
        return root;
    }
}