package com.clpstudio.tvshowtimespent.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.clpstudio.tvshowtimespent.R;
import com.clpstudio.tvshowtimespent.Utils.Constants;
import com.clpstudio.tvshowtimespent.Utils.UrlConstants;
import com.clpstudio.tvshowtimespent.model.TvShow;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lclapa on 10/26/2015.
 */
public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.ViewHolder> {

    /**
     * The context
     */
    private Context mContext;

    /**
     * The list of tv shows
     */
    private List<TvShow>mData;

    /**
     * Interface for when we delete an movie
     */
    public interface OndMovieEventListener {

        void onItemClick(int position);
    }

    /**
     * Listener
     */
    private OndMovieEventListener mListener;

    /**
     * The constructor
     * @param mContext The context
     */
    public MoviesListAdapter(Context mContext, OndMovieEventListener listener) {
        this.mContext = mContext;
        this.mData= new ArrayList<>();
        this.mListener = listener;
    }

    /**
     * Get the list of shows
     * @return List of Shows
     */
    public  List<TvShow> getData(){
        return mData;
    }

    /**
     * Add a new show in the list
     * @param show The show to be added
     */
    public void add(TvShow show){
        mData.add(show);
        notifyDataSetChanged();
    }

    /**
     * add new data as a list of items
     * @param data The list of data
     */
    public void addAll(List<TvShow> data){
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movies_list_row, parent, false);
        final MoviesListAdapter.ViewHolder viewHolder = new MoviesListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TvShow show = mData.get(position);

        //download image after size was measured for Picasso center crop size
        ViewTreeObserver vto = holder.image.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                holder.image.getViewTreeObserver().removeOnPreDrawListener(this);
                int finalHeight = holder.image.getMeasuredHeight();
                int finalWidth = holder.image.getMeasuredWidth();

                Picasso.with(mContext)
                        .load(UrlConstants.BASE_IMAGE_URL + show.getPosterUrl() + UrlConstants.API_KEY_QUESTION)
                        .resize(finalWidth, finalHeight)
                        .centerCrop()
                        .error(R.drawable.ic_no_image)
                        .into(holder.image);

                return true;
            }
        });


        holder.title.setText(show.getName());
        holder.season.setText(show.getNumberOfSeasons() + " seasons");
    }

    /**
     * Remove element from list
     * @param position The position
     */
    public void deleteItem(int position){
        mData.remove(position);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }



    /**
     * Reset the position to be stored in database as reference to
     * item position in list
     */
    public void reorderPosition(){
        for(int i =0; i < mData.size(); i++){
            mData.get(i).setPositionInList(i);
        }
    }

    /**
     * MoviesListViewHolder required by the RecycleViewAdapter.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * The title of the show
         */
        private TextView title;

        /**
         * The number of season to be calculated
         */
        private TextView season;

        /**
         * The background image
         */
        private ImageView image;

        /**
         * The view to be removed on swipe
         */
        private View removableView;

        /**
         * The view behind image
         */
        private View behindView;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            season = (TextView)itemView.findViewById(R.id.seasons);
            image = (ImageView)itemView.findViewById(R.id.image);
            removableView = itemView.findViewById(R.id.removable);
            behindView = itemView.findViewById(R.id.behind_view);
        }

        public View getRemovableView(){
            return removableView;
        }
    }
}
