package com.shoppursshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shoppursshop.models.Market;

import java.util.ArrayList;
import java.util.List;

public class MarketAdapter extends ArrayAdapter<Market> {

    Context context;
    int resource, textViewResourceId;
    List<Market> items, tempItems, suggestions;

    public MarketAdapter(@NonNull Context context, int resource,int textViewResourceId, List<Market> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<Market>(items); // this makes the difference.
        suggestions = new ArrayList<Market>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);
        }
        Market market = items.get(position);
        if (market != null) {
            TextView lblName = (TextView) view.findViewById(textViewResourceId);
            if (lblName != null)
                lblName.setText(market.getMrkName());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Market) resultValue).getMrkName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Market market : tempItems) {
                    if (market.getMrkName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(market);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Market> filterList = (ArrayList<Market>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Market market : filterList) {
                    add(market);
                    notifyDataSetChanged();
                }
            }
        }
    };

}
