package com.sickfutre.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private List<T> mOriginalValues = new ArrayList<T>();

    @Override public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewHolder(LayoutInflater.from(parent.getContext()), parent, viewType);
    }

    @Override public void onBindViewHolder(VH holder, int position) {
        onBindItemViewHolder(holder, getItem(position), position, getItemViewType(position));
    }

    private T getItem(int position) {
        return mOriginalValues.get(position);
    }

    @Override public int getItemCount() {
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

    protected abstract void onBindItemViewHolder(VH viewHolder, T data, int position, int type);

    protected abstract VH viewHolder(LayoutInflater inflater, ViewGroup parent, int type);

    public static abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        ViewMap views;

        public BaseViewHolder(View itemView) {
            super(itemView);
            views = new ViewMap(itemView);
            init(views);
        }

        public abstract void init(ViewMap views);

        @SuppressWarnings("unchecked")
        public <T extends View> T get(int viewId) {
            return (T) views.get(viewId);
        }

    }

}
