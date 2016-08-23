package com.sickfutre.android.adapter;

import java.util.ArrayList;
import java.util.List;

public class SectionDataHolder<T> {
    private int code;
    boolean isExpanded;
    List<T> data = new ArrayList<>();
    int sectionPosition;
    List<Integer> itemsPositions = new ArrayList<>();

    void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public List<T> getData() {
        return data;
    }

    @Override
    public String toString() {
        return code + " items ["+data.size()+"] isExp: "+isExpanded;
    }
}
