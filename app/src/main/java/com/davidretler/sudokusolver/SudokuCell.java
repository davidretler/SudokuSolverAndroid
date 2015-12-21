package com.davidretler.sudokusolver;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by david on 12/18/15.
 */
public class SudokuCell extends EditText {

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
    }

    private class CellListener implements TextWatcher {

        String oldText;
        String newText;

        /**
         * This method is called to notify you that, within <code>s</code>,
         * the <code>count</code> characters beginning at <code>start</code>
         * are about to be replaced by new text with length <code>after</code>.
         * It is an error to attempt to make changes to <code>s</code> from
         * this callback.
         *
         * @param s
         * @param start
         * @param count
         * @param after
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            oldText = s.toString();
            Log.d("Cell Edit", "Changing from " + oldText);
        }

        /**
         * This method is called to notify you that, within <code>s</code>,
         * the <code>count</code> characters beginning at <code>start</code>
         * have just replaced old text that had length <code>before</code>.
         * It is an error to attempt to make changes to <code>s</code> from
         * this callback.
         *
         * @param s
         * @param start
         * @param before
         * @param count
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            newText = s.toString();
            Log.d("Cell Edit", "Changing to " + newText);
        }

        /**
         * This method is called to notify you that, somewhere within
         * <code>s</code>, the text has been changed.
         * It is legitimate to make further changes to <code>s</code> from
         * this callback, but be careful not to get yourself into an infinite
         * loop, because any changes you make will cause this method to be
         * called again recursively.
         * (You are not told where the change took place because other
         * afterTextChanged() methods may already have made other changes
         * and invalidated the offsets.  But if you need to know here,
         * you can use {@link Spannable#setSpan} in {@link #onTextChanged}
         * to mark your place and then look up from here where the span
         * ended up.
         *
         * @param s
         */
        @Override
        public void afterTextChanged(Editable s) {
            if(newText.equals(oldText)) {
              return;
            } else if(newText.length() > 1) {
                s.clear();
                s.append(oldText);
                Log.d("Cell Edit", "Too long, reverting");
            } else {
                Log.d("Cell Edit", "New length fine, keeping");
            }
        }
    }


}
