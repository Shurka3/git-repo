package com.example.gunshot;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private SoundPool sounds;
    private int sound_shot;
    private int sound_shot_fals;
    private int sound_baraban;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createSoundPool();
        loadSounds();
    }
    protected void createSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            createNewSoundPool();
        } else {
            createOldSoundPool();
        }
    }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void createNewSoundPool(){
        .setUsage(AudioAttributes.USAGE_GAME)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build();
      AudioAttributes attributes;
      sounds = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();

  }

    private AudioAttributes.Builder setUsage(int usageGame) {



    }

    private AudioAttributes.Builder setUsaage(int usageGame) {
        return null;
    }

    @SuppressWarnings("deprecation")
    protected void createOldSoundPool() { sounds = new SoundPool(5, AudioManager.STREAM_MUSIC,0); }
    private void loadSounds()
    {
        sound_shot = sounds.load(this,R.raw.Gun, 1);
        sound_shot_fals = sounds.load(this,R.raw.GunFree, 1);
        sound_baraban = sounds.load(this,R.raw.Revol, 1);

    }


    public void onShot(View view)
    {
        sounds.play(sound_shot, 1.0f,1.0f, 1,0,1);
    }

    public void onShotFals(View view) {
        sounds.play(sound_shot_fals, 1.0f,1.0f, 1,0,1);
    }

    public void Baraban(View view) {
        sounds.play(sound_baraban, 1.0f,1.0f, 1,0,1);
    }

}
