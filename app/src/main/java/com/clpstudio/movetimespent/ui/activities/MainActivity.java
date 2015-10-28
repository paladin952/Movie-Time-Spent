package com.clpstudio.movetimespent.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clpstudio.movetimespent.R;
import com.clpstudio.movetimespent.Utils.FacebookUtils;
import com.clpstudio.movetimespent.adapters.AutocompleteAdapter;
import com.clpstudio.movetimespent.adapters.MoviesListAdapter;
import com.clpstudio.movetimespent.loaders.DatabaseLoader;
import com.clpstudio.movetimespent.model.TvShow;
import com.clpstudio.movetimespent.network.DatabaseSuggestionsRetriever;
import com.clpstudio.movetimespent.network.NetworkUtils;
import com.clpstudio.movetimespent.persistance.DatabaseDAO;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements AutocompleteAdapter.OnDropDownListClick,
        DatabaseSuggestionsRetriever.OnNetworkLoadFinish,
        MoviesListAdapter.OnDeletedMovie, LoaderManager.LoaderCallbacks<List<TvShow>> {

    /**
     * Api key
     */
    public static String API_KEY;

    /**
     * The mToolbar
     */
    private Toolbar mToolbar;

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

    /**
     * Loader's callbacks
     */
    private LoaderManager.LoaderCallbacks<List<TvShow>> mCallbacks;

    /**
     * Boolean to check if back button was double pressed
     */
    private boolean mDoubleBackToExitPressedOnce;

    /**
     * Facebook callbacks
     */
    private CallbackManager callbackManager;

    /**
     * Dialog for share
     */
    private ShareDialog mFacebookShareDialog;


    /**
     * Handler for delay operations
     */
    private Handler mHandler = new Handler();

    /**
     * The database dao
     */
    private DatabaseDAO mDatabaseDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCallbacks = this;
        setupNetwork();
        setupDatabase();
        linkUi();
        setupAutocomplete();
        setupMovieList();
        setupListeners();
        setupToolbar();
        setupFacebookSdk();
        API_KEY = getString(R.string.api_key);
        Log.d("face", FacebookUtils.getKeyHash(this));


    }

    @Override
    protected void onDestroy() {
        mDatabaseDAO.close();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        android.support.v4.app.LoaderManager loaderManager = getSupportLoaderManager();
        int CURRENT_LOADER_ID = DatabaseLoader.LOADER_ID;
        loaderManager.initLoader(CURRENT_LOADER_ID, null, mCallbacks);
    }

    /**
     * Setup for facebook sharing
     */
    private void setupFacebookSdk() {
        FacebookSdk.sdkInitialize(this);
        mFacebookShareDialog = new ShareDialog(this);
        callbackManager = CallbackManager.Factory.create();

        mFacebookShareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d("luci", "share dialog success");
            }

            @Override
            public void onCancel() {
                Log.d("luci", "share dialog cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("luci", "share dialog error");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_share_facebook){
            if(NetworkUtils.isNetworkAvailable(this)){
//                shareCustomGraph();
                FacebookUtils.shareLinkOnFacebook(this, mFacebookShareDialog, mDaysSpent.getText().toString(), mHoursSpent.getText().toString(), mMinutesSpent.getText().toString());
            }else{
                Toast.makeText(this, getString(R.string.toast_no_internet), Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void shareCustomGraph(){

        // Create an object
        ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                .putString("og:type", "video.tv_show")
                .putString("og:title", "Movie Time Spent")
                .putString("og:description", "Android app for calculating time spent watching tv shows")
                .build();

        // Create an action
        ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                .setActionType("books.reads")
                .putObject("book", object)
                .build();

        // Create the content
        ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                .setPreviewPropertyName("book")
                .setAction(action)
                .build();

        ShareDialog.show(this, content);

    }

    /**
     * Prepare database objects
     */
    private void setupDatabase() {
        mDatabaseDAO = new DatabaseDAO(this);
        mDatabaseDAO.open();
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
                        mShow.setSeasonsNumber(mSeasonEditText.getText().toString());
                        mShow.setMinutesTotalTime(calculateTimeForOneShow(mShow));

                        //set show id to be the same with the database id
                        //so it can be deleted easily
                        int databaseId = mDatabaseDAO.addTvShowItem(mShow.getName(), mShow.getNumberOfSeasons(), String.valueOf(mShow.getMinutesTotalTime()), mShow.getPosterUrl());
                        mShow.setId(databaseId);
                        mMoviesListAdapter.add(mShow);

                        //add to database
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

    /**
     * Close the keyboard automatically
     */
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
        mDaysSpent = (TextView) findViewById(R.id.days);
        mHoursSpent = (TextView) findViewById(R.id.hours);
        mMinutesSpent = (TextView) findViewById(R.id.minutes);
    }

    /**
     * Setup the mToolbar
     */
    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    /**
     * Calculate the total time spent from one show
     *
     * @param show The show
     * @return int minutes
     */
    private int calculateTimeForOneShow(TvShow show) {
        int minutes = 0;
        for (int i = 0; i < Integer.parseInt(show.getNumberOfSeasons()); i++) {
            if (show.getEpisodesRunTime().size() > 0) {
                int timePerSeason = Integer.parseInt(show.getEpisodesRunTime().get(0)) * Integer.parseInt(show.getSeasonsEpisodesNumber().get(String.valueOf(i)));
                minutes += timePerSeason;
            }
        }
        return minutes;
    }

    /**
     * Calculate the total time spent
     *
     * @return A list<string> of time {day, hours, minutes}
     */
    private List<String> calculateTimeSpent() {
        List<TvShow> showList = mMoviesListAdapter.getData();

        int totalMinutes = 0;
        for (TvShow show : showList) {
            totalMinutes += show.getMinutesTotalTime();
        }

        int seconds = totalMinutes * 60;
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
     * Set the time on ui
     *
     * @param timeList The list<string> of time {day, hours, minutes}
     */
    private void setTime(List<String> timeList) {
        mDaysSpent.setText(timeList.get(0));
        mHoursSpent.setText(timeList.get(1));
        mMinutesSpent.setText(timeList.get(2));
    }

    @Override
    public void onBackPressed() {
        if (this.mDoubleBackToExitPressedOnce) {
            finish();
            return;
        }

        mDoubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.toast_double_back_click), Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.mDoubleBackToExitPressedOnce = false;
            }
        }, 1000);
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

    /**
     * Recalculate the time when deleted one movie
     */
    @Override
    public void onDeleteMovie(int id) {
        setTime(calculateTimeSpent());
        mDatabaseDAO.deleteTvShow(id);
    }

    /**
     * Called when drop down suggestion list is clicked
     *
     * @param show The show
     */
    @Override
    public void onSuggestionClickListener(TvShow show) {
        mDatabaseSuggestionsRetriever.getTvShowById(String.valueOf(show.getId()), this);
    }

    /**Loader listeners*/
    @Override
    public Loader<List<TvShow>> onCreateLoader(int id, Bundle args) {
        return new DatabaseLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<TvShow>> loader, List<TvShow> data) {
        mMoviesListAdapter.addAll(data);
        setTime(calculateTimeSpent());
    }

    @Override
    public void onLoaderReset(Loader<List<TvShow>> loader) {

    }

}
