package homebrew.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
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

/**
 * Created by Nishit on 10/12/16.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private static class ViewHolder {
        ImageView movieImage;
        TextView movieTitle;
        TextView movieOverview;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);
        int orientation = getContext().getResources().getConfiguration().orientation;
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
            viewHolder.movieImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            viewHolder.movieTitle = (TextView) convertView.findViewById(R.id.tvMovieName);
            viewHolder.movieOverview = (TextView) convertView.findViewById(R.id.tvMovieOverview);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.movieImage.setImageResource(0);

        viewHolder.movieTitle.setText(movie.getMovieTitle());
        viewHolder.movieOverview.setText(movie.getOverview());
        String image_path = movie.getPosterPath();
        Log.d("DEBUG","getView:" + image_path);
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            image_path = movie.getBackdropPath();
            Log.d("DEBUG","LANDSCAPE:" + image_path);
        }

        Picasso.with(getContext()).load(image_path).into(viewHolder.movieImage);
        return convertView;
    }
}
