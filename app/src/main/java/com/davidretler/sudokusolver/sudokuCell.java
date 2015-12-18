package com.davidretler.sudokusolver;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by david on 12/18/15.
 */
public class sudokuCell extends TextView {

    public sudokuCell(Context context, AttributeSet attrs) {
        super(context, attrs);

        // get the attributes
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.sudokuCell,
                0,
                R.style.SudokuCell);

        a.recycle();
    }
}
