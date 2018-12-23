package walmartapp.com.walmartapp;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static walmartapp.com.walmartapp.MovieConstants.MOVIE_DETAILS;
import static walmartapp.com.walmartapp.MovieConstants.POSITION;
import static walmartapp.com.walmartapp.MovieConstants.TRAILER_ID;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    TextView mTextViewTitle;
    TextView mTextViewRating;
    TextView mTextViewReleaseDate;
    ImageView mImageViewProfile;
    Button mPlayTrailer;
    TextView mTextViewOriginalLanguage;
    TextView mTextViewAdult;
    TextView mTextViewOverview;
    int mCurrentPosition;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        mTextViewTitle = (TextView) findViewById(R.id.textview_title);
        mTextViewRating = (TextView) findViewById(R.id.textview_rating);
        mImageViewProfile = (ImageView) findViewById(R.id.imageview_profile);
        mTextViewReleaseDate = (TextView) findViewById(R.id.textview_release_date);
        mPlayTrailer = (Button) findViewById(R.id.button_play_trailer);
        mTextViewOriginalLanguage = (TextView) findViewById(R.id.textview_original_language);
        mTextViewAdult = (TextView) findViewById(R.id.textview_adult);
        mTextViewOverview = (TextView) findViewById(R.id.textview_overview);
        mCurrentPosition = getIntent().getIntExtra(POSITION, -1);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        try {
            JSONObject movieDetails = new JSONObject(getIntent().getStringExtra(MOVIE_DETAILS));
            try {
                String name = movieDetails.getString("title");
                mTextViewTitle.setText(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                String vote_average = movieDetails.getString("vote_average");
                mTextViewRating.setText(DetailActivity.this.getResources()
                        .getString(R.string.rating, vote_average));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                String url = movieDetails.getString("poster_path");
                Glide.with(this).load(MovieConstants.BASE_URL + url)
                        .into((mImageViewProfile));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                String releaseDate = movieDetails.getString("release_date");
                mTextViewReleaseDate.setText(DetailActivity.this.getResources()
                        .getString(R.string.released_on, releaseDate));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                final String Id = movieDetails.getString("id");

                mPlayTrailer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new FetchYoutubeTask().execute(Id);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                String language = movieDetails.getString("original_language");
                mTextViewOriginalLanguage.setText(DetailActivity.this.getResources()
                        .getString(R.string.orig_lang, language));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                Boolean isAdult = movieDetails.getBoolean("adult");
                if (isAdult) {
                    mTextViewAdult.setText(R.string.for_adults);
                } else {
                    mTextViewAdult.setText(R.string.general_audience);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                String overview = movieDetails.getString("overview");
                mTextViewOverview.setText(overview);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class FetchYoutubeTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String videoId = params[0];
            URL youtubeRequestUrl = NetworkUtils.buildYoutubeUrl(videoId);

            try {
                String jsonYoutubeResponse = NetworkUtils
                        .getResponseFromHttpUrl(youtubeRequestUrl);

                JSONObject youtubeResponse = new JSONObject(jsonYoutubeResponse);

                JSONArray youtubeResponseArray = youtubeResponse.getJSONArray(MovieConstants.RESULTS);
                JSONObject youtubeObject = youtubeResponseArray.getJSONObject(0);

                return youtubeObject.getString("key");

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String key) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            Intent mIntent = new Intent(DetailActivity.this, TrailerActivity.class);
            mIntent.putExtra(TRAILER_ID, key);
            startActivity(mIntent);
        }
    }
}
