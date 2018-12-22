package walmartapp.com.walmartapp;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int MOVIE_TYPE = 1;

    private List<Movie> moviesList;

    private String LOG_TAG = MovieListAdapter.class.getSimpleName();

    CustomItemClickListener listener;
    public MovieListAdapter(CustomItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case MOVIE_TYPE: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.movie_list_item, parent, false);
                final MovieViewHolder mMovieViewHolder = new MovieViewHolder(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = mMovieViewHolder.getPosition();
                        Log.d(LOG_TAG, "position : " + position + " , " + position);
                        listener.onItemClick(v, moviesList.get(position).movie.toString(), position);
                    }
                });
                return mMovieViewHolder;
            }
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.movie_list_item, parent, false);
                return new MovieViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindMovieViewHolder(holder, position);
    }

    private void bindMovieViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            String title = moviesList.get(position).movie.getString("title");
            ((MovieViewHolder) holder).movieName.setText(title);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String poster_path = moviesList.get(position).movie.getString("poster_path");
            if (poster_path.length() > 0) {
                Glide.with(holder.itemView).load(MovieConstants.BASE_URL + poster_path)
                        .into(((MovieViewHolder) holder).movieAvatar);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String overview = moviesList.get(position).movie.getString("overview");
            ((MovieViewHolder) holder).movieOverview.setText(overview);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (moviesList != null && moviesList.size() != 0) {
            return moviesList.size();
        }
        return count;
    }

    public void setMovieData(List<Movie>  moviesList) {
        if (moviesList != null) {
            this.moviesList = moviesList;
            notifyDataSetChanged();
        }
    }
}
