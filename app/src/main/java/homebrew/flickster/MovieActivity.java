package homebrew.flickster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import homebrew.flickster.adapters.MovieArrayAdapter;
import homebrew.flickster.models.Movie;

public class MovieActivity extends AppCompatActivity {

    ArrayList<Movie> movies;
    MovieArrayAdapter movieAdapter;
    ListView lvItems;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        lvItems = (ListView)findViewById(R.id.lvMovies);
        movies = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieAdapter);
        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJasonResults = null;

                try {
                    movieJasonResults = response.getJSONArray("results");
                    if(savedInstanceState != null) {
                        ArrayList<Movie> savedMovies = savedInstanceState.getParcelableArrayList("savedInstance");
                        movies.addAll(savedMovies);
                        /*
                        Note: notifyDataSetChanged will only be called if we call addAll(), add(), clear(),
                        insert(), remove() methods on ArrayList. direct assignment to ArrayList will not change
                        */
                        Log.d("DEBUG","OnSuccess, getting parceable:" +  movies.toString());
                    }
                    else {
                        movies.addAll(Movie.fromJSONArray(movieJasonResults));
                        Log.d("DEBUG","OnSuccess, getting json:" +  movies.toString());
                    }
                    movieAdapter.notifyDataSetChanged();
                    //Log.d("DEBUG","OnSuccess:" +  movies.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("savedInstance", movies);
        Log.d("DEBUG", "OnSaveInstanceState:" + movies.toString());
        super.onSaveInstanceState(outState);
    }
}
