package com.sickfutre.android.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;

public class ViewMap extends SparseArray<View> {

    private View parent;

    public ViewMap(View parent) {
        this.parent = parent;
    }

    public void put(@IdRes int id) {
        put(id, parent.findViewById(id));
    }

    public void put(@NonNull int... ids) {
        for (int id : ids) {
            put(id);
        }
    }

    public View getView(@IdRes int id) {
        View view = get(id);
        if (view == null) {
            view = parent.findViewById(id);
        }
        return view;
    }
}
