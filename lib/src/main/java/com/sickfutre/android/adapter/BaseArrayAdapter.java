package com.sickfutre.android.adapter;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.sickfutre.android.util.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Base adapter used across app.
 *
 * @param <T> item type
 */
public abstract class BaseArrayAdapter<T> extends ArrayAdapter<T> implements IBaseAdapter<T> {

    public interface OnItemClickedListener<T> {
        void onItemClicked(View view, T model, int position);
    }

    private Comparator<? super T> mSortComparator;
    private Function<T, Boolean> mFilter;
    /**
     * Lock used to modify the content of . Any write operation
     * performed on the array should be synchronized on this lock. This lock is also
     * used by the filter (see {@link #getFilter()} to make a synchronized copy of
     * the original array of data.
     */
    private final Object mLock = new Object();

    private List<T> mOriginalValues = new ArrayList<T>();
    private OnItemClickedListener<T> mOnItemClickedListener;

    public BaseArrayAdapter(Context context) {
        super(context, 0);
    }

    public BaseArrayAdapter(Context context, List<T> items) {
        this(context);
        setDropDownViewResource(getDropDownLayoutRes());
        setItems(items);
    }

    protected int getDropDownLayoutRes() {
        return 0;
    }

    /* (non-Javadoc)
     * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        T model = getItem(position);

        View view = convertView;
        if (view == null) {
            view = newView(getContext(), model, parent, position);
        }
        bindView(view, model, position);
        return view;
    }

    public abstract View newView(Context context, T data, ViewGroup parent, int position);

    public abstract void bindView(View view, T data, int position);

    /**
     * Sets the list of data mItems of type .
     * <p/>
     * NOTE: mItems are sorted if sortBy field was specified before.
     * NOTE: Change notifications fired at end.
     * <p/>
     * {@link #setSortBy(Comparator<? super T>)}
     *
     * @param rawList the new mItems
     */
    @Override
    public void setItems(List<T> rawList) {
        mOriginalValues.clear();
        mOriginalValues.addAll(rawList);

        if (mSortComparator != null) {
            Collections.sort(mOriginalValues, mSortComparator);
        }

        applyFilter();

    }

    protected List<T> getItems() {
        return mOriginalValues;
    }

    protected void setItemsInternal(List<T> rawList) {
        setNotifyOnChange(false);

        clear();

        if (rawList != null) {
            if (Build.VERSION.SDK_INT >= 11)
                addAll(rawList);
            else {
                for (T t : rawList) {
                    add(t);
                }
            }
        }

        notifyDataSetChanged();

    }

    /**
     * Sets the sort by field.
     *
     * @param c the new sort by field
     */
    public void setSortBy(Comparator<? super T> c) {
        this.mSortComparator = c;
        // calculate direction
        if (mSortComparator != null) {
            Collections.sort(mOriginalValues, mSortComparator);
            sort(mSortComparator);
        } else {
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                FilterResults results = new FilterResults();
                ArrayList<T> newValues;
                if (mFilter == null) {

                    synchronized (mLock) {
                        newValues = new ArrayList<T>(mOriginalValues);
                    }

                } else {

                    ArrayList<T> values;
                    synchronized (mLock) {
                        values = new ArrayList<T>(mOriginalValues);
                    }

                    final int count = values.size();
                    newValues = new ArrayList<T>();

                    for (int i = 0; i < count; i++) {
                        final T value = values.get(i);
                        if (mFilter.apply(value)) {
                            newValues.add(value);
                        }
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                setItemsInternal(results.values instanceof List ? (List<T>) results.values : null);

            }
        };
    }

    public void setFilter(Function<T, Boolean> filter) {
        mFilter = filter;

        applyFilter();
    }

    private void applyFilter() {
        if (mFilter == null) {
            setItemsInternal(mOriginalValues);
        } else {
            getFilter().filter("");
        }

    }

    public OnItemClickedListener<T> getOnItemClickedListener() {
        return mOnItemClickedListener;
    }

    public void setOnItemClickedListener(OnItemClickedListener<T> onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
