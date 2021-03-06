package walmartapp.com.walmartapp;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private static final String LOG_TAG = MovieListAdapter.class.getSimpleName();
    ImageView movieAvatar;
    TextView movieName;
    TextView voteAverage;
    TextView movieReleaseDate;

    public MovieViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        movieAvatar = itemView.findViewById(R.id.imageview_profile);
        movieName = itemView.findViewById(R.id.textview_name);
        voteAverage = itemView.findViewById(R.id.textview_vote_average);
        movieReleaseDate = itemView.findViewById(R.id.textview_release_date);
    }

    @Override
    public void onClick(View v) {
        Log.d(LOG_TAG, "onClick " + getPosition());
    }

    public interface OnItemClickListener {
        public void onItemClick(String jsonString);
    }
}
