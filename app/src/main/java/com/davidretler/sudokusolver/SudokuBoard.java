package com.davidretler.sudokusolver;

public class SudokuBoard {

	int[][] board;

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
}
