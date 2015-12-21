package com.davidretler.sudokusolver;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by david on 12/18/15.
 */
public class SudokuCell extends TextView {

    public SudokuCell(Context context, AttributeSet attrs) {
        super(context, attrs);

        // get the attributes
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SudokuCell,
                0,
                R.style.SudokuCell);

        a.recycle();

        this.addTextChangedListener(new CellListener());
        this.setOnFocusChangeListener(new FocusListener());
    }

    private class CellListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // do nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // do nothing
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().equals("0")) {
                s.clear();
                s.append(" ");
            }
            clearFocus();
            requestFocus();
        }
    }

    private class FocusListener implements OnFocusChangeListener {

        ColorStateList oldColors = getTextColors();
        Drawable oldBackground = getBackground();

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus) {
                setTextColor(getResources().getColor(R.color.colorAccent));
                setBackgroundColor(getResources().getColor(R.color.lighterGray));
            } else {
                setTextColor(oldColors);
                setBackground(oldBackground);
            }
        }
    }
}
