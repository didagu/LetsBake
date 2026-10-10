package com.example.android.letsbake.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.letsbake.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import static com.example.android.letsbake.MainActivity.isTablet;
import static com.example.android.letsbake.fragments.StepsActivityFragment.steps;

/**
 * Created by Dominic Idagu on 02/07/2017.
 */

public class StepsDetailsActivityFragment extends Fragment implements Player.Listener {

    private static final String TAG = StepsDetailsActivityFragment.class.getSimpleName();

    private TextView longDescription;
    private Button prev;
    private Button next;
    protected static int index = 0;
    private ExoPlayer exoPlayer;
    private StyledPlayerView playerView;
    private static MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private static long position = 0;

    public StepsDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps_details, container, false);

        longDescription = (TextView) view.findViewById(R.id.description);
        prev = (Button) view.findViewById(R.id.prev);
        next = (Button) view.findViewById(R.id.next);
        playerView = (StyledPlayerView) view.findViewById(R.id.playerView);
        initializeMediaSession();
        initializePlayer(Uri.parse(steps.get(index).getVideoURL()));

        if (!isTablet) {
            index = getActivity().getIntent().getExtras().getInt("item");
        }

        getActivity().setTitle(steps.get(index).getShortDescription());
        longDescription.setText(steps.get(index).getDescription());

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index > 0) {
                    index--;
                    longDescription.setText(steps.get(index).getDescription());
                    restExoPlayer(0, false);
                    initializePlayer(Uri.parse(steps.get(index).getVideoURL()));
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < steps.size() - 1) {
                    index++;
                    longDescription.setText(steps.get(index).getDescription());
                    restExoPlayer(0, false);
                    initializePlayer(Uri.parse(steps.get(index).getVideoURL()));
                }
            }
        });

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !isTablet) {
            hideSystemUI();
            playerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            //restExoPlayer(position, true);
            longDescription.setVisibility(View.GONE);
            prev.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
        }
        return view;
    }

    private void hideSystemUI() {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(getContext(), TAG);
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMediaButtonReceiver(null);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MySessionCallback());
        mediaSession.setActive(true);
    }


    private void initializePlayer(Uri mediaUri) {
        if (exoPlayer == null) {
            exoPlayer = new ExoPlayer.Builder(requireContext()).build();
            playerView.setPlayer(exoPlayer);
            exoPlayer.addListener(this);
            exoPlayer.setMediaItem(MediaItem.fromUri(mediaUri));
            exoPlayer.prepare();
            restExoPlayer(position, false);
        }
    }


    private void restExoPlayer(long position, boolean playWhenReady) {
        this.position = position;
        exoPlayer.seekTo(position);
        exoPlayer.setPlayWhenReady(playWhenReady);
    }

    private void releasePlayer() {
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        exoPlayer.setPlayWhenReady(false);
        mediaSession.setActive(false);

    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        mediaSession.setActive(true);
    }

    @Override
    public void onPlaybackStateChanged(int playbackState) {
        if (playbackState == Player.STATE_READY) {
            position = exoPlayer.getCurrentPosition();
        }
        mediaSession.setPlaybackState(stateBuilder.build());
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            restExoPlayer(0, false);
        }
    }

}
