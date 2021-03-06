package com.davidretler.sudokusolver;

import android.os.SystemClock;
import android.util.Log;

public class SudokuBoard {

	// store the board
	private int[][] board;
	// reference to the activity (to update the board)
	private SudokuBoardActivity activity = null;

    private SolveThread thread;

    // runnable to update the board
    // thgis is necessary as updating the UI cannot be done in the solve thread
    // so we must post this runnable to the UI thread
    private static class BoardUpdater implements Runnable {

        SudokuBoardActivity activity;
        SudokuBoard myBoard;

        public BoardUpdater(SudokuBoardActivity activity, SudokuBoard myBoard) {
            this.activity = activity;
            this.myBoard = myBoard;
        }

        @Override
        public void run() {
            Log.d("BoardUpdater", "Displaying board");
			Log.d("BoardUpdater", myBoard.toString());
            activity.displayBoard(myBoard);
        }
    }

	/**
	 * Defult constructor. Creates a blank board.
	 */
	public SudokuBoard() {
		board = new int[9][9];

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				board[i][j] = 0;
			}
		}
	}

	/**
	 * Create new board with initial state.
	 *
	 * @param init initial state of board
	 */
	public SudokuBoard(int[][] init) {
		board = init;
	}

	/**
	 * Sets position (i,j) to num if valid position. Returns true if valid.
	 * Returns false if invalid, and does not allow change to be made.
	 *
	 * @param i Row
	 * @param j Column
	 * @return true if valid change, false otherwise
	 *
	 */
	public boolean set(int i, int j, int num) {
		if (num < 0 || num > 9)
			throw new IllegalArgumentException();
		if (board[i][j] != 0)
			throw new RuntimeException();

		int oldIJ = board[i][j];
		board[i][j] = num;

		if (this.check()) {
			return true;
		} else {
			board[i][j] = oldIJ;
			return false;
		}
	}

	/**
	 * Unsets the board positon (i,j).
	 *
	 * @param i Row
	 * @patam j Column
	 */
	public void unSet(int i, int j) {
		board[i][j] = 0;
	}

	/**
	 * Checks to see whether the current state of the board is valid.
	 *
	 * @return whether the current state of the board is valid
	 */
	public boolean check() {
		//check the rows
		for(int row = 0; row < 9; row++) {
			boolean[] used = {false, false, false, false, false, false, false, false, false};

			//check along each row
			for(int j = 0; j < 9; j++) {
				if(board[row][j] != 0) {
					if(used[board[row][j] - 1]) return false;
					used[board[row][j] - 1] = true;
				}
			}
		}

		//check the cols
		for(int col = 0; col < 9; col++) {
			boolean[] used = {false, false, false, false, false, false, false, false, false};

			//check down each col
			for(int i = 0; i < 9; i++) {
				if(board[i][col] != 0) {
					if(used[board[i][col] - 1]) return false;
					used[board[i][col] - 1] = true;
				}
			}
		}

		//check the squares
		for(int xOff = 0; xOff < 9; xOff += 3) {
			for(int yOff = 0; yOff < 9; yOff += 3) {
				boolean[] used = {false, false, false, false, false, false, false, false, false};

				for(int i = 0; i < 3; i++) {
					for(int j = 0; j < 3; j++) {
						if(board[xOff + i][yOff + j] != 0) {
							if(used[board[xOff + i][yOff + j] - 1]) return false;
							used[board[xOff + i][yOff +j] - 1] = true;
						}
					}
				}
			}
		}

		return true;
	}

	public String toString() {
		// really ugly code for formatting the board
		String out = "———————  ———————  ———————\n";

		for (int i = 0; i < 9; i++) {
			out += '|';
			for (int j = 0; j < 9; j++) {
				out += board[i][j] == 0 ? " " : board[i][j]+"";
				out += (j + 1) % 3 == 0 ? (j != 8 ? "|  |" : "| ") : "|";
			}
			out += "\n———————  ———————  ———————\n";
			if ((i + 1) % 3 == 0) {
				if (i != 8)
					out += "\n———————  ———————  ———————\n";
				else
					out += "\n";
			}

		}

		return out;
	}

	/**
	 * If (i,j) definitely needs to take on a given value to solve the board, set that
	 * as the value. Otherwise, do nothing.
	 *
	 * @param i Row
	 * @param j Column
	 */
	public boolean definiteMove(int i, int j) {

	    boolean move = false;

		// only try to make a move if this spot hasn't been set already
		if (board[i][j] != 0)
			return false;

		int count = 0;

        // count number of possible moves
		for (int num = 1; num <= 9; num++) {
			if (this.set(i, j, num)) {
				count++;
				this.unSet(i, j);
			}
		}

		// if only one possible move, make that move
		if (count == 1) {
			for (int num = 1; num <= 9; num++) {
				if (this.set(i, j, num)) {
                    Log.d("definiteMove()", "Made definite move");
                    updateIfStep();
					break;
				}
			}
		}

		return (count == 1);

	}

	/**
	 * Get the value at (i,j).
	 * @param i Row
	 * @param j Column
	 * @return 0 if empty, otherwise the number written at the location
	 */
	public int getNum(int i, int j) {
		return board[i][j];
	}

	/**
	 * Return the state of the board as a double array
	 *
	 * @return The state of the board
	 */
	public int[][] getState() {
		int[][] state = new int[9][9];

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				state[i][j] = board[i][j];
			}
		}

		return state;
	}

	/**
	 * Set the state of the board.
	 * @param state The state of the board as an array
	 */
	public void setState(int[][] state) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				board[i][j] = state[i][j];
			}
		}
	}

	/**
	 * Returns whether the board is solved.
	 *
	 * @return
	 */
	public boolean solved() {
		if (this.check()) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (board[i][j] == 0) {
						return false;
					}
				}
			}
			// valid config and all spots filled: yay!
			return true;
		}

		return false;
	}

    public boolean solve() {
        if(!this.check()) {
            // board contains an error and therefor has no valid solution
            return false;
        }
        return solve(0,0);
    }

	/**
	 * Recursively solves sudoku board using backtracking Starts at (i,j) Returns 0 if no
	 * solution
	 *
	 * @return
	 */
	private boolean solve(int i, int j) {


		if (!this.check())
			// base case: return false if current config is invalid
			return false;

		if (this.solved()) {
			// base case: return true if board is already solved
			return true;
		}

		// make sure to save state before making moves
		int[][] oldState = this.getState();

		// make all moves that certainly must be made
		makeDefiniteMoves();

		if (this.getNum(i, j) == 0) {
			// try each number for current configuration, if not already solved
			for (int n = 1; n <= 9; n++) {
				if (this.set(i, j, n)) {
					// if we can set the current position to, do so and solve
					// rest of board (this is guess)
                    updateIfStep();


					// next i value is either i+1 or 1 if i is already 9
					int nextI = i < 8 ? i + 1 : 1;
					int nextJ = j;
					if (nextI < i) {
						// only change the j if i has moved back to 1
						nextJ = j < 8 ? j + 1 : 1;

					}
					if (solve(nextI, nextJ)) {
						// if rest of board can be solved from here, then this is a valid
						// solution
						return true;
					} else {
						// rest of board cannot be solved so undo the current
						// change, and restart loop to try next possible value for this
						// location
						this.unSet(i, j);
					}
				}
			}
		} else {
			// current position already has entry, so go to the next one and solve from
			// there
			int nextI = i < 8 ? i + 1 : 1;
			int nextJ = j;
			if (nextI < i) {
				// only change the j if i has moved back to 1
				nextJ = j < 8 ? j + 1 : 1;

			}
			if (solve(nextI, nextJ)) {
				// if rest of board can be solved from here, backtrack
				return true;
			}
		}

		// if we make it here than we could not solve the board given this configuration
		// so we need to restore the old state and then return (backtrack)
		this.setState(oldState);
		return false;
	}

	/**
	 * Makes moves to the board that we can be certain must be made
	 */
	private void makeDefiniteMoves() {

		boolean move = false;
		do {
			// keep looping until none of the entries can be decided
			move = false;
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					// check each (i,j) to see if it hasn't been set yet
					if (this.getNum(i, j) == 0) {
						if (this.definiteMove(i, j)) {
							// for unset (i,j), set the value if it can be decided
							// keep track whether a value was changed
							move = true;
						}
					}
				}
			}
		} while (move); // restart loop as long as we can make changes

	}

    // update the board
    private void updateIfStep() {

        if(thread.pauseOnUpdate) {
            SudokuBoardActivity.paused = true;
            activity.runOnUiThread(new BoardUpdater(activity, this));
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.setPause();
                }
            });
            thread.pauseOnUpdate = false;
        }

        if(activity.step) {
            synchronized (thread.lock) {
                while (SudokuBoardActivity.paused) {
                    Log.d("updateIfStep()", "Waiting to unpause");
                    try {
                        Log.d("updateIfStep()", "Waiting...");
                        thread.lock.wait();
                        Log.d("updateIfStep()", "...done");
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                        Alerts.error("Error", "An unexpected interrupt occurred.", activity);
                    }
                }
                activity.runOnUiThread(new BoardUpdater(activity, this));
                SystemClock.sleep((long) activity.stepTime);
            }
            Log.d("updateIfStep()", "Updating");
			Log.d("updateIfStep()", this.toString());
        }
    }

	/**
	 * Set the activity, so we can update the board
	 * @param activity the activity
	 */
	void setActivity(SudokuBoardActivity activity) {
        this.activity = activity;
	}

    void setSolveThread(SolveThread thread) {
        this.thread = thread;
    }

}
