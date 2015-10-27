package com.clpstudio.movetimespent.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.clpstudio.movetimespent.Adapters.AutocompleteAdapter;
import com.clpstudio.movetimespent.Adapters.MoviesListAdapter;
import com.clpstudio.movetimespent.R;
import com.clpstudio.movetimespent.model.TvShow;
import com.clpstudio.movetimespent.network.DatabaseSuggestionsRetriever;
import com.clpstudio.movetimespent.network.VolleyRequestQueueSingletone;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements AutocompleteAdapter.OnDropDownListClick, DatabaseSuggestionsRetriever.OnNetworkLoadFinish, MoviesListAdapter.OnDeletedMovie {

    /**
     * Volley request queue
     */
    private RequestQueue requestQueue;

    /**
     * Api key
     */
    public static String API_KEY;

    /**
     * The toolbar
     */
    private Toolbar toolbar;

    /**
     * The autocomplete edit text
     */
    private AutoCompleteTextView mAutoCompleteTextView;

    /**
     * The adapter for autocomplete edit text
     */
    private AutocompleteAdapter mAutocompleteAdapter;

    /**
     * The list of movies to calculate
     */
    private RecyclerView mMoviesList;

    /**
     * Edit text for setting the number of season
     */
    private EditText mSeasonEditText;

    /**
     * For getting server connection
     */
    private DatabaseSuggestionsRetriever mDatabaseSuggestionsRetriever;

    /**
     * The show to be added
     */
    private TvShow mShow;

    /**
     * Text view to show the result in days
     */
    private TextView mDaysSpent;

    /**
     * Text view to show the result in hours
     */
    private TextView mHoursSpent;

    /**
     * Text view to show the result in minutes
     */
    private TextView mMinutesSpent;

    /**
     * The adapter for recycle view movie list
     */
    private MoviesListAdapter mMoviesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNetwork();
        linkUi();
        setupAutocomplete();
        setupMovieList();
        setupListeners();
        setupToolbar();
        setupVolley();
        API_KEY = getString(R.string.api_key);
    }

    /**
     * Add listeners on views
     */
    private void setupListeners() {
        mSeasonEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Integer showSeasonsCount = Integer.parseInt(mShow.getNumberOfSeasons());
                    Integer userInput = Integer.parseInt(mSeasonEditText.getText().toString());
                    if (showSeasonsCount < userInput || userInput < 1) {
                        Toast.makeText(MainActivity.this, getString(R.string.incorrect_seasons), Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        mSeasonEditText.setHint(getString(R.string.hint_seasons_standard));
                        mShow.setSeason(mSeasonEditText.getText().toString());
                        mMoviesListAdapter.add(mShow);

                        resetEditTexts();
                        closeSoftKeyboard();
                        mSeasonEditText.clearFocus();
                        mAutoCompleteTextView.clearFocus();
                        setTime(calculateTimeSpent());
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void closeSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Reset autocomplete and season edit text to normal
     */
    private void resetEditTexts() {
        mAutoCompleteTextView.setText("");
        mSeasonEditText.setText("");
        mSeasonEditText.setVisibility(View.GONE);
    }

    /**
     * Set the recycle list of movies
     */
    private void setupMovieList() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMoviesList.setLayoutManager(layoutManager);
        mMoviesListAdapter = new MoviesListAdapter(this, this);
        mMoviesList.setAdapter(mMoviesListAdapter);
    }

    /**
     * Setup the autocomplete
     */
    private void setupAutocomplete() {
        mAutocompleteAdapter = new AutocompleteAdapter(this, this);
        mAutoCompleteTextView.setAdapter(mAutocompleteAdapter);
        mAutoCompleteTextView.requestFocus();
    }

    /**
     * Setup server connection object
     */
    private void setupNetwork() {
        mDatabaseSuggestionsRetriever = new DatabaseSuggestionsRetriever(this);
    }

    /**
     * Link the ui
     */
    private void linkUi() {
        mAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_text_view);
        mMoviesList = (RecyclerView) findViewById(R.id.movies_list);
        mSeasonEditText = (EditText) findViewById(R.id.seasons_edit_text);
        mSeasonEditText.setVisibility(View.GONE);
        mDaysSpent = (TextView)findViewById(R.id.days);
        mHoursSpent = (TextView)findViewById(R.id.hours);
        mMinutesSpent = (TextView)findViewById(R.id.minutes);
    }


    /**
     * Setup volley library for http calls
     */
    private void setupVolley() {
        requestQueue = VolleyRequestQueueSingletone.getInstance(this).getRequestQueue();
    }

    /**
     * Setup the toolbar
     */
    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Called when drop down suggestion list is clicked
     *
     * @param show The show
     */
    @Override
    public void onSuggestionClick(TvShow show) {
        mDatabaseSuggestionsRetriever.getTvShowById(String.valueOf(show.getId()), this);
    }

    /**
     * Called when the data from server finished loading
     *
     * @param show The newest created show
     */
    @Override
    public void onNetworkLoadFinish(TvShow show) {
        mShow = show;
        mAutoCompleteTextView.setText(show.getName());
        mAutoCompleteTextView.dismissDropDown();
        mSeasonEditText.setVisibility(View.VISIBLE);
        mSeasonEditText.setHint("1 - " + show.getNumberOfSeasons());
        mSeasonEditText.requestFocus();
    }

    private List<String> calculateTimeSpent() {
        List<TvShow> showList = mMoviesListAdapter.getData();

        int minutes = 0;

        for (TvShow show : showList) {
            for (int i =0; i < Integer.parseInt(show.getNumberOfSeasons()); i++) {
                if(show.getEpisodesRunTime().size() > 0){
                    int timePerSeason = Integer.parseInt(show.getEpisodesRunTime().get(0)) * Integer.parseInt(show.getSeasonsEpisodesNumber().get(String.valueOf(i)));
                    minutes += timePerSeason;
                }
            }

        }

        int seconds = minutes * 60;
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        List<String> result = new ArrayList<>();
        result.add(String.valueOf(day));
        result.add(String.valueOf(hours));
        result.add(String.valueOf(minute));

        return result;
    }

    /**
     * Recalculate the time when deleted one movie
     */
    @Override
    public void onDeleteMovie() {
        setTime(calculateTimeSpent());
    }

    private void setTime(List<String> timeList){
        mDaysSpent.setText(timeList.get(0));
        mHoursSpent.setText(timeList.get(1));
        mMinutesSpent.setText(timeList.get(2));
    }
}
