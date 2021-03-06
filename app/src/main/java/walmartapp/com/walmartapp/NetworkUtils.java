package walmartapp.com.walmartapp;


import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to
 * communicate with the movie servers.
 */

public final class NetworkUtils {

    private static final String TAG =
            NetworkUtils.class.getSimpleName();

    private static final String MOVIE_URL =
            "https://api.themoviedb.org/3/discover/movie?api_key=db637646bffa9037e9af87ff3295ec21&sort_by=popularity.desc";

    private static final String TRAILER_URL_PART1 =
            "http://api.themoviedb.org/3/movie/";

    private static final String TRAILER_URL_PART2 =
            "/videos?api_key=db637646bffa9037e9af87ff3295ec21";

    public static URL buildUrl() {
        Uri builtUri = Uri.parse(MOVIE_URL).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * Builds the URL used to talk to the movie server.
     */
    public static URL buildYoutubeUrl(String videoId) {
        String youtube_url = TRAILER_URL_PART1 + videoId + TRAILER_URL_PART2;
        Uri builtUri = Uri.parse(youtube_url).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}