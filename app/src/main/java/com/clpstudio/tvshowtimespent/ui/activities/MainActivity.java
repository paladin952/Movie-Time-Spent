package com.clpstudio.tvshowtimespent.ui.activities;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.clpstudio.tvshowtimespent.BuildConfig;
import com.clpstudio.tvshowtimespent.R;
import com.clpstudio.tvshowtimespent.adapters.AutocompleteAdapter;
import com.clpstudio.tvshowtimespent.adapters.MoviesListAdapter;
import com.clpstudio.tvshowtimespent.loaders.DatabaseLoader;
import com.clpstudio.tvshowtimespent.model.DbTvShow;
import com.clpstudio.tvshowtimespent.network.NetworkUtils;
import com.clpstudio.tvshowtimespent.network.RetrofitServiceFactory;
import com.clpstudio.tvshowtimespent.network.interfaces.IMovieDbService;
import com.clpstudio.tvshowtimespent.network.model.ApiResult;
import com.clpstudio.tvshowtimespent.network.model.TvShow;
import com.clpstudio.tvshowtimespent.persistance.DatabaseDAO;
import com.clpstudio.tvshowtimespent.persistance.preferences.ISharedPreferences;
import com.clpstudio.tvshowtimespent.persistance.preferences.SharedPreferencesFactory;
import com.clpstudio.tvshowtimespent.utils.Constants;
import com.clpstudio.tvshowtimespent.utils.SnackBarUtils;
import com.clpstudio.tvshowtimespent.utils.TimeUtils;
import com.clpstudio.tvshowtimespent.utils.Utils;
import com.jakewharton.rxbinding.widget.RxSearchView;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity implements AutocompleteAdapter.OnDropDownListClick,
        MoviesListAdapter.OndMovieEventListener, LoaderManager.LoaderCallbacks<List<DbTvShow>> {

    /**
     * The log tag
     */
    private static final String LOG_TAG = MainActivity.class.toString();
    /**
     * The mToolbar
     */
    private Toolbar mToolbar;

    /**
     * The list of movies to calculate
     */
    private RecyclerView mMoviesList;

    /**
     * Edit text for setting the number of season
     */
    private EditText mSeasonEditText;

    /**
     * The last tvShow donwloaded form the server
     */
    private TvShow mTvShow;

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
    private LoaderManager.LoaderCallbacks<List<DbTvShow>> mCallbacks;

    /**
     * Boolean to check if back button was double pressed
     */
    private boolean mDoubleBackToExitPressedOnce;

    /**
     * Handler for delay operations
     */
    private Handler mDelayHandler = new Handler();

    /**
     * The database dao
     */
    private DatabaseDAO mDatabaseDAO;

    /**
     * The search view
     */
    private SearchView mSearchView;

    /**
     * The adapter for the autocomplete list
     */
    private AutocompleteAdapter mAutocompleteAdapter;

    /**
     * The container view for add and season edit text
     */
    @Bind(R.id.add_container)
    View mAddContainer;

    /**
     * The add button binding
     */
    @Bind(R.id.add_button)
    Button mAddButton;

    @Bind(R.id.autocomplete_list)
    RecyclerView mAutoCompleteList;

    /**
     * Called when add button is clicked
     */
    @OnClick(R.id.add_button)
    void onAddClick() {
        String seasonsText = mSeasonEditText.getText().toString();

        if (TextUtils.isEmpty(seasonsText) || mTvShow == null) {
            return;
        }

        DbTvShow dbTvShow;
        dbTvShow = convertToDbTvShow(mTvShow);
        Integer showSeasonsCount = Integer.parseInt(dbTvShow.getNumberOfSeasons());
        Integer userInput = Integer.parseInt(seasonsText);
        if (showSeasonsCount < userInput || userInput < 1) {
            SnackBarUtils.snackStandard(mCoordinatorLayout, getString(R.string.incorrect_seasons));
            return;
        }

        dbTvShow.setSeasonsNumber(mSeasonEditText.getText().toString());
        dbTvShow.setMinutesTotalTime(TimeUtils.calculateTimeForOneShow(dbTvShow));

        saveToDb(dbTvShow);
        mMoviesListAdapter.add(dbTvShow, 0);
        updateDatabase();
        resetAfterSaveInDb();

    }

    /**
     * Updating the database with the new items
     */
    public void updateDatabase() {
        mMoviesListAdapter.reorderPosition();
        mDatabaseDAO.rearangeDataInDb(mMoviesListAdapter.getData());
    }

    /**
     * Resetting the state of the activity after we save in database the movie
     */
    private void resetAfterSaveInDb() {
        resetAdd();
        closeSoftKeyboard();
        mSeasonEditText.clearFocus();
        setTime(TimeUtils.calculateTimeSpent(mMoviesListAdapter.getData()));
        mTvShow = null;
        mSearchView.setQuery("", false);
        mSearchView.clearFocus();
    }

    /**
     * Saving a tv show to database
     *
     * @param dbTvShow The tv show to be saved
     */
    private void saveToDb(DbTvShow dbTvShow) {
        int databaseId = mDatabaseDAO.addTvShowItem(dbTvShow.getName(), dbTvShow.getNumberOfSeasons(),
                String.valueOf(dbTvShow.getMinutesTotalTime()), dbTvShow.getPosterUrl(), mMoviesListAdapter.getItemCount());
        dbTvShow.setId(databaseId);
    }

    /**
     * Converting a {@link TvShow} to {@link DbTvShow}
     *
     * @param tvShow The tvshow to be converter
     * @return A new DbTvShow object
     */
    public DbTvShow convertToDbTvShow(TvShow tvShow) {
        DbTvShow dbTvShow = new DbTvShow(tvShow.getId(), tvShow.getName());
        dbTvShow.setPosterUrl(tvShow.getPosterPath());

        dbTvShow.setNumberOfEpisoades((tvShow.getNumberOfEpisodes() != null) ? tvShow.getNumberOfEpisodes() : 0);
        dbTvShow.setEpisodesRunTime(Utils.convertListOfIntegersToString(tvShow.getEpisodeRunTime()));
        dbTvShow.setSeasonsEpisodes(tvShow.getSeasons());
        dbTvShow.setSeasonsNumber(String.valueOf(tvShow.getNumberOfSeasons()));
        return dbTvShow;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mCallbacks = this;
        setupDatabase();
        linkUi();
        setupAutocomplete();
        setupMovieList();
        setupListeners();
        setupToolbar();
    }

    @Override
    protected void onDestroy() {
        mDatabaseDAO.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().initLoader(DatabaseLoader.LOADER_ID, null, mCallbacks).forceLoad();
    }

    /**
     * Called when drop down suggestion list is clicked
     *
     * @param show The show
     */
    @Override
    public void onSuggestionClickListener(ApiResult show) {
        IMovieDbService movieDbService = RetrofitServiceFactory.createRetrofitService(IMovieDbService.class, getString(R.string.api_base_url));
        movieDbService
                .getTvShowById(String.valueOf(show.getId()), getString(R.string.api_key))
                .compose(bindUntilEvent(ActivityEvent.PAUSE))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof UnknownHostException) {
                        SnackBarUtils.snackError(MainActivity.this, mCoordinatorLayout, getString(R.string.error_no_internet_connection));
                    } else {
                        SnackBarUtils.snackError(MainActivity.this, mCoordinatorLayout, getString(R.string.api_server_error));
                    }

                    return Observable.empty();
                })
                .subscribe(tvShow -> {
                    mTvShow = tvShow;
                    mAddContainer.setVisibility(View.VISIBLE);

                    if (tvShow.getNumberOfEpisodes() != null && tvShow.getNumberOfEpisodes() == 0) {
                        mSeasonEditText.setHint(getString(R.string.no_seasons_available));
                    } else {
                        mSeasonEditText.setHint(getString(R.string.seasons_edit_text_hint, tvShow.getNumberOfSeasons()));
                    }
                    mSeasonEditText.setText("");
                    mSeasonEditText.requestFocus();
                });
        hideAutocompleteList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            mSearchView = (SearchView) searchItem.getActionView();
        }

        if (mSearchView != null) {
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            mSearchView.setIconified(false);
            mSearchView.setQueryHint(getString(R.string.search_view_hint));
            mSearchView.setMaxWidth(getResources().getDimensionPixelOffset(R.dimen.BU_22));
        }
        setListenerForSearchView();
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Setting a listener for search view
     */
    private void setListenerForSearchView() {
        IMovieDbService movieDbService = RetrofitServiceFactory.createRetrofitService(IMovieDbService.class, getString(R.string.api_base_url));
        RxSearchView.queryTextChanges(mSearchView)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .doOnEach(notification -> {
                    if (notification.getValue().toString().length() <= Constants.General.AUTOCOMPLETE_THRESHOLD) {

                        mAutocompleteAdapter.clear();
                        mAutoCompleteList.setVisibility(View.GONE);
                    }
                })
                .filter(textViewTextChangeEvent -> textViewTextChangeEvent.toString().length() > Constants.General.AUTOCOMPLETE_THRESHOLD)
                .debounce(Constants.Time.DEBOUNCE_TIME_INTERVAL, TimeUnit.MILLISECONDS)
                .map(textViewTextChangeEvent1 -> textViewTextChangeEvent1.toString().replace(" ", "%20"))// replace empty space with html code
                .flatMap(inputString ->
                        movieDbService
                                .getTvShowsByName(inputString, getString(R.string.api_key))
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .onErrorResumeNext(throwable -> {
                                    if (throwable instanceof UnknownHostException) {
                                        SnackBarUtils.snackError(MainActivity.this, mCoordinatorLayout, getString(R.string.error_no_internet_connection));
                                    } else {
                                        SnackBarUtils.snackError(MainActivity.this, mCoordinatorLayout, getString(R.string.api_server_error));
                                    }
                                    hideAutocompleteList();
                                    return Observable.empty();
                                }))
                .onErrorResumeNext(throwable1 -> Observable.empty())
                .subscribe(apiModel -> {
                    if (apiModel == null) {
                        return;
                    }

                    if (apiModel.getTotalResults() > 0) {
                        showAutocompleteList(apiModel.getApiResults());
                    } else {
                        SnackBarUtils.snackStandard(mCoordinatorLayout, getString(R.string.no_result));
                        hideAutocompleteList();
                    }
                });

        mSearchView.setOnCloseListener(() -> true);
    }

    /**
     * Show the autocomplete list
     *
     * @param data The data to be show on it
     */
    private void showAutocompleteList(List<ApiResult> data) {
        if (data.size() > 5) {
            mAutocompleteAdapter.addAll(data.subList(0, Constants.General.AUTOCOMPLETE_SUBLIST_END));
        } else {
            mAutocompleteAdapter.addAll(data);
        }

        mAutoCompleteList.setVisibility(View.VISIBLE);
        mAutoCompleteList.bringToFront();
    }

    /**
     * Clean and hide the autocomplete list
     */
    private void hideAutocompleteList() {
        mAutocompleteAdapter.clear();
        mAutoCompleteList.setVisibility(View.GONE);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_voice_control:
                handleVoiceControl();
                break;
            case R.id.action_share:
                handleShareClick();
                return true;
            case R.id.action_about:
                handleOnClickAbout();
                return true;
            case R.id.action_rate:
                handleOnClickRate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles clicks on rate button
     */
    private void handleOnClickRate() {
        Utils.openStore(this);
    }

    /**
     * Get's called when the user presses on voice control button
     */
    private void handleVoiceControl() {
        if(NetworkUtils.isNetworkAvailable(this)){
            promptSpeechInput();
        }else{
            SnackBarUtils.snackError(this, mCoordinatorLayout, getString(R.string.error_no_internet_connection));
        }
    }

    /**
     * Handles the click on about from toolbar
     */
    private void handleOnClickAbout() {
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, getString(R.string.about_us), Snackbar.LENGTH_LONG);
        snackbar.setAction(getString(R.string.go_to_site), v -> {
            if (NetworkUtils.isNetworkAvailable(MainActivity.this)) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.the_movie_db_link)));
                startActivity(browserIntent);
            } else {
                SnackBarUtils.snackError(MainActivity.this, mCoordinatorLayout, getString(R.string.error_no_internet_connection));
            }
        });
        snackbar.show();
    }

    /**
     * Handles click on share button
     */
    private void handleShareClick() {
        String timeDays = mDaysSpent.getText().toString();
        String timeHours = mHoursSpent.getText().toString();
        String timeMinutes = mMinutesSpent.getText().toString();
        String totalTimeSpent = getString(R.string.time_format, timeDays, timeHours, timeMinutes);

        String text = getString(R.string.share_time_spent_to_external_apps,
                totalTimeSpent, getMoviesTitles(), Utils.getAppLink(this));

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");

        startActivity(Intent.createChooser(sendIntent, getString(R.string.share_choose_title)));
    }

    /**
     * Returns the movies titles as string
     *
     * @return The list of titles
     */
    private String getMoviesTitles() {
        StringBuilder res = new StringBuilder("");
        int counter = 1;
        for (DbTvShow dbTvShow : mMoviesListAdapter.getData()) {
            int numberOfSeasons = Integer.parseInt(dbTvShow.getNumberOfSeasons());
            String seasonText;
            seasonText = (numberOfSeasons == 1) ? " season" : " seasons";

            res.append(counter)
                    .append(" ")
                    .append(dbTvShow.getName())
                    .append(" ")
                    .append(dbTvShow.getNumberOfSeasons())
                    .append(seasonText)
                    .append("\n");
            counter++;
        }
        return res.toString();
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
        /**listener for drag and drop*/
        ItemTouchHelper.Callback itemMoveCallbacks = new ItemTouchHelper.Callback() {
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Collections.swap(mMoviesListAdapter.getData(), viewHolder.getAdapterPosition(), target.getAdapterPosition());
                mMoviesListAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
                updateDatabase();
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

                final DbTvShow undoShow = mMoviesListAdapter.getData().get(position);
                mMoviesListAdapter.deleteItem(position);
                mDatabaseDAO.deleteTvShow(databaseId);

                Snackbar snackbar = Snackbar
                        .make(mCoordinatorLayout, "\n\n", Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.undo), v -> {
                            undo(undoShow, position);
                        });
                snackbar.show();


                setTime(TimeUtils.calculateTimeSpent(mMoviesListAdapter.getData()));

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
    private void undo(DbTvShow show, int position) {
        mDatabaseDAO.addTvShowItem(show.getName(), show.getNumberOfSeasons(), String.valueOf(show.getMinutesTotalTime()), show.getPosterUrl(), show.getPositionInList());
        mMoviesListAdapter.add(position, show);
        mMoviesList.smoothScrollToPosition(position);
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
    private void resetAdd() {
        mSeasonEditText.setText("");
        mAddContainer.setVisibility(View.GONE);
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
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAutoCompleteList.setLayoutManager(layoutManager);
        mAutocompleteAdapter = new AutocompleteAdapter(this);
        mAutoCompleteList.setAdapter(mAutocompleteAdapter);
    }

    /**
     * Link the ui
     */
    private void linkUi() {
        mMoviesList = (RecyclerView) findViewById(R.id.movies_list);
        mSeasonEditText = (EditText) findViewById(R.id.seasons_edit_text);
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
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
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

        if (mAutoCompleteList.getVisibility() == View.VISIBLE) {
            mAutoCompleteList.setVisibility(View.GONE);
            return;
        }

        if (this.mDoubleBackToExitPressedOnce) {
            finish();
            return;
        }

        mDoubleBackToExitPressedOnce = true;
        SnackBarUtils.snackStandard(mCoordinatorLayout, getString(R.string.toast_double_back_click));

        mDelayHandler.postDelayed(() -> MainActivity.this.mDoubleBackToExitPressedOnce = false, Constants.Time.ONE_SECOND);
    }

    @Override
    public void onItemClick(int position) {
        mMoviesList.smoothScrollToPosition(position);
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, Constants.Code.REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            SnackBarUtils.snackError(this, mCoordinatorLayout, getString(R.string.speech_not_supported));
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.Code.REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mSearchView.setQuery(result.get(0), false);
                }
                break;
            }

        }
    }

    /**
     * Loader listeners
     */
    @Override
    public Loader<List<DbTvShow>> onCreateLoader(int id, Bundle args) {
        return new DatabaseLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<DbTvShow>> loader, List<DbTvShow> data) {
        mMoviesListAdapter.addAll(data);
        setTime(TimeUtils.calculateTimeSpent(mMoviesListAdapter.getData()));
    }

    @Override
    public void onLoaderReset(Loader<List<DbTvShow>> loader) {
    }
}
