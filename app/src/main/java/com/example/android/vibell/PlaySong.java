package com.example.android.vibell;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {

//    Importing the required modules

    TextView textView;
    ImageView playButton, nextSong, previousSong;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String textContent;
    int position;
    SeekBar seekBar;
    Uri uri;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        textView= findViewById(R.id.songName);
        playButton = findViewById(R.id.playButton);
        nextSong=findViewById(R.id.nextSong);
        previousSong= findViewById(R.id.previousSong);
        seekBar=findViewById(R.id.seekbar);


// receiving values from the intent at MainActitvity.class

        Intent intent= getIntent();
        Bundle bundle= intent.getExtras();
        songs= (ArrayList) bundle.getParcelableArrayList("songList");

        textContent= intent.getStringExtra("currentSong");
        textView.setText(textContent);
        textView.setSelected(true);

        position= intent.getIntExtra("position", 0);
        uri = Uri.parse(songs.get(position).toString());

        mediaPlayer= MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        Thread updateSeekBar = new Thread(){
            @Override
            public void run() {
                int currentPosition=0;

                try {
                    while(currentPosition<mediaPlayer.getDuration()){
                        currentPosition=mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(800);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateSeekBar.start();


//        PlayButton logic
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    playButton.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
                    mediaPlayer.pause();
                }
                else {
                    playButton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
                    mediaPlayer.start();
                }
            }
        });

//        PreviousButton logic
        previousSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mediaPlayer.stop();
               mediaPlayer.release();
                playButton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);

               if(position!=0){
                   position=position-1;

               }
               else{
                   position=songs.size()-1;
               }
                uri = Uri.parse(songs.get(position).toString());

                mediaPlayer= MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());

                textContent= songs.get(position).getName().toString();
                textView.setText(textContent);
            }
        });

//      NextButton logic
        nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                playButton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);

                if(position!=songs.size()-1){
                    position=position+1;

                }
                else{
                    position=0;
                }
                uri = Uri.parse(songs.get(position).toString());

                mediaPlayer= MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());

                textContent=songs.get(position).getName();
                textView.setText(textContent);

            }
        });
    }
}