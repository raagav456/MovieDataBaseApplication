package Data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.volleyparsing.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Activities.MovieDetailActivity;
import Model.Movie;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder>{

    private Context context;
    private List<Movie> movieList;

    public MovieRecyclerViewAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public MovieRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(MovieRecyclerViewAdapter.ViewHolder holder, int position) {

        Movie movie = movieList.get(position);
        String posterLink =  movie.getPoster();

        holder.title.setText(movie.getTitle());
        holder.type.setText(movie.getMovieType());

        Picasso.with(context)
                .load(posterLink)
                .placeholder(android.R.drawable.ic_btn_speak_now)
                .into(holder.poster);

        holder.year.setText(movie.getYear());

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        ImageView poster;
        TextView year;
        TextView type;

        public ViewHolder(View itemView, final Context ctx) {
            super(itemView);
            context = ctx;
            title  = itemView.findViewById(R.id.movieTitleID);
            poster = itemView.findViewById(R.id.movieImageID);
            year   = itemView.findViewById(R.id.movieReleaseID);
            type   = itemView.findViewById(R.id.movieCatID);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie movie = movieList.get(getAdapterPosition());

                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra("movie", movie);
                    ctx.startActivity(intent);

                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
}

