package com.davidretler.sudokusolver;

import android.util.Log;

/**
 * Created by david on 12/24/15.
 */
public class SolveThread extends Thread {

    public final Object lock = new Object();

    private static SudokuBoardActivity activity;
    private static SudokuBoard myBoard;

    private static class SolveRun implements Runnable {

        @Override
        public void run() {

            SudokuBoardActivity.solving = true;
            // turn of listeners while solving board, for performance
            Log.d("solveBoard()", "Turning off listeners");
            SudokuCell.ignoreListeners = true;

            Log.d("solveBoard()", "Solving board");
            myBoard.solve();
            if (myBoard.solved()) {
                // display the board... must be called through UI thread
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.displayBoard(myBoard);
                    }
                });
            } else {
                // display error that there is no solution
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Alerts.error("No Solution", "The current board has no valid solution.", activity);
                    }
                });
            }

            // turn the listeners back on
            Log.d("solveBoard()", "Turning on listeners");
            SudokuCell.ignoreListeners = false;
            SudokuBoardActivity.solving = false;
        }
    }

    public SolveThread(SudokuBoardActivity activity, SudokuBoard myBoard) {
        super(new SolveRun());
        SolveThread.activity = activity;
        SolveThread.myBoard = myBoard;
        SolveThread.myBoard.setSolveThread(this);

    }
}

