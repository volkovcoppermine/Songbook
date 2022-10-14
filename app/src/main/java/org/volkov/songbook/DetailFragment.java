package org.volkov.songbook;

import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class DetailFragment extends Fragment {
    private static final String TAG = "Detail fragment";
    MediaPlayer player;
    ImageButton playButton;
    SeekBar seekBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        playButton = view.findViewById(R.id.playButton);
        seekBar = view.findViewById(R.id.seekBar);

        playButton.setEnabled(false);
        seekBar.setEnabled(false);
    }

    public void setSelectedItem(Bundle data) {
        ImageView scoreView = getView().findViewById(R.id.scoreView);
        TextView songTextView = getView().findViewById(R.id.songTextView);

        if (player != null) stop();

        String imgPath = data.getString("IMAGE");
        try (InputStream inputStream = getActivity().getApplicationContext().getAssets()
                .open("images/" + imgPath)) {
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            scoreView.setImageDrawable(drawable);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }

        playButton = getView().findViewById(R.id.playButton);
        seekBar = getView().findViewById(R.id.seekBar);
        String soundPath = data.getString("SOUND");
        if (soundPath != null) {
            try {
                AssetFileDescriptor descriptor = getActivity().getApplicationContext().getAssets().openFd("sound/" + soundPath);
                player = new MediaPlayer();
                player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                player.prepare();

                seekBar.setMax(player.getDuration() / 1000);
                playButton.setEnabled(true);
                seekBar.setEnabled(true);
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
            }
        }

        Handler handler = new Handler();
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (player != null) {
                    int mCurrentPosition = player.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                }
                handler.postDelayed(this, 1000);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekBar.getProgress() * 1000);
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Clicked!");
                play(view);
            }
        });

        if (player != null) {
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stop();
                }
            });
        }

        songTextView.setText(Html.fromHtml(data.getString("TEXT")));
    }

    public void play(View view) {
        Log.d(TAG, "Play called");
        if (!player.isPlaying()) {
            Log.d(TAG, "Start");
            player.start();
            playButton.setImageResource(R.drawable.ic_baseline_pause_24);
            // Иначе воспроизведение сразу встанет на паузу
            return;
        }
        if (player.isPlaying()) {
            Log.d(TAG, "Pause");
            player.pause();
            playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }
    }

    public void stop() {
        playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        if (player.isPlaying()) {
            player.stop();
            try {
                player.prepare();
                player.seekTo(0);
                seekBar.setProgress(0);
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            stop();
            player.release();
            player = null;
        }

        playButton.setEnabled(false);
        seekBar.setProgress(0);
        seekBar.setEnabled(false);
    }
}