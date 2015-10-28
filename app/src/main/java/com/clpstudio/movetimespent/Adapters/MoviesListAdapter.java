package com.clpstudio.movetimespent.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
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
     * Interface for when we delete an movie
     */
    public interface OnDeletedMovie{
        void onDeleteMovie();
    }

    /**
     * Listener
     */
    private OnDeletedMovie mListener;

    /**
     * The constructor
     * @param mContext The context
     */
    public MoviesListAdapter(Context mContext, OnDeletedMovie listener) {
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mData.get(position).isShownRemove()){
                    mData.get(position).setIsShownRemove(false);
                    holder.remove.setVisibility(View.GONE);
                }else{
                    setRemoveVisibility(position);
                }
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(position);
                notifyDataSetChanged();
                mListener.onDeleteMovie();
            }
        });

        if(show.isShownRemove()){
            holder.remove.setVisibility(View.VISIBLE);
        }else{
            holder.remove.setVisibility(View.GONE);
        }

        holder.title.setText(show.getName());
        holder.season.setText(show.getNumberOfSeasons() + " seasons");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * set visibility of the remove button
     * @param position Position
     */
    private void setRemoveVisibility(int position){
        for(int i =0; i < mData.size(); i++){
            mData.get(i).setIsShownRemove(false);
        }
        mData.get(position).setIsShownRemove(true);
        notifyDataSetChanged();
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
         * The remove button
         */
        private ImageButton remove;


        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            season = (TextView)itemView.findViewById(R.id.seasons);
            image = (ImageView)itemView.findViewById(R.id.image);
            remove = (ImageButton)itemView.findViewById(R.id.remove_image);
        }
    }
}
