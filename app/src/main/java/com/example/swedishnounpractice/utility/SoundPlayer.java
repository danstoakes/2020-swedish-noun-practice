package com.example.swedishnounpractice.utility;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageButton;

public class SoundPlayer implements MediaPlayer.OnCompletionListener
{
    private Context context;

    private View view;

    private MediaPlayer player;

    private boolean played;

    public SoundPlayer(Context context)
    {
        this.context = context;

        player = new MediaPlayer();

        played = false;
    }

    public SoundPlayer(Context context, View view)
    {
        this.context = context;
        this.view = view;

        player = new MediaPlayer();
    }

    public void setPlayed (boolean played)
    {
        this.played = played;
    }

    public void playSound (int soundID)
    {
        if (!played)
        {
            played = true;

            player = MediaPlayer.create(context, soundID);
            player.setOnCompletionListener(this);
            player.start();
        }

        played = false;
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        if (view != null)
        {
            ImageButton button = (ImageButton) view; /* ((Activity) context).findViewById(R.id.button2); */
            button.setEnabled(true);
        }
        player.release();
    }
}