package com.example.meditation.ui.relax;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
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
    private SeekBar seekBar;
    private ImageButton play;
    private TextView currentTime, endTime;
    private AudioService player;
    private int songProgress;
    private String audioUrl = "https://firebasestorage.googleapis.com/v0/b/meditation-df2e7.appspot.com/o/relax_quiet_time_david_fesliyan.mp3?alt=media";
    boolean serviceBound = false;
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.example.meditation.PlayNewAudio";
    public static final String BROADCAST_PROGRESS = "com.example.meditation.Progress";
    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            AudioService.LocalBinder binder = (AudioService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;


            mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
            checkIsPlaying();
            Toast.makeText(getContext(), "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    private BroadcastReceiver progress = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                //An audio file is passed to the service through putExtra();
                songProgress = Integer.parseInt(intent.getExtras().getString("progress"));
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("RelaxFragment", e.getMessage());
            }
        }
    };
    private void checkIsPlaying() {
        player.getIsPlaying().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    play.setImageResource(R.drawable.pause);
                }
                else {
                    if(player.isComplete())
                        seekBar.setProgress(0);
                    play.setImageResource(R.drawable.play);
                }
            }
        });
    }
    private void registerProgress() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(RelaxFragment.BROADCAST_PROGRESS);
        getContext().registerReceiver(progress, filter);
    }
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
            getActivity().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        } else {
            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            broadcastIntent.putExtra("media", audioUrl);
            getContext().sendBroadcast(broadcastIntent);
        }
    }

    private Handler mSeekbarUpdateHandler = new Handler();
    private Runnable mUpdateSeekbar = new Runnable() {
        @Override
        public void run() {
            int seconds = (int) (player.getCurrentPosition() / 1000) % 60;
            int minutes = (int) (player.getCurrentPosition() / (1000*60)) % 60;
            currentTime.setText(String.format("%02d:%02d", minutes, seconds));
            seekBar.setMax(player.getDuration());
            seconds = (int) (player.getDuration() / 1000) % 60;
            minutes = (int) (player.getDuration() / (1000*60)) % 60;
            endTime.setText(String.format("%02d:%02d", minutes, seconds));
            seekBar.setProgress(player.getCurrentPosition());
            mSeekbarUpdateHandler.postDelayed(this, 50);
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        registerProgress();

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

        play = root.findViewById(R.id.play_button);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player == null) {
                    playAudio(audioUrl);
                    play.setImageResource(R.drawable.pause);
                }
                else {
                    if(player.isPlaying()) {
                        player.pause();
                        play.setImageResource(R.drawable.play);
                    }
                    else {
                        player.play();
                        play.setImageResource(R.drawable.pause);
                    }
                }
            }
        });
        seekBar = root.findViewById(R.id.relaxSeekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && serviceBound)
                    player.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        currentTime = root.findViewById(R.id.currentTime);
        endTime = root.findViewById(R.id.endTime);

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(serviceBound)
            mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
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
            getActivity().unbindService(serviceConnection);
            //service is active
            player.stopSelf();
            getContext().unregisterReceiver(progress);
        }
    }
}