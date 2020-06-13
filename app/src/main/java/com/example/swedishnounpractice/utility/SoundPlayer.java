/* Finalised on 06/06/2020 */

package com.example.swedishnounpractice.utility;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundPlayer implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener
{
    private Context context;

    private MediaPlayer player;

    private boolean playing;

    public SoundPlayer (Context context)
    {
        this.context = context;

        player = new MediaPlayer ();
    }

    @Override
    public void onPrepared (MediaPlayer mp)
    {
        playing = true;

        mp.start ();
    }

    @Override
    public void onCompletion (MediaPlayer mp)
    {
        playing = false;
    }

    public void playSound (int soundID)
    {
        if (!playing)
        {
            player = MediaPlayer.create (context, soundID);
            player.setOnPreparedListener (this);
            player.setOnCompletionListener (this);
        }
    }
}