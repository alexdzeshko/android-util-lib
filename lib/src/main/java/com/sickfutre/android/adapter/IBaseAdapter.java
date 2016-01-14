package com.sickfutre.android.adapter;

import android.widget.ListAdapter;

import java.util.List;

public interface IBaseAdapter<T> extends ListAdapter{

    public void setItems(List<T> rawList);
}
