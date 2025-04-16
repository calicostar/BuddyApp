package com.example.buddyapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize MediaPlayer with the intro music
        mediaPlayer = MediaPlayer.create(this, R.raw.mymind);

        // Set the volume level (adjust as needed)
        float leftVolume = 1.0f; // Max volume
        float rightVolume = 1.0f; // Max volume
        mediaPlayer.setVolume(leftVolume, rightVolume);

        // Start playing the music
        mediaPlayer.start();

        // Simulate a loading effect by delaying the start of MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Stop the music when transitioning to MainActivity
                mediaPlayer.stop();
                mediaPlayer.release();

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                finish();
            }
        }, 21000); // 5000 milliseconds (5 seconds) - Adjust as needed
    }

    @Override
    protected void onDestroy() {
        // Release MediaPlayer resources when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        super.onDestroy();
    }
}
