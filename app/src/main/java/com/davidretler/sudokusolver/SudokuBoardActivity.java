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

        parseBoard();
    }

    private SudokuBoard parseBoard() {
        int[][] boardArray = new int[9][9];

        for(int gridRow = 1; gridRow <= 3; gridRow++) {
            for(int gridCol = 1; gridCol <= 3; gridCol++) {
                for(int cellRow = 1; cellRow <= 3; cellRow++) {
                    for(int cellCol = 1; cellCol <= 3; cellCol++) {

                        try {
                            int gridId = getResources().getIdentifier("grid" + gridRow + gridCol, "id", "com.davidretler.sudokusolver");
                            View currGrid = findViewById(gridId);
                            Log.d("parseBoard()", "Got the grid!");
                            int cellNum = (cellRow-1) * 3 + cellCol;
                            int cellId = getResources().getIdentifier("cell" + cellNum, "id", "com.davidretler.sudokusolver");
                            Log.d("parseBoard()", "Trying to get cell " + cellNum);
                            TextView cell = (TextView) currGrid.findViewById(cellId);
                            Log.d("parseBoard()", "Got the cell");
                            Log.d("parseBoard()", "Value of the current cell is " + cell.getText().toString());

                            int cellValue = Integer.parseInt(cell.getText().toString());
                            int boardIndexRow = (gridRow-1)*3 + cellRow - 1;
                            int boardIndexCol = (gridCol-1)*3 + cellCol - 1;
                            boardArray[boardIndexRow][boardIndexCol] = cellValue;
                        } catch(Exception ex) {
                            Log.e("parseBoard()", "Failed loading board at " + gridRow + " " + gridCol + " " + cellRow + " " + cellCol);
                            ex.printStackTrace();
                        }

                    }
                }
            }
        }

        return null;
    }

    public void promptCellEntry(View view) {
        Log.d("promptCellEntry()", "Prompting entry for cell value");
        AlertFactory.info("Prompt Entry", "This will prompt an entry for the cell", this.getApplicationContext());
    }


}
