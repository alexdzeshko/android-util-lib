package com.sickfutre.android.adapter;

import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.sickfutre.android.util.Function;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Adapter for sections.
 */
public abstract class SectionListAdapter<T> extends BaseAdapter implements AdapterView.OnItemClickListener, IBaseAdapter<T> {

    protected final BaseArrayAdapter<T> linkedAdapter;
    protected final Map<Integer, Integer> sectionPositions = new LinkedHashMap<>();
    protected final Map<Integer, Integer> itemPositions = new LinkedHashMap<>();

    private int viewTypeCount;
    protected final LayoutInflater inflater;

//    private View transparentSectionView;

    private AdapterView.OnItemClickListener linkedListener;

    public SectionListAdapter(final LayoutInflater inflater, final BaseArrayAdapter<T> linkedAdapter) {
        this.linkedAdapter = linkedAdapter;
        this.inflater = inflater;
        DataSetObserver dataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                updateSessionCache();
                notifyDataSetChanged();
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
                updateSessionCache();
                notifyDataSetInvalidated();
            }

        };
        linkedAdapter.registerDataSetObserver(dataSetObserver);
        updateSessionCache();
    }

    public SectionListAdapter(@NonNull BaseArrayAdapter<T> linkedAdapter) {
        this(LayoutInflater.from(linkedAdapter.getContext()), linkedAdapter);
    }

    private boolean isTheSame(final String previousSection, final String newSection) {
        if (previousSection == null) {
            return newSection == null;
        } else {
            return previousSection.equals(newSection);
        }
    }

    protected synchronized void updateSessionCache() {
        int currentPosition = 0;
        sectionPositions.clear();
        itemPositions.clear();
        viewTypeCount = linkedAdapter.getViewTypeCount() + 1;
        String currentSection = null;
        final int count = linkedAdapter.getCount();
        for (int i = 0; i < count; i++) {

            final T item = (T) linkedAdapter.getItem(i);

            if (!isTheSame(currentSection, String.valueOf(sectionCode(item)))) {
                sectionPositions.put(currentPosition, sectionCode(item));
                currentSection = String.valueOf(sectionCode(item));
                currentPosition++;
            }
            itemPositions.put(currentPosition, i);
            currentPosition++;
        }
    }

    public abstract int sectionCode(T item);

    @Override
    public synchronized int getCount() {
        return sectionPositions.size() + itemPositions.size();
    }

    @Override
    public synchronized Object getItem(final int position) {
//        if (isSection(position)) {
//            return sectionPositions.get(position);
//        } else {
//            final int linkedItemPosition = getLinkedPosition(position);
//            return linkedAdapter.getItem(linkedItemPosition);
//        }
        return isSection(position)? null : linkedAdapter.getItem(getLinkedPosition(position));
    }

    public synchronized boolean isSection(final int position) {
        return sectionPositions.containsKey(position);
    }

    public synchronized int getSectionPosition(final int position) {
        return sectionPositions.get(position);
    }

    @Override
    public long getItemId(final int position) {
        if (isSection(position)) {
            return sectionPositions.get(position).hashCode();
        } else {
            return linkedAdapter.getItemId(getLinkedPosition(position));
        }
    }

    protected Integer getLinkedPosition(final int position) {
        return itemPositions.get(position);
    }

    @Override
    public int getItemViewType(final int position) {
        if (isSection(position)) {
            return viewTypeCount - 1;
        }
        return linkedAdapter.getItemViewType(getLinkedPosition(position));
    }

    private View getSectionView(View convertView, final int sectionCode, ViewGroup parent) {
        View theView = convertView;

        if (theView == null || theView.getHeight() == 0) {
            theView = createNewSectionView(inflater, parent);
        }
        setSectionValue(sectionCode, theView);
        //replaceSectionViewsInMaps(sectionCode, theView);
        return theView;
    }

    public abstract void setSectionValue(int sectionCode, final View sectionView);

    public abstract View createNewSectionView(LayoutInflater inflater, ViewGroup parent);

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (isSection(position)) {
            return getSectionView(convertView, sectionPositions.get(position), parent);
        }
        if (linkedAdapter.getCount() > 0) {
            return linkedAdapter.getView(getLinkedPosition(position), convertView, parent);
        }
        return getSectionView(convertView, -1, parent);//todo take attention
    }

    @Override
    public int getViewTypeCount() {
        return viewTypeCount;
    }

    @Override
    public boolean hasStableIds() {
        return linkedAdapter.hasStableIds();
    }

    @Override
    public boolean isEmpty() {
        return linkedAdapter.isEmpty();
    }

    @Override
    public void registerDataSetObserver(final DataSetObserver observer) {
        linkedAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(final DataSetObserver observer) {
        linkedAdapter.unregisterDataSetObserver(observer);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return linkedAdapter.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(final int position) {
        return !isSection(position) && linkedAdapter.isEnabled(getLinkedPosition(position));
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {

        if (isSection(position)) {
            sectionClicked(parent, view, getSectionPosition(position), id);
        } else if (linkedListener != null) {
            linkedListener.onItemClick(parent, view, getLinkedPosition(position), id);
        }
    }

    private void sectionClicked(AdapterView<?> parent, View view, Integer position, long id) {
        //NOOP
    }

    public void setOnItemClickListener(final AdapterView.OnItemClickListener linkedListener) {
        this.linkedListener = linkedListener;
    }

    @Override public void setItems(List<T> rawList) {

        linkedAdapter.setItems(rawList);
    }

    public void setFilter(Function<T, Boolean> filter) {
        linkedAdapter.setFilter(filter);
    }


    //unused in current implementation
//    private final Map<View, Integer> currentViewSections = new HashMap<View, Integer>();

//    protected synchronized void replaceSectionViewsInMaps(final int section, final View theView) {
//        if (currentViewSections.containsKey(theView)) {
//            currentViewSections.remove(theView);
//        }
//        currentViewSections.put(theView, section);
//    }

//    public void makeSectionInvisibleIfFirstInList(final int firstVisibleItem) {
//        final String section = getSectionName(firstVisibleItem);
//        // only make invisible the first section with that name in case there
//        // are more with the same name
//        boolean alreadySetFirstSectionInvisible = false;
//        for (final Map.Entry<View, Integer> itemView : currentViewSections
//                .entrySet()) {
//            if (itemView.getValue().equals(section)
//                    && !alreadySetFirstSectionInvisible) {
//                itemView.getKey().setVisibility(View.INVISIBLE);
//                alreadySetFirstSectionInvisible = true;
//            } else {
//                itemView.getKey().setVisibility(View.VISIBLE);
//            }
//        }
//        for (final Map.Entry<Integer, Integer> entry : sectionPositions.entrySet()) {
//            if (entry.getKey() > firstVisibleItem + 1) {
//                break;
//            }
//            setSectionValue(LandmarkFragment.Section.getByCode(entry.getValue()), getTransparentSectionView());
//        }
//    }
//
//    public synchronized View getTransparentSectionView() {
//        if (transparentSectionView == null) {
//            transparentSectionView = createNewSectionView();
//        }
//        return transparentSectionView;
//    }

}
