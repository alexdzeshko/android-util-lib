package com.sickfutre.android.util;

import android.text.TextUtils;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class ObjectFilter<T> extends Filter {
    private List<T> objects;

    public ObjectFilter(List<T> objects) {
        this.objects = objects;
    }

    protected abstract String getObjectStringToSearchIn(T object);

    protected abstract void onFiltered(List<T> values);

    @Override
    protected FilterResults performFiltering(final CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        if(TextUtils.isEmpty(constraint)) {
            filterResults.values = objects;
        } else {
            List<T> results = new ArrayList<>();
            final String formattedQuery = constraint.toString().toLowerCase();
            for (T t : objects) {
                if (getObjectStringToSearchIn(t).toLowerCase().contains(formattedQuery)) {
                    results.add(t);
                }
            }
            Collections.sort(results, new Comparator<T>() {
                @Override
                public int compare(T lhs, T rhs) {
                    Integer lhsVal = getObjectStringToSearchIn(lhs).toLowerCase().indexOf(formattedQuery);
                    int rhsVal = getObjectStringToSearchIn(rhs).toLowerCase().indexOf(formattedQuery);
                    return lhsVal.compareTo(rhsVal);
                }
            });
            filterResults.values = results;
        }
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        onFiltered((List<T>) results.values);
    }
}
