package com.osias.mymediaplayer;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView songListView;
    ArrayList<Song> songList;
    ArrayAdapter<String> songListViewAdapter;

    Song currentSong = null;

    MediaPlayer mediaPlayer;
    int finalTime, startTime;
    TextView startTimeText;
    TextView finalTimeText;
    TextView titleTextView;
    TextView descriptionTextView;
    Button playPauseButton;

    private Handler myHandler = new Handler();

    SeekBar seekBar;

    Runnable seekBarUpdate = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            startTimeText.setText(String.format("%2d:%2d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekBar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    int selectedSong = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songList = new ArrayList<>();
        songListView = findViewById(R.id.songListview);
        songListViewAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
        songListView.setAdapter(songListViewAdapter);
        songListView.setOnItemClickListener(this);
        seekBar = findViewById(R.id.seekBar);
        GetAllSongs(this);
        for(Song song : songList) {
            songListViewAdapter.add(song.getDisplayName());
        }
        startTimeText = findViewById(R.id.startTimeTextView);
        finalTimeText = findViewById(R.id.endTimeTextView);
        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        playPauseButton = findViewById(R.id.playPauseButton);
    }

    private void GetAllSongs(Context context){
        Uri allSongsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor cursor =  context.getContentResolver().query(allSongsUri, null, null, null, selection);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Song song = new Song(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)),
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)),
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                    songList.add(song);

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        currentSong = songList.get(i);
        if(mediaPlayer != null)
            mediaPlayer.stop();
        mediaPlayer = MediaPlayer.create(this, Uri.parse(currentSong.getPath()));
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        seekBar.setProgress(0);
        seekBar.setMax(finalTime);
        myHandler.postDelayed(seekBarUpdate, 100);
        titleTextView.setText(currentSong.getDisplayName());
        descriptionTextView.setText(currentSong.getArtist());
        PlayCurrentSong();
        selectedSong = i;
    }
    public void PlayPauseButton_OnClick(View view){
        if(selectedSong == -1)
        {
            onItemClick(null, null, 0, 0);
            return;
        }
        if(!mediaPlayer.isPlaying())
        {
            PlayCurrentSong();
            playPauseButton.setText("Pause");
        }

        else {
            PauseCurrentSong();
            playPauseButton.setText("Play");
        }
    }

    public void NextButton_OnClick(View view){
        if(selectedSong+1 < songList.size())
        onItemClick(null, null, ++selectedSong, 0);
    }

    public void PrevButton_OnClick(View view){
        if(selectedSong-1 >= 0)
        onItemClick(null, null, --selectedSong, 0);
    }

    private void PlayCurrentSong(){


        mediaPlayer.start();
    }


    private void PauseCurrentSong(){
        mediaPlayer.pause();
    }

    class Song{
        private int id;
        private String displayName;
        private String artist;
        private String path;

        public Song() {
        }

        public Song(int id, String displayName, String artist, String path) {
            this.id = id;
            this.displayName = displayName;
            this.artist = artist;
            this.path = path;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

}
