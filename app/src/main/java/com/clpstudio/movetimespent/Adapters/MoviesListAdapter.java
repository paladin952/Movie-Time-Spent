package com.clpstudio.movetimespent.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clpstudio.movetimespent.R;
import com.clpstudio.movetimespent.Utils.UrlConstants;
import com.clpstudio.movetimespent.model.TvShow;
import com.squareup.picasso.Picasso;

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
     * The constructor
     * @param mContext The context
     */
    public MoviesListAdapter(Context mContext) {
        this.mContext = mContext;
        this.mData= new ArrayList<>();
    }

    /**
     * Add a new show in the list
     * @param show The show to be added
     */
    public void add(TvShow show){
        mData.add(show);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movies_list_row, parent, false);
        final MoviesListAdapter.ViewHolder viewHolder = new MoviesListAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TvShow show = mData.get(position);
        Picasso.with(mContext).load(UrlConstants.BASE_IMAGE_URL + show.getPosterUrl() + UrlConstants.API_KEY_QUESTION).into(holder.image);
        holder.title.setText(show.getName());
        holder.season.setText(show.getSeason());
    }

    @Override
    public int getItemCount() {
        return mData.size();
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

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            season = (TextView)itemView.findViewById(R.id.seasons);
            image = (ImageView)itemView.findViewById(R.id.image);
        }
    }
}
