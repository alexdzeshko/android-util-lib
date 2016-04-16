package com.sickfutre.android.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Alex Dzeshko on 04-Feb-16.
 */
public class AppDialog extends Dialog implements View.OnClickListener {

    View mView;
    private OnClickListener mPositiveListener;
    private OnClickListener mNeutralListener;
    private OnClickListener mNegativeListener;

    AppDialog(Context context, View view) {
        this(context);
        mView = view;
    }

    private AppDialog(Context context) {
        this(context, true, null);
    }

    private AppDialog(Context context, int themeResId) {
        this(context, true, null);
    }

    protected AppDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.button1: {
                if (mPositiveListener != null) {
                    mPositiveListener.onClick(this, BUTTON_POSITIVE);
                }
                break;
            }
            case android.R.id.button2: {
                if (mNeutralListener != null) {
                    mNeutralListener.onClick(this, BUTTON_NEUTRAL);
                }
                break;
            }
            case android.R.id.button3: {
                if (mNegativeListener != null) {
                    mNegativeListener.onClick(this, BUTTON_NEGATIVE);
                }
                break;
            }
        }
    }

    public static class Builder {

        private AppDialog dialog;

        private final View dialogView;

        public Builder(Context context, @LayoutRes int layoutId) {
            dialogView = View.inflate(context, layoutId, null);
            dialog = new AppDialog(context, dialogView);
        }

        public Builder setTitle(String title) {
            TextView view = (TextView) dialogView.findViewById(android.R.id.title);
            view.setVisibility(View.VISIBLE);
            view.setText(title);
            return this;
        }

        public Builder setMessage(String message) {
            TextView view = (TextView) dialogView.findViewById(android.R.id.message);
            view.setVisibility(View.VISIBLE);
            view.setText(message);
            return this;
        }

        public Builder setPositiveButton(String text, DialogInterface.OnClickListener listener) {
            bindButton(text, android.R.id.button1);
            dialog.mPositiveListener = listener;
            return this;
        }

        public Builder setNeutralButton(String text, DialogInterface.OnClickListener listener) {
            bindButton(text, android.R.id.button2);
            dialog.mNeutralListener = listener;
            return this;
        }

        public Builder setNegativeButton(String text, DialogInterface.OnClickListener listener) {
            bindButton(text, android.R.id.button3);
            dialog.mNegativeListener = listener;
            return this;
        }

        private void bindButton(String text, int id) {
            TextView view = (TextView) dialogView.findViewById(id);
            ((View) view.getParent()).setVisibility(View.VISIBLE);
            view.setText(text);
            view.setOnClickListener(dialog);
        }

        public Dialog build() {
            return dialog;
        }

    }
}
