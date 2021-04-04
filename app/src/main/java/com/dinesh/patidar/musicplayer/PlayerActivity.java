package com.dinesh.patidar.musicplayer;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class PlayerActivity extends AppCompatActivity {

    static MediaPlayer myMediaPlayer;
    int position;
    ArrayList<File> mySongs;
    Thread updateSeekBar;
    String songTitle;
    private Button btnNext, btnPrevious, btnPause;
    private SeekBar seekBar;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btnNext = findViewById(R.id.btn_next);
        btnPrevious = findViewById(R.id.btn_previous);
        btnPause = findViewById(R.id.btn_pause);
        seekBar = findViewById(R.id.seek_bar);
        tvTitle = findViewById(R.id.tv_title);


        updateSeekBar = new Thread() {
            @Override
            public void run() {

                int totalDuration = myMediaPlayer.getDuration();
                int currentPosition = 0;

                while (currentPosition < totalDuration) {
                    try {
                        sleep(500);
                        currentPosition = myMediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        if (myMediaPlayer != null) {
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");


        songTitle = mySongs.get(position).getName().toString();

        String songName = intent.getStringExtra("title");
        tvTitle.setText(songName);
        tvTitle.setSelected(true);

        position = bundle.getInt("pos",0);

        Uri uri = Uri.parse(mySongs.get(position).toString());
        myMediaPlayer = MediaPlayer.create(getApplicationContext(),uri);

        myMediaPlayer.start();
        seekBar.setMax(myMediaPlayer.getDuration());

        updateSeekBar.start();

        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.purple_500), PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.purple_500),PorterDuff.Mode.SRC_IN);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                myMediaPlayer.seekTo(seekBar.getProgress());
            }
        });


        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBar.setMax(myMediaPlayer.getDuration());
                if (myMediaPlayer.isPlaying()){
                    btnPause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                    myMediaPlayer.pause();
                }else {
                    btnPause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                    myMediaPlayer.start();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myMediaPlayer.stop();
                myMediaPlayer.release();
                 position = ((position +1) %mySongs.size());

                 Uri u = Uri.parse(mySongs.get(position).toString());
                 myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                 songTitle = mySongs.get(position).getName().toString();
                 tvTitle.setText(songTitle);
                 myMediaPlayer.start();
            }
        });


        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myMediaPlayer.stop();
                myMediaPlayer.release();

                position = ((position -1) <0) ? mySongs.size()-1 : (position - 1);
                Uri u = Uri.parse(mySongs.get(position).toString());
                myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                songTitle = mySongs.get(position).getName().toString();
                tvTitle.setText(songTitle);
                myMediaPlayer.start();
            }
        });

    }
}