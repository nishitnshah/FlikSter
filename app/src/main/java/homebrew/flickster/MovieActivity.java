package homebrew.flickster;

import android.support.v4.widget.SwipeRefreshLayout;
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
    private SwipeRefreshLayout swipeContainer;

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

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMovieJsonData();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        if(savedInstanceState != null) {
            ArrayList<Movie> savedMovies = savedInstanceState.getParcelableArrayList("savedInstance");
            movies.addAll(savedMovies);
            /*
            Note: notifyDataSetChanged will only be called if we call addAll(), add(), clear(),
            insert(), remove() methods on ArrayList. direct assignment to ArrayList will not change
            */
        }
        else {
            getMovieJsonData();
        }
    }

    public void getMovieJsonData () {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getString(R.string.url) , new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJasonResults = null;

                try {
                    movieJasonResults = response.getJSONArray("results");
                    movieAdapter.clear();
                    movies.addAll(Movie.fromJSONArray(movieJasonResults));
                    movieAdapter.notifyDataSetChanged();
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
        super.onSaveInstanceState(outState);
    }
}
