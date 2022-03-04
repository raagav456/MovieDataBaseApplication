package Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.volleyparsing.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import Data.MovieRecyclerViewAdapter;
import Model.Movie;
import Util.Constants;
import Util.Prefs;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<Movie> movieList;
    private RequestQueue queue;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Prefs prefs = new Prefs(MainActivity.this);
        String search = prefs.getSearch();

        movieList = new ArrayList<>();

        // getMovies(search);
        movieList = getMovies(search);

        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this, movieList);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        movieRecyclerViewAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_search) {
            showInputDialog();
            // return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showInputDialog() {

        alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_view, null);
        final EditText newSearchEdt = view.findViewById(R.id.searchEdt);
        Button submitButton = view.findViewById(R.id.submitButton);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prefs prefs = new Prefs(MainActivity.this);

                if (!newSearchEdt.getText().toString().isEmpty()) {

                    String search = newSearchEdt.getText().toString();
                    prefs.setSearch(search);
                    movieList.clear();

                    getMovies(search);

                    // movieRecyclerViewAdapter.notifyDataSetChanged();//Very important!!
                }
                dialog.dismiss();
            }
        });
    }

    //Get movies
    public List<Movie> getMovies(String searchTerm) {
        // Every time this method is called it clears all the list and populate.
        movieList.clear();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Constants.URL_LEFT + searchTerm + Constants.URL_RIGHT + Constants.API_KEY, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray moviesArray = response.getJSONArray("Search");

                    for (int i = 0; i < moviesArray.length(); i++) {

                        JSONObject movieObj = moviesArray.getJSONObject(i);

                        Movie movie = new Movie();
                        movie.setTitle(movieObj.getString("Title"));
                        movie.setYear("Year Released: " + movieObj.getString("Year"));
                        movie.setMovieType("Type: " + movieObj.getString("Type"));
                        movie.setPoster(movieObj.getString("Poster"));
                        movie.setImdbId(movieObj.getString("imdbID"));

                        // Log.d("Movies: ", movie.getTitle());
                        movieList.add(movie);
                    }
                    /**
                     * Very important!! Otherwise, we wont see anything being displayed.
                     */
                    movieRecyclerViewAdapter.notifyDataSetChanged();//Important!!

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);
        return movieList;
    }
}
