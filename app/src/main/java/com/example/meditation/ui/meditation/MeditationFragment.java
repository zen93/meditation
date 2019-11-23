package com.example.meditation.ui.meditation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.MediaController;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.net.Uri;

import com.example.meditation.R;

public class MeditationFragment extends Fragment {

    private MeditationViewModel meditationViewModel;
    private VideoView myVideo;
    private MediaController m;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        meditationViewModel =
                ViewModelProviders.of(this).get(MeditationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_meditation, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        meditationViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        myVideo = (VideoView) root.findViewById(R.id.videoView);

        MediaController m = new MediaController(getActivity());
        m.setAnchorView(myVideo);
        myVideo.setMediaController(m);
        //myVideo.setMediaController(m);
        //myVideo.setKeepScreenOn(true);

        String path = "android.resource://com.example.meditation/" + R.raw.meditation_video;

        Uri u = Uri.parse(path);

        myVideo.setVideoURI(u);

        myVideo.start();

        return root;

    }


}