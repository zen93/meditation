package com.example.meditation.ui.relax;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.meditation.AudioService;
import com.example.meditation.MainActivity;
import com.example.meditation.R;

public class RelaxFragment extends Fragment {

    private RelaxViewModel relaxViewModel;
    private AudioService player;
    private String audioUrl = "https://firebasestorage.googleapis.com/v0/b/meditation-df2e7.appspot.com/o/relax_quiet_time_david_fesliyan.mp3?alt=media";
    boolean serviceBound = false;
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.example.meditation.PlayNewAudio";
    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            AudioService.LocalBinder binder = (AudioService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;

            Toast.makeText(getContext(), "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    private void playAudio(String media) {
        //Check is service is active
        if (!serviceBound) {
            Intent playerIntent = new Intent(getContext(), AudioService.class);
            playerIntent.putExtra("media", media);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getContext().startForegroundService(playerIntent);
            } else {
                getContext().startService(playerIntent);
            }
            //getContext().startService(playerIntent);
            getContext().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            broadcastIntent.putExtra("media", audioUrl);
            getContext().sendBroadcast(broadcastIntent);
        }
    }

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
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio(audioUrl);
                play.setImageResource(R.drawable.pause);
            }
        });
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            if(savedInstanceState.getBoolean("ServiceState")) {
                serviceBound = savedInstanceState.getBoolean("ServiceState");
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            getContext().unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }
    }
}