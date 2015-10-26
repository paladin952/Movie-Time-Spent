package com.clpstudio.movetimespent.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.clpstudio.movetimespent.R;
import com.clpstudio.movetimespent.model.TvShow;
import com.clpstudio.movetimespent.network.DatabaseSuggestionsRetriver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lclapa on 10/26/2015.
 */
public class AutocompleteAdapter extends BaseAdapter implements Filterable {

    /**
     * The filter
     */
    private Filter mResultFilter;
    /**
     * The data
     */
    private ArrayList<TvShow>mData;

    /**
     * Backend retriver
     */
    private DatabaseSuggestionsRetriver mDatabaseSuggestionsRetriver;

    /**
     * Layout inflater
     */
    private LayoutInflater inflater;

    /**
     * The context
     */
    private Context mContext;

    /**
     * Listener for on drop down list click
     */
    public interface OnDropDownListClick{

        void onSuggestionClick(TvShow show);

    }

    /**
     * The listener for on click
     */
    private OnDropDownListClick mListener;

    /**
     * The constructor
     * @param context The context
     */
    public AutocompleteAdapter(Context context, OnDropDownListClick listener) {
        mData = new ArrayList<>();
        mDatabaseSuggestionsRetriver = new DatabaseSuggestionsRetriver(context);
        inflater = LayoutInflater.from(context);
        mContext = context;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public TvShow getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_autocomplete_list_row, null);
            viewHolder.title = (TextView)convertView.findViewById(R.id.title);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.title.setText(mData.get(position).getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSuggestionClick(getItem(position));
            }
        });

        return convertView;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Filter getFilter() {
        if(mResultFilter == null){
            mResultFilter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    List<TvShow> results = mDatabaseSuggestionsRetriver.getTvShowsSuggestions(constraint.toString());
                    filterResults.values = results;
                    filterResults.count = results.size();
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (constraint == null) {
                        return;
                    }

                    if(results.count>0){
                        mData.clear();
                        mData.addAll((List<TvShow>) results.values);
                        notifyDataSetChanged();
                    }else{
                        Toast.makeText(mContext, mContext.getString(R.string.no_result), Toast.LENGTH_SHORT).show();
                    }
                }
            };

        }
        return mResultFilter;
    }

    public static class ViewHolder{
        public TextView title;
    }
}
