package com.example.mediaplayerapp;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer mediaPlayer;
    private ImageView artistImage;
    private TextView leftTime;
    private TextView rightTime;
    private SeekBar seekBar;
    private Button prevButton;
    private Button playButton;
    private Button nextButton;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpUI();

        // Reveals the duration of the song and play the exact spot of song //
        seekBar.setMax(mediaPlayer.getDuration());
        // Setup the Seek Bar that will specify the progress of the song //
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }

                // The precise exact position of the track  //
                SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
                int currentPos = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                leftTime.setText(dateFormat.format(new Date(currentPos)));

                rightTime.setText(dateFormat.format (new Date(currentPos)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    // Setting up the user interface //
    public void setUpUI() {

        // Initiate the Media Player and retrieves the mp3 files //
        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.hunnit);

        artistImage = (ImageView) findViewById(R.id.imageView);
        leftTime = (TextView) findViewById(R.id.leftTIme);
        rightTime = (TextView) findViewById(R.id.rightTime);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        prevButton = (Button) findViewById(R.id.prevButton);
        playButton = (Button) findViewById(R.id.playButton);
        nextButton = (Button) findViewById(R.id.nextButton);

        // Setup onclick events and registering the buttons//
        prevButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

    }

    @Override
    // Setting up the views for OnClickListener //
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prevButton:

                break;

            case R.id.playButton:
                if (mediaPlayer.isPlaying()) {
                    pauseMusic();
                } else {
                    startMusic();
                }

                break;

            case R.id.nextButton:

                break;
        }
    }
        // Check to see if the Media Player is not null //
        public void pauseMusic() {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
                playButton.setBackgroundResource(android.R.drawable.ic_media_play);
            }

        }
        // Check to see if the Media Player is not null //
        public void startMusic() {
            if (mediaPlayer != null) {
                mediaPlayer.start();
                updateThread();
                playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
            }

        }

        // New thread to update the progress of the Seek Bar //
        public void updateThread() {

            thread = new Thread() {
                @Override
                public void run() {
                    try {

                        while (mediaPlayer !=null && mediaPlayer.isPlaying()) {


                                Thread.sleep(50);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Update the text left and right //
                                        int newPosition = mediaPlayer.getCurrentPosition();
                                        // The duration of the track//
                                        int newMax = mediaPlayer.getDuration();
                                        // Set the max for the Seek Bar //
                                        seekBar.setMax(newMax);
                                        // Displays the progress of the Seek Bar //
                                        seekBar.setProgress(newPosition);

                                        // Update the text //
                                        leftTime.setText(String.valueOf(new SimpleDateFormat("mm:ss")
                                        .format(new Date(mediaPlayer.getCurrentPosition()))));

                                        rightTime.setText(String.valueOf(new SimpleDateFormat("mm:ss")
                                        .format(new Date(mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition()))));



                                    }
                                });

                        }

                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        };
        // Start the thread //
        thread.start();
    }
}
