package com.clpstudio.tvshowtimespent.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clpstudio.tvshowtimespent.R;
import com.clpstudio.tvshowtimespent.datalayer.network.model.ApiResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AutocompleteAdapter extends RecyclerView.Adapter<AutocompleteAdapter.ViewHolder> {

    /**
     * The data
     */
    private ArrayList<ApiResult> mData;


    /**
     * Listener for on drop down list click
     */
    public interface OnDropDownListClick {

        /**
         * Called when a item on the list is clicked
         *
         * @param show The item clicked
         */
        void onSuggestionClickListener(ApiResult show);

    }

    /**
     * The listener for on click
     */
    private OnDropDownListClick mListener;

    /**
     * The constructor
     *
     * @param listener The listener for actions
     */
    public AutocompleteAdapter(OnDropDownListClick listener) {
        mData = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_autocomplete_list_row, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(mData.get(position).getName());
        holder.itemView.setOnClickListener(v -> mListener.onSuggestionClickListener(mData.get(position)));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addAll(List<ApiResult> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * Clear the data from the list
     */
    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    /**
     * The view holder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title)
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
