package com.sickfutre.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class SectionRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected BaseRecyclerAdapter<T, VH> linkedAdapter;
    protected Map<Integer, Integer> sectionPositions = new LinkedHashMap<>();
    protected Map<Integer, Integer> itemPositions = new LinkedHashMap<>();

    public SectionRecyclerAdapter(BaseRecyclerAdapter<T, VH> linkedAdapter) {
        this.linkedAdapter = linkedAdapter;
        linkedAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                updateSessionCache();
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                updateSessionCache();
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                updateSessionCache();
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                updateSessionCache();
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                updateSessionCache();
                notifyDataSetChanged();
            }
        });
    }

    public abstract int sectionCode(T item);

    protected synchronized void updateSessionCache() {
        int currentPosition = 0;
        sectionPositions.clear();
        itemPositions.clear();
        String currentSection = null;
        final int count = linkedAdapter.getItemCount();
        for (int i = 0; i < count; i++) {

            final T item = linkedAdapter.getItem(i);

            if (!isTheSame(currentSection, String.valueOf(sectionCode(item)))) {
                sectionPositions.put(currentPosition, sectionCode(item));
                currentSection = String.valueOf(sectionCode(item));
                currentPosition++;
            }
            itemPositions.put(currentPosition, i);
            currentPosition++;
        }
    }

    private boolean isTheSame(final String previousSection, final String newSection) {
        if (previousSection == null) {
            return newSection == null;
        } else {
            return previousSection.equals(newSection);
        }
    }

    public synchronized boolean isSection(final int position) {
        return sectionPositions.containsKey(position);
    }

    public synchronized int getSectionPosition(final int position) {
        return sectionPositions.get(position);
    }

    protected Integer getLinkedPosition(final int position) {
        return itemPositions.get(position);
    }

    @Override
    public long getItemId(final int position) {
        if (isSection(position)) {
            return sectionPositions.get(position).hashCode();
        } else {
            return linkedAdapter.getItemId(getLinkedPosition(position));
        }
    }

    @Override
    public int getItemViewType(final int position) {
        if (isSection(position)) {
            return sectionViewType();
        }
        return linkedAdapter.getItemViewType(getLinkedPosition(position));
    }

    protected abstract int sectionViewType();

    public abstract void setSectionValue(int sectionCode, final View sectionView);

    public abstract View createNewSectionView(LayoutInflater inflater, ViewGroup parent);

    public synchronized int getCount() {
        return sectionPositions.size() + itemPositions.size();
    }

    public synchronized T getItem(final int position) {
        // TODO: 01-Apr-16 return section item
        return isSection(position) ? null : linkedAdapter.getItem(getLinkedPosition(position));
    }

    @Override
    public int getItemCount() {
        return sectionPositions.size() + itemPositions.size();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewHolder(LayoutInflater.from(parent.getContext()), parent, viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBindItemViewHolder(holder, getItem(position), position, getItemViewType(position));
    }

    protected abstract void onBindItemViewHolder(VH viewHolder, T data, int position, int type);

    protected abstract VH viewHolder(LayoutInflater inflater, ViewGroup parent, int type);

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        linkedAdapter.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        linkedAdapter.unregisterAdapterDataObserver(observer);
    }
}
