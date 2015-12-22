package com.davidretler.sudokusolver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SudokuBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_board);
    }

    public void solveBoard(View view) {
        Log.d("solveBoard()", "This will solve the board");
        Log.d("solveBoard()", "Turning off listeners");
        SudokuCell.ignoreListeners = true;
        SudokuBoard myBoard = parseBoard();
        if (myBoard.solve()) {
            displayBoard(myBoard);
        } else {
            Alerts.error("No Solution", "The current board has no valid solution.", view.getContext());
        }
        Log.d("solveBoard()", "Turning on listeners");
        SudokuCell.ignoreListeners = false;
    }

    // parse the current state of the board and return a sudokuboard object with that state
    private SudokuBoard parseBoard() {

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
                            Alerts.error("Error", "There was an error parsing the board.", this.getApplicationContext());
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }

        Log.d("solveBoard()", "Turning on listeners");
        SudokuCell.ignoreListeners = false;

        return new SudokuBoard(boardArray);
    }

    private void displayBoard(SudokuBoard board) {

        Log.d("solveBoard()", "Turning off listeners");
        SudokuCell.ignoreListeners = true;

        for(int gridRow = 1; gridRow <= 3; gridRow++) {
            for(int gridCol = 1; gridCol <= 3; gridCol++) {
                for(int cellRow = 1; cellRow <= 3; cellRow++) {
                    for(int cellCol = 1; cellCol <= 3; cellCol++) {

                        SudokuCell cell = getCell(gridRow, gridCol, cellRow, cellCol);
                        int boardIndexRow = (gridRow-1)*3 + cellRow - 1;
                        int boardIndexCol = (gridCol-1)*3 + cellCol - 1;

                        cell.setText("" + board.getNum(boardIndexRow,boardIndexCol));
                    }
                }
            }
        }

        Log.d("solveBoard()", "Turning on listeners");
        SudokuCell.ignoreListeners = false;
    }

    public void clearBoard(View view) {

        Log.d("solveBoard()", "Turning off listeners");
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

        Log.d("solveBoard()", "Turning on listeners");
        SudokuCell.ignoreListeners = false;
    }

    private SudokuCell getCell(int gridRow, int gridCol, int cellRow, int cellCol) {
        int gridId = getResources().getIdentifier("grid" + gridRow + gridCol, "id", "com.davidretler.sudokusolver");
        View currGrid = findViewById(gridId);
        Log.d("getCell()", "Got the grid!");
        int cellNum = (cellRow-1) * 3 + cellCol;
        int cellId = getResources().getIdentifier("cell" + cellNum, "id", "com.davidretler.sudokusolver");
        Log.d("getCell()", "Trying to get cell " + cellNum);
        return (SudokuCell) currGrid.findViewById(cellId);
    }
}
