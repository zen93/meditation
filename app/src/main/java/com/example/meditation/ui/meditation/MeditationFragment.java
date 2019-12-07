package com.example.meditation.ui.meditation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class MeditationFragment extends Fragment {
    private static final String DEVELOPER_KEY = "AIzaSyBpdmE61jKbaySYf8PLIGudyXMSAnz4BP8";
    private MeditationViewModel meditationViewModel;
    private VideoView myVideo;
    private MediaController m;

    private YouTubePlayerView youTubePlayerView;
    private String videoId;

    private void initYouTubePlayerView() {
        // The player will automatically release itself when the fragment is destroyed.
        // The player will automatically pause when the fragment is stopped
        // If you don't add YouTubePlayerView as a lifecycle observer, you will have to release it manually.
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {

                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer, getLifecycle(),
                        videoId,0f
                );
            }
        });
    }



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
        youTubePlayerView = root.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String languageCode = sharedPref.getString("languageCode", "en");
        switch (languageCode) {
            case "en":
                videoId = "t6uvlMPglqE";
                break;
            case "hi":
                videoId = "8iaKLg114DQ";
                break;
            case "zh":
                videoId = "CMoEZPHXqcQ";
                break;
            case "te":
                videoId = "cACLiwdKwAM";
                break;
        }
        initYouTubePlayerView();

        return root;

    }


}