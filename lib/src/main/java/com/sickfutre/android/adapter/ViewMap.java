package com.sickfutre.android.adapter;

import android.util.SparseArray;
import android.view.View;

public class ViewMap extends SparseArray<View> {

    private View parent;

    public ViewMap(View parent) {
        this.parent = parent;
    }

    public void put(int id) {
        put(id, parent.findViewById(id));
    }

    public View getView(int id) {
        View view = get(id);
        if(view == null) {
            view = parent.findViewById(id);
        }
        return view;
    }
}
