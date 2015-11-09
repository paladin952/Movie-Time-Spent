package com.clpstudio.tvshowtimespent.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
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

import com.clpstudio.tvshowtimespent.R;
import com.clpstudio.tvshowtimespent.Utils.FacebookUtils;
import com.clpstudio.tvshowtimespent.Utils.SnackBarUtils;
import com.clpstudio.tvshowtimespent.adapters.AutocompleteAdapter;
import com.clpstudio.tvshowtimespent.adapters.MoviesListAdapter;
import com.clpstudio.tvshowtimespent.loaders.DatabaseLoader;
import com.clpstudio.tvshowtimespent.model.TvShow;
import com.clpstudio.tvshowtimespent.network.DatabaseSuggestionsRetriever;
import com.clpstudio.tvshowtimespent.network.NetworkUtils;
import com.clpstudio.tvshowtimespent.persistance.DatabaseDAO;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements AutocompleteAdapter.OnDropDownListClick,
        DatabaseSuggestionsRetriever.OnNetworkLoadFinish, MoviesListAdapter.OndMovieEventListener,
        LoaderManager.LoaderCallbacks<List<TvShow>> {

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
     * The coordinator layout for snackbar-
     */
    private CoordinatorLayout mCoordinatorLayout;

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
    private Handler mDelayHandler = new Handler();

    /**
     * The database dao
     */
    private DatabaseDAO mDatabaseDAO;

    /**
     * Internet permission request code
     */
    private static final int PERMISSION_INTERNET_REQUEST_CODE = 0;

    /**
     * Internet permission request code
     */
    private static final int PERMISSION_ACCESSE_NETWORK_STATE_REQUEST_CODE = 1;

    /**
     * one second in milliseconds
     */
    private static final int ONE_SECOND = 1000;

    /**
     * To know when there are position changes in list
     */
    private boolean mPositionChanged;

    /**
     * The tv show to be undo
     */
    private TvShow undoShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkForInternetPermission();
        mCallbacks = this;
        setupNetwork();
        setupDatabase();
        linkUi();
        setupAutocomplete();
        setupMovieList();
        setupListeners();
        setupToolbar();
        setupFacebookSdk();
    }

    @Override
    protected void onDestroy() {
        mDatabaseDAO.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForInternetPermission();
        getSupportLoaderManager().initLoader(DatabaseLoader.LOADER_ID, null, mCallbacks).forceLoad();
    }

    @Override
    protected void onPause() {
        super.onPause();

        /**If are changes in list must be stored in database*/
        if (mPositionChanged) {
            /**recalculate each show the position in db*/
            mMoviesListAdapter.reorderPosition();
            for (TvShow show : mMoviesListAdapter.getData()) {
                mDatabaseDAO.updateCurrencyItem(show.getId(), show.getName(), show.getNumberOfSeasons(),
                        String.valueOf(show.getMinutesTotalTime()), show.getPosterUrl(), show.getPositionInList());
            }
            mPositionChanged = false;
        }
    }

    /**
     * Request for permissions
     */
    private void checkForInternetPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PERMISSION_INTERNET_REQUEST_CODE);
        }
    }

    /**
     * request accesse internet permission
     */
    private void checkForAcceseInternetPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, PERMISSION_INTERNET_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_INTERNET_REQUEST_CODE:
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    SnackBarUtils.snackError(this, mCoordinatorLayout, getString(R.string.permission_error));
                    mDelayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, ONE_SECOND);
                }
                break;
            case PERMISSION_ACCESSE_NETWORK_STATE_REQUEST_CODE:
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    SnackBarUtils.snackError(this, mCoordinatorLayout, getString(R.string.permission_error));
                    mDelayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, ONE_SECOND);
                }
                break;
        }
    }

    /**
     * Setup for facebook sharing
     */
    private void setupFacebookSdk() {
        FacebookSdk.sdkInitialize(this);
        mFacebookShareDialog = new ShareDialog(this);
        callbackManager = CallbackManager.Factory.create();
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
        switch (item.getItemId()) {
            case R.id.action_share_facebook:
                checkForAcceseInternetPermission();
                if (NetworkUtils.isNetworkAvailable(this)) {
                    FacebookUtils.shareLinkOnFacebook(this, mFacebookShareDialog, mDaysSpent.getText().toString(), mHoursSpent.getText().toString(), mMinutesSpent.getText().toString());
                } else {
                    SnackBarUtils.snackError(this, mCoordinatorLayout, getString(R.string.toast_no_internet));
                }
                return true;
            case R.id.action_about:
                Snackbar snackbar = Snackbar.make(mCoordinatorLayout, getString(R.string.about_us), Snackbar.LENGTH_LONG);
                snackbar.setAction(getString(R.string.go_to_site), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (NetworkUtils.isNetworkAvailable(MainActivity.this)) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.the_movie_db_link)));
                            startActivity(browserIntent);
                        } else {
                            SnackBarUtils.snackError(MainActivity.this, mCoordinatorLayout, getString(R.string.toast_no_internet));
                        }
                    }
                });
                snackbar.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                    checkForAcceseInternetPermission();
                    String text = mSeasonEditText.getText().toString();
                    if (NetworkUtils.isNetworkAvailable(MainActivity.this)) {
                        if (!text.isEmpty()) {
                            Integer showSeasonsCount = Integer.parseInt(mShow.getNumberOfSeasons());
                            Integer userInput = Integer.parseInt(text);
                            if (showSeasonsCount < userInput || userInput < 1) {
                                SnackBarUtils.snackStandard(mCoordinatorLayout, getString(R.string.incorrect_seasons));
                                return true;
                            } else {
                                mSeasonEditText.setHint(getString(R.string.hint_seasons_standard));
                                mShow.setSeasonsNumber(mSeasonEditText.getText().toString());
                                mShow.setMinutesTotalTime(calculateTimeForOneShow(mShow));

                                //set show id to be the same with the database id
                                //so it can be deleted easily
                                int databaseId = mDatabaseDAO.addTvShowItem(mShow.getName(), mShow.getNumberOfSeasons(),
                                        String.valueOf(mShow.getMinutesTotalTime()), mShow.getPosterUrl(), mMoviesListAdapter.getItemCount());
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
                    } else {
                        SnackBarUtils.snackError(MainActivity.this, mCoordinatorLayout, getString(R.string.toast_no_internet));
                    }
                }
                return false;
            }
        });

        /**listener for drag and drop*/
        ItemTouchHelper.Callback itemMoveCallbacks = new ItemTouchHelper.Callback() {
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Collections.swap(mMoviesListAdapter.getData(), viewHolder.getAdapterPosition(), target.getAdapterPosition());
                mMoviesListAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                mPositionChanged = true;
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //TODO
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP);
            }
        };

        /**listener for swipe*/
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int position = viewHolder.getAdapterPosition();
                final int databaseId = mMoviesListAdapter.getData().get(position).getId();

                undoShow = mMoviesListAdapter.getData().get(position);
                mDatabaseDAO.deleteTvShow(databaseId);
                mMoviesListAdapter.deleteItem(position);

                Snackbar snackbar = Snackbar
                        .make(mCoordinatorLayout, "", Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.undo), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                undo(undoShow, position);
                                setTime(calculateTimeSpent());
                                undoShow = null;
                            }
                        });

                snackbar.show();
                setTime(calculateTimeSpent());
                mPositionChanged = true;

            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                getDefaultUIUtil().clearView(((MoviesListAdapter.ViewHolder) viewHolder).getRemovableView());
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    getDefaultUIUtil().onSelected(((MoviesListAdapter.ViewHolder) viewHolder).getRemovableView());
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                getDefaultUIUtil().onDraw(c, recyclerView, (((MoviesListAdapter.ViewHolder) viewHolder).getRemovableView()), dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                getDefaultUIUtil().onDrawOver(c, recyclerView, (((MoviesListAdapter.ViewHolder) viewHolder).getRemovableView()), dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(itemMoveCallbacks);
        ItemTouchHelper swipeHelper = new ItemTouchHelper(simpleItemTouchCallback);

        touchHelper.attachToRecyclerView(mMoviesList);
        swipeHelper.attachToRecyclerView(mMoviesList);
    }

    /**
     * Undo last deleted show
     *
     * @param show     The last deleted show
     * @param position The last position in list
     */
    private void undo(TvShow show, int position) {
        if(show!= null){
            mDatabaseDAO.addTvShowItem(show.getName(), show.getNumberOfSeasons(), String.valueOf(show.getMinutesTotalTime()), show.getPosterUrl(), show.getPositionInList());
            mMoviesListAdapter.add(position, show);
            show = null;
        }
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
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
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
        SnackBarUtils.snackStandard(mCoordinatorLayout, getString(R.string.toast_double_back_click));

        mDelayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.mDoubleBackToExitPressedOnce = false;
            }
        }, ONE_SECOND);
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

        if (show.getNumberOfEpisoades() == 0) {
            mSeasonEditText.setHint(getString(R.string.no_seasons_available));
        } else {
            mSeasonEditText.setHint("Seasons 1 - " + show.getNumberOfSeasons());
        }
        mSeasonEditText.requestFocus();
    }

    @Override
    public void onItemClick(int position) {
        mMoviesList.smoothScrollToPosition(position);
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

    /**
     * Loader listeners
     */
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
