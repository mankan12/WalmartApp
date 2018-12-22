package walmartapp.com.walmartapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import static walmartapp.com.walmartapp.MovieConstants.MOVIE_DETAILS;
import static walmartapp.com.walmartapp.MovieConstants.POSITION;


public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MovieListAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie);
        mErrorMessageDisplay = (TextView) findViewById(R.id.error_message_display);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieListAdapter(MainActivity.this, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, String json_object, int position) {
                Log.d(LOG_TAG, "clicked json item:" + json_object);
//                Intent mIntent = new Intent(MainActivity.this, DetailActivity.class);
//                mIntent.putExtra(MOVIE_DETAILS, json_object.toString());
//                mIntent.putExtra(POSITION, position);
//                startActivity(mIntent);
            }
        });

        mRecyclerView.setAdapter(mMovieAdapter);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        loadMovieData();
    }

    /**
     * This method will get movie data in the background thread.
     */
    private void loadMovieData() {
        showMovieDataView();

        new FetchMovieTask().execute();
    }

    /**
     * This method will make the View for the movie data visible and
     * hide the error message.
     */
    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message
     * visible and hide the movie
     * View.
     */
    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            URL movieRequestUrl = NetworkUtils.buildUrl();
            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);

                List<Movie> entries = new ArrayList<Movie>();

                JSONObject movieResponse = new JSONObject(jsonMovieResponse);
                Log.d(LOG_TAG, "movieResponse.len : " + movieResponse.length());

                JSONArray movieResponseArray = movieResponse.getJSONArray(MovieConstants.RESULTS);
                for (int i = 0; i < movieResponseArray.length(); i++) {
                    Log.d(LOG_TAG, "movieResponseArray  : "
                            + movieResponseArray.getJSONObject(i).toString());

                    entries.add(new Movie(movieResponseArray.getJSONObject(i)));
                }

                return entries;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showMovieDataView();

                mMovieAdapter.setMovieData(movieData);

            } else {
                showErrorMessage();
            }
        }
    }
}