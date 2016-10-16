package homebrew.flickster.models;

import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by Nishit on 10/12/16.
 */

public class Movie implements Parcelable {

    String posterPath;
    String backdropPath;
    String movieTitle;
    String overview;

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s",posterPath);
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s",backdropPath);
    }

    public Movie () {

    }

    public Movie (JSONObject obj) throws JSONException{
        this.posterPath = obj.getString("poster_path");
        this.backdropPath = obj.getString("backdrop_path");
        this.movieTitle = obj.getString("original_title");
        this.overview = obj.getString("overview");
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<Movie>();

        for(int x = 0; x < array.length(); x++) {
            try {
                results.add(new Movie(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(this.posterPath);
        dest.writeString(this.backdropPath);
        dest.writeString(this.movieTitle);
        dest.writeString(this.overview);
    }

    protected Movie(android.os.Parcel in) {
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
        this.movieTitle = in.readString();
        this.overview = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(android.os.Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
