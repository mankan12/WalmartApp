package walmartapp.com.walmartapp;


import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import static walmartapp.com.walmartapp.MovieConstants.TRAILER_ID;
import static walmartapp.com.walmartapp.MovieConstants.YOUTUBE_API_KEY;

public class TrailerActivity extends YouTubeBaseActivity {

    private ProgressBar mLoadingIndicator;
    private static final String LOG_TAG = TrailerActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);
        final YouTubePlayerView youtubePlayerView = findViewById(R.id.youtube_player_view);
        Log.d(LOG_TAG, "videoId:" + getIntent().getStringExtra(TRAILER_ID));
        playVideo(getIntent().getStringExtra(TRAILER_ID), youtubePlayerView);
    }

    public void playVideo(final String videoId, YouTubePlayerView youTubePlayerView) {
        //initialize youtube player view
        youTubePlayerView.initialize(YOUTUBE_API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        youTubePlayer.cueVideo(videoId);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                    }
                });
    }
}