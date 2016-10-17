package homebrew.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.media.Image;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import homebrew.flickster.R;
import homebrew.flickster.models.Movie;

import static android.R.attr.orientation;
import static android.R.transition.move;
import static homebrew.flickster.models.Movie.MovieType.POPULAR;
import static homebrew.flickster.models.Movie.MovieType.REGULAR;

/**
 * Created by Nishit on 10/12/16.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private static class ViewHolderRegularMovies {
        ImageView movieImage;
        TextView movieTitle;
        TextView movieOverview;
    }

    private static class ViewHolderPopularMovies {
        ImageView movieImage;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);

    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getMovieType().ordinal();
    }

    @Override
    public int getViewTypeCount() {
        return Movie.MovieType.values().length;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);
        if(convertView == null) {
            if(getItemViewType(position) == POPULAR.ordinal()) {
                ViewHolderPopularMovies viewHolder = new ViewHolderPopularMovies();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.popular_movie, parent, false);
                viewHolder.movieImage = (ImageView) convertView.findViewById(R.id.ivPopularMovieImage);
                convertView.setTag(viewHolder);
                setupViewHolderForPopularMovies(viewHolder, movie);
            }
            else {
                ViewHolderRegularMovies viewHolder = new ViewHolderRegularMovies();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
                viewHolder.movieImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
                viewHolder.movieTitle = (TextView) convertView.findViewById(R.id.tvMovieName);
                viewHolder.movieOverview = (TextView) convertView.findViewById(R.id.tvMovieOverview);
                convertView.setTag(viewHolder);
                setupViewHolderForRegularMovies(viewHolder, movie);
            }
        }
        else {
            if(getItemViewType(position) == POPULAR.ordinal()) {
                ViewHolderPopularMovies viewHolder = (ViewHolderPopularMovies) convertView.getTag();
                setupViewHolderForPopularMovies(viewHolder, movie);
            }
            else {
                ViewHolderRegularMovies viewHolder = (ViewHolderRegularMovies) convertView.getTag();
                setupViewHolderForRegularMovies(viewHolder, movie);
            }
        }

        return convertView;
    }

    private void setupViewHolderForRegularMovies (ViewHolderRegularMovies viewHolder, Movie movie) {
        int orientation = getContext().getResources().getConfiguration().orientation;
        viewHolder.movieImage.setImageResource(0);

        viewHolder.movieTitle.setText(movie.getMovieTitle());
        viewHolder.movieOverview.setText(movie.getOverview());
        String image_path = movie.getPosterPath();
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            image_path = movie.getBackdropPath();
        }
        Picasso.with(getContext()).load(image_path).into(viewHolder.movieImage);
    }

    private void setupViewHolderForPopularMovies (ViewHolderPopularMovies viewHolder, Movie movie) {
        viewHolder.movieImage.setImageResource(0);
        String image_path = movie.getPosterPath();
        Picasso.with(getContext()).load(image_path).into(viewHolder.movieImage);
    }
}
