package com.davidretler.sudokusolver;

import android.content.Context;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

public class SudokuBoardActivity extends AppCompatActivity {

    // whether or not we solve the board step-by-step
    static boolean step = false;

    // the time (in ms) to wait between steps if solving step by step
    static double stepTime = 500;

    // are we currently solving the board?
    static boolean solving = false;
    // is the solver paused?
    static boolean paused = false;

    private SolveThread solveThread = null;

    // board to share between threads
    public volatile SudokuBoard theBoard = null;

    private Menu myMenu = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // start the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_board);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // crate the memu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        myMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle menu item selections
        switch (item.getItemId()) {
            case R.id.step_by_step:
                // checkbox for step-by-step
                if(item.isChecked()) {
                    // uncheck if checked
                    item.setChecked(false);
                    step = false;
                } else {
                    // check if unchecked
                    item.setChecked(true);
                    step = true;
                }
                return true;

            case R.id.step_time:
                Alerts.timeDialog(this);
                return true;

            case R.id.playPause:
                playPause(item);
                return true;

            default:
                return false;
        }
    }

    // solve the board
    public void solveBoard(View view) {

        if(!solving) {
            Log.d("solveBoard()", "This will solve the board");

            SudokuBoard myBoard = parseBoard(view);
            this.theBoard = myBoard;
            myBoard.setActivity(this);

            // background thread to solve the board
            solveThread = new SolveThread(this, theBoard);
            solveThread.start();
        } else if (paused) {
            playPause(myMenu.findItem(R.id.playPause));
        }
    }

    // parse the current state of the board and return a sudokuboard object with that state
    private SudokuBoard parseBoard(View view) {

        Log.d("solveBoard()", "Turning off listeners");
        SudokuCell.ignoreListeners = true;

        int[][] boardArray = new int[9][9];

        for(int gridRow = 1; gridRow <= 3; gridRow++) {
            for(int gridCol = 1; gridCol <= 3; gridCol++) {
                for(int cellRow = 1; cellRow <= 3; cellRow++) {
                    for(int cellCol = 1; cellCol <= 3; cellCol++) {

                        try {

                            SudokuCell cell = getCell(gridRow, gridCol, cellRow, cellCol);
                            Log.d("parseBoard()", "Got the cell");
                            Log.d("parseBoard()", "Value of the current cell is " + cell.getText().toString());

                            int cellValue;
                            if(!cell.getText().toString().equals(" ") && !cell.getText().toString().equals("")) {
                                cellValue = Integer.parseInt(cell.getText().toString());
                            } else {
                                cellValue = 0;
                            }

                            int boardIndexRow = (gridRow-1)*3 + cellRow - 1;
                            int boardIndexCol = (gridCol-1)*3 + cellCol - 1;
                            boardArray[boardIndexRow][boardIndexCol] = cellValue;

                        } catch(Exception ex) {
                            Log.e("parseBoard()", "Failed loading board at " + gridRow + " " + gridCol + " " + cellRow + " " + cellCol);
                            Alerts.error("Error", "There was an error parsing the board.", view.getContext());
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }

        if(!solving) {
            Log.d("solveBoard()", "Turning on listeners");
            SudokuCell.ignoreListeners = false;
        }

        return new SudokuBoard(boardArray);
    }

    // display the board
    void displayBoard(SudokuBoard board) {

        Log.d("displayBoard()", board.toString());

        Log.d("displayBoard()", "Turning off listeners");
        SudokuCell.ignoreListeners = true;

        for(int gridRow = 1; gridRow <= 3; gridRow++) {
            for(int gridCol = 1; gridCol <= 3; gridCol++) {
                for(int cellRow = 1; cellRow <= 3; cellRow++) {
                    for(int cellCol = 1; cellCol <= 3; cellCol++) {

                        SudokuCell cell = getCell(gridRow, gridCol, cellRow, cellCol);
                        int boardIndexRow = (gridRow-1)*3 + cellRow - 1;
                        int boardIndexCol = (gridCol-1)*3 + cellCol - 1;
                        int cellValue = board.getNum(boardIndexRow, boardIndexCol);
                        cell.setText("" + (cellValue != 0 ? cellValue : " "));
                    }
                }
            }
        }

        if(!solving) {
            Log.d("displayBoard()", "Turning on listeners");
            SudokuCell.ignoreListeners = false;
        }
    }

    public void clearBoard(View view) {

        Log.d("clearBoard()", "Turning off listeners");
        SudokuCell.ignoreListeners = true;

        for(int gridRow = 1; gridRow <= 3; gridRow++) {
            for(int gridCol = 1; gridCol <= 3; gridCol++) {
                for(int cellRow = 1; cellRow <= 3; cellRow++) {
                    for(int cellCol = 1; cellCol <= 3; cellCol++) {

                        SudokuCell cell = getCell(gridRow, gridCol, cellRow, cellCol);
                        cell.setText("");
                    }
                }
            }
        }

        Log.d("clearBoard()", "Turning on listeners");
        SudokuCell.ignoreListeners = false;
    }

    // get a particular cell
    private SudokuCell getCell(int gridRow, int gridCol, int cellRow, int cellCol) {
        int gridId = getResources().getIdentifier("grid" + gridRow + gridCol, "id", "com.davidretler.sudokusolver");
        View currGrid = findViewById(gridId);
        Log.d("getCell()", "Got the grid!");
        int cellNum = (cellRow-1) * 3 + cellCol;
        int cellId = getResources().getIdentifier("cell" + cellNum, "id", "com.davidretler.sudokusolver");
        Log.d("getCell()", "Trying to get cell " + cellNum);
        return (SudokuCell) currGrid.findViewById(cellId);
    }

    private void playPause(MenuItem item) {
        if(item.getTitle().toString().equals("Pause")) {
            if(solving) {
                Log.d("Menu", "Pausing");
                item.setTitle("Play");
                paused = true;
            }
        } else {
            if(solving) {
                synchronized (solveThread.lock) {
                    Log.d("Menu", "Playing");
                    paused = false;
                    item.setTitle("Pause");
                    solveThread.lock.notifyAll();
                }
            }
        }
    }
}
