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

    protected abstract void onFiltered(List<T> values);

    @Override
    protected FilterResults performFiltering(final CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        if (TextUtils.isEmpty(constraint)) {
            filterResults.values = objects;
        } else {
            List<T> results = new ArrayList<>();
            for (T t : objects) {
                if (acceptable(t, constraint)) {
                    results.add(t);
                }
            }
            Comparator<? super T> comparator = getComparator();
            if (comparator != null) {
                Collections.sort(results, comparator);
            }
            filterResults.values = results;
        }
        return filterResults;
    }

    protected abstract Comparator<? super T> getComparator();

    protected abstract boolean acceptable(T t, CharSequence constraint);

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        onFiltered((List<T>) results.values);
    }
}
