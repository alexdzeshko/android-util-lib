package com.sickfutre.android.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.sickfutre.android.util.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class BaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>
        implements Filterable {

    private List<T> mOriginalValues;

    private Comparator<? super T> mSortComparator;
    private Function<T, Boolean> mFilter;
    /**
     * Lock used to modify the content of . Any write operation
     * performed on the array should be synchronized on this lock. This lock is also
     * used by the filter (see {@link #getFilter()} to make a synchronized copy of
     * the original array of data.
     */
    private final Object mLock = new Object();

    public BaseRecyclerAdapter() {
        mOriginalValues = new ArrayList<T>();
    }

    public BaseRecyclerAdapter(@NonNull List<T> values) {
        this();
        addAll(values);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewHolder(LayoutInflater.from(parent.getContext()), parent, viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBindItemViewHolder(holder, getItem(position), position, getItemViewType(position));
    }

    private T getItem(int position) {
        return mOriginalValues.get(position);
    }

    @Override
    public int getItemCount() {
        return mOriginalValues.size();
    }

    public void add(int position, T item) {
        mOriginalValues.add(position, item);
        notifyItemInserted(position);
        int itemCount = mOriginalValues.size() - position;
        notifyItemRangeChanged(position, itemCount);
    }

    public void add(T item) {
        mOriginalValues.add(item);
        notifyItemInserted(mOriginalValues.size() - 1);
    }

    public void addAll(List<? extends T> items) {
        final int size = this.mOriginalValues.size();
        this.mOriginalValues.addAll(items);
        notifyItemRangeInserted(size, items.size());
    }

    public void setItems(List<? extends T> items) {
        mOriginalValues.clear();
        mOriginalValues.addAll(items);
        notifyDataSetChanged();
    }

    public void set(int position, T item) {
        mOriginalValues.set(position, item);
        int itemCount = mOriginalValues.size() - position;
        notifyItemRangeChanged(position, itemCount);
    }

    public void removeChild(int position) {
        mOriginalValues.remove(position);
        notifyItemRemoved(position);
        int itemCount = mOriginalValues.size() - position;
        notifyItemRangeChanged(position, itemCount);
    }

    public void clear() {
        final int size = mOriginalValues.size();
        mOriginalValues.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void moveChildTo(int fromPosition, int toPosition) {
        if (toPosition != -1 && toPosition < mOriginalValues.size()) {
            final T item = mOriginalValues.remove(fromPosition);
            mOriginalValues.add(toPosition, item);
            notifyItemMoved(fromPosition, toPosition);
            int positionStart = fromPosition < toPosition ? fromPosition : toPosition;
            int itemCount = Math.abs(fromPosition - toPosition) + 1;
            notifyItemRangeChanged(positionStart, itemCount);
        }
    }

    public void sort(Comparator<? super T> comparator) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.sort(mOriginalValues, comparator);
                notifyDataSetChanged();
            }
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

                setItems(results.values instanceof List ? (List<T>) results.values : null);

            }
        };
    }

    public void setFilter(Function<T, Boolean> filter) {
        mFilter = filter;

        applyFilter();
    }

    private void applyFilter() {
        if (mFilter == null) {
            setItems(mOriginalValues);
        } else {
            getFilter().filter("");
        }

    }

    protected abstract void onBindItemViewHolder(VH viewHolder, T data, int position, int type);

    protected abstract VH viewHolder(LayoutInflater inflater, ViewGroup parent, int type);

    public static abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        ViewMap views;

        public BaseViewHolder(View itemView) {
            super(itemView);
            views = new ViewMap(itemView);
            putViewsIntoMap(views);
        }

        public abstract void putViewsIntoMap(ViewMap views);

        @SuppressWarnings("unchecked")
        public <T extends View> T get(int viewId) {
            return (T) views.get(viewId);
        }

    }

}
