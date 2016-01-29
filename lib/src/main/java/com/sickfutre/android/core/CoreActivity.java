package com.sickfutre.android.core;

import android.annotation.SuppressLint;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CoreActivity extends AppCompatActivity {

    private List<OnBackPressListener> mOnBackPressListeners = new ArrayList<>();

    @SuppressLint("CommitTransaction")
    public void addFragment(@IdRes int containerId, @NonNull Fragment fragment, boolean addToBackStack) {
        String name = fragment.getClass().getName();
        FragmentTransaction add = getSupportFragmentManager().beginTransaction().add(containerId, fragment, name);
        if (addToBackStack) {
            add.addToBackStack(name);
        }
        add.commit();
    }

    public void replaceFragment(@IdRes int containerId, @NonNull Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment, fragment.getClass().getName())
                .commit();
    }

    public void removeFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    public void removeFragment(@IdRes int id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(id);
        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
    }

    public void removeFragment(@NonNull String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
    }

    public void removeFragment(Class<? extends Fragment> fragmentClass) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentClass.getName());
        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
    }

    @Override public void onBackPressed() {
        if (interruptedByListener()) {
            //noinspection UnnecessaryReturnStatement
            return;
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private boolean interruptedByListener() {
        boolean interrupt = false;
        for (OnBackPressListener listener : mOnBackPressListeners) {
            if(listener.onBackPressed()){
                interrupt = true;
            }
        }
        return interrupt;
    }

    public void addOnBackPressListener(OnBackPressListener listener) {
        if (listener != null) {
            mOnBackPressListeners.add(listener);
        }
    }

    public void removeOnBackPressListener(OnBackPressListener listener) {
        mOnBackPressListeners.remove(listener);
    }

    public interface OnBackPressListener {
        boolean onBackPressed();
    }

    @SuppressWarnings("unchecked")
    public <V> V findById(@IdRes int id) {
        return (V)  findViewById(id);
    }
}
