package walmartapp.com.walmartapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.json.JSONException;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int MOVIE_TYPE = 1;

    private List<Movie> moviesList;
    private Context mContext;

    private String LOG_TAG = MovieListAdapter.class.getSimpleName();

    CustomItemClickListener listener;
    public MovieListAdapter(Context context, CustomItemClickListener listener) {
        mContext = context;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
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
            String voteAverage = moviesList.get(position).movie.getString("vote_average");
            ((MovieViewHolder) holder).voteAverage.setText(mContext.getResources()
                    .getString(R.string.rating, voteAverage));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String releaseDate = moviesList.get(position).movie.getString("release_date");
            ((MovieViewHolder) holder).movieReleaseDate.
                    setText(mContext.getResources().getString(R.string.released_on, releaseDate));
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
