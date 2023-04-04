package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FlexibleTicTacToe {

	private char[][] tiles;
	private int row;
	private int column;
	private int num;
	private List<Integer> selectedPositions = new ArrayList<Integer>();
	private List<Integer> player1Positions = new ArrayList<Integer>();
	private List<Integer> player2Positions = new ArrayList<Integer>();

	// specify the number of tiles (rows and columns) and a row to win
	FlexibleTicTacToe(int row, int column, int num) throws TilesNumberException {
		// create a flexible tic-tac-toe game board
		makeGameBoard(row, column, num);
		this.row = row;
		this.column = column;
		this.num = num;
	}

	public enum Player {
		PLAYER1, PLAYER2;
		public Player next() {
			return Player.values()[(this.ordinal() + 1) % Player.values().length];
		}
	}

	public enum WinStatus {
		PLAYER1, PLAYER2, DRAW, INPROGRESS
	}
	
	public char[][] makeGameBoard(int row, int column, int num) throws TilesNumberException {
		// check arguments are available or not
		checkNumbers(row, column, num);
		// create a flexible tic-tac-toe game board
		tiles = new char[row][column];
		// give each tile a value
		char index = ' ';
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				tiles[i][j] = index;
			}
		}
		return tiles;
	}

	public void checkNumbers(int row, int column, int num) throws TilesNumberException {
		// in these case, cannot create a board to play tic-tac-toe
		if (row < 1 || column < 1 || num < 1) {
			throw new TilesNumberException("Enter a number more than 0");
		}
		if (Math.max(row, column) < num) {
			throw new TilesNumberException("Set the number of a row to win less than " + num);
		}
	}

	// place their symbol on the board if the position is available
	public WinStatus playerSelectPosition(int position, Player player) throws PositionNumberException {
		// check if the position is available
		checkThePosition(position);

		char playerSymbol;
		List<Integer> playerPositions;

		// when player1's turn
		if (player == Player.PLAYER1) {
			playerSymbol = 'o';
			playerPositions = player1Positions;
		// when player2's turn
		} else {
			playerSymbol = 'x';
			playerPositions = player2Positions;
		}
		// place the symbol in the position
		tiles[(position - 1) / column][(position - 1) % column] = playerSymbol;
		playerPositions.add(position);

		// check if the player win
		return checkWinning(player);
	}

	// make diagonal winning conditions
	public List<List<Integer>> winningConditionWithDiagonal() {
		List<List<Integer>> allList = new ArrayList<List<Integer>>();
		int element = 1;
		// current number of column
		int line = 1;
		// make a diagonal winning condition from left and top
		for (int i = 1; i < row * column; i++) {
			if (element == line * row) {
				line++;
				element += 1;
				i = element;
			}
			element = i;
			List<Integer> list = new ArrayList<Integer>();
			if (column - num + 1 >= line) {
				if (i + (num - 1) <= line * row) {
					while (list.size() < num) {
						list.add(element);
						element += row + 1;
					}
					allList.add(list);
				} else {
					element = line * row;
				}
			} else {
				break;
			}
		}
		// make a diagonal winning condition from right and bottom
		line = column;
		for (int i = row * column; i > 1; i--) {
			element = i;
			if (element != column * row && element % row == 0) {
				line--;
			}
			List<Integer> list = new ArrayList<Integer>();
			if (num <= line) {
				if (i + (num - 1) <= line * row) {
					while (list.size() < num) {
						list.add(element);
						element -= row - 1;
					}
					allList.add(list);
				}
			} else {
				break;
			}
		}
		return allList;
	}

	// make winning conditions except diagonal
	public List<List<Integer>> winningCondition() {
		List<List<Integer>> allList = new ArrayList<List<Integer>>();
		if (num == 1) {
			return allList;
		}
		if (Math.min(row, column) >= num) {
			// call the list that already contains the diagonal winning condition
			allList = winningConditionWithDiagonal();
		}

		int element = 1;
		// make the horizontal winning condition
		if (column < num || Math.min(row, column) >= num) {
			for (int i = 1; i < row * column; i++) {
				if (element % row == 0) {
					i = element + 1;
				}
				element = i;
				if (element < row * column) {
					List<Integer> list = new ArrayList<Integer>();
					while (list.size() < num) {
						list.add(element);
						element++;
					}
					allList.add(list);
					element = list.get(num - 1);
				} else {
					break;
				}
			}
		}
		// make the vertical winning condition
		if (row < num || Math.min(row, column) >= num) {
			int stop = (row * column) - (row * (num - 1));
			for (int i = 1; i < row * column; i++) {
				element = i;
				if (stop >= element) {
					List<Integer> list = new ArrayList<Integer>();
					while (list.size() < num) {
						list.add(element);
						element += row;
					}
					allList.add(list);
				} else {
					break;
				}
			}
		}
		return allList;
	}

	// check if the player win
	public WinStatus checkWinning(Player player) {
		// if a row to win is 1, player 1 wins
		if (num == 1) {
			return WinStatus.PLAYER1;
		}
		if (player1Positions.size() + player2Positions.size() == row * column) {
			return WinStatus.DRAW;
		}
		List<List<Integer>> winningList = winningCondition();
		switch (player) {
		case PLAYER1:
			for (List<Integer> list : winningList) {
				if (player1Positions.containsAll(list)) {
					return WinStatus.PLAYER1;
				}
			}
			break;
		case PLAYER2:
			for (List<Integer> list : winningList) {
				if (player2Positions.containsAll(list)) {
					return WinStatus.PLAYER2;
				}
			}
		}
		return WinStatus.INPROGRESS;
	}

	// check if the position that players select is available
	public List<Integer> checkThePosition(int position) throws PositionNumberException {
		// in this case, the position does not exist
		if (position > row * column) {
			throw new PositionNumberException("Enter a number less than " + position);
		}
		if (position < 1) {
			throw new PositionNumberException("Enter a number more than 0");
		}
		// in this case, cannot select the position because it is already selected
		if (selectedPositions.contains(position)) {
			throw new PositionNumberException("You cannot select the position because it is already selected");
		}
		selectedPositions.add(position);

		return selectedPositions;
	}

}
