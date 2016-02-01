package com.sickfutre.android.adapter;

import android.widget.ListAdapter;

import java.util.List;

public interface IBaseAdapter<T> extends ListAdapter {

    void setItems(List<T> rawList);
}
