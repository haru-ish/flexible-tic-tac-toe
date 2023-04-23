package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlexibleTicTacToe {

	private int rows;
	private int columns;
	private int num;
	private HashMap<Player, List<Integer>> playerPositions = new HashMap<Player, List<Integer>>() {
		private static final long serialVersionUID = 1L;
		{
			put(Player.PLAYER1, new ArrayList<Integer>());
			put(Player.PLAYER2, new ArrayList<Integer>());
		}
	};
	private List<List<Integer>> allList = null;

	// specify the number of tiles (rows and columns) and a row to win
	FlexibleTicTacToe(int rows, int columns, int num) throws TilesNumberException {
		// check arguments are available or not
		checkNumbers(rows, columns, num);
		this.rows = rows;
		this.columns = columns;
		this.num = num;
		// create a flexible tic-tac-toe game board
		makeGameBoard();
	}

	public enum Player {
		PLAYER1('o'), PLAYER2('x');

		private final char symbol;

		private Player(final char symbol) {
			this.symbol = symbol;
		}
		public char getSymbol() {
			return this.symbol;
		}
		// get another player
		public Player next() {
			return Player.values()[(this.ordinal() + 1) % Player.values().length];
		}
		// get a variable of WinStatus
		public WinStatus toWinStatus() {
			return WinStatus.values()[this.ordinal()];
		}
	}

	public enum WinStatus {
		PLAYER1, PLAYER2, DRAW, INPROGRESS
	}

	public void checkNumbers(int rows, int columns, int num) throws TilesNumberException {
		// in these case, cannot create a board to play tic-tac-toe
		if (rows < 1 || columns < 1 || num < 1) {
			throw new TilesNumberException("Enter a number larger than 0");
		}
		if (Math.max(rows, columns) < num) {
			throw new TilesNumberException("Set the number of a row to win less than " + num);
		}
	}

	public char[][] makeGameBoard() throws TilesNumberException {
		// create a flexible tic-tac-toe game board
		char[][] tiles = new char[rows][columns];
		// initialize tiles
		char index = ' ';
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				tiles[i][j] = index;
			}
		}
		// place the symbol in the game board
		for (Map.Entry<Player, List<Integer>> positionEntry : playerPositions.entrySet()) {
			for (Integer position : positionEntry.getValue()) {
				tiles[(position - 1) / columns][(position - 1) % columns] = positionEntry.getKey().getSymbol();
			}
		}
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				System.out.println(tiles[i][j]);
			}
		}
		return tiles;
	}

	// add the position in the playerPositions if the position is available
	public WinStatus playerSelectPosition(int position, Player player) throws PositionNumberException {
		// check if the position is available
		checkThePosition(position);

		playerPositions.get(player).add(position);

		// check if the player win
		return checkWinning(player);
	}

	// make diagonal winning conditions
	public List<List<Integer>> winningConditionWithDiagonal() {
		for (int curRow = 0; curRow <= rows - num; curRow++) {
			for (int curCol = 0; curCol <= columns - num; curCol++) {
				// make a diagonal winning condition from left and top
				List<Integer> newTL2BR = new ArrayList<Integer>();
				// make a diagonal winning condition from left and bottom
				List<Integer> newBL2TR = new ArrayList<Integer>();
				for (int i = 0; i < num; i++) {
					newTL2BR.add(curRow * columns + curCol + (columns + 1) * i + 1);
					newBL2TR.add((rows - curRow - 1) * columns + curCol - (columns - 1) * i + 1);
				}
				allList.add(newTL2BR);
				allList.add(newBL2TR);
			}
		}
		return allList;
	}

	// make winning conditions except diagonal
	public List<List<Integer>> winningCondition() {
		if (allList == null) {
			allList = new ArrayList<List<Integer>>();
			// make winning condition, in the case a row to win is 1
			if (num == 1) {
				for(int i = 1; i <= rows * columns; i++) {
					List<Integer> newAllPositions = new ArrayList<Integer>();
					newAllPositions.add(i);
					allList.add(newAllPositions);
				}
				return allList;
			}
			
			if (Math.min(rows, columns) >= num) {
				// call the list that already contains the diagonal winning condition
				allList = winningConditionWithDiagonal();
			}
			// make winning conditions
			for (int curRow = 0; curRow < rows; curRow++) {
				for (int curCol = 0; curCol < columns; curCol++) {
					if (curCol <= columns - num) {
						List<Integer> newHorizontal = new ArrayList<Integer>();
						for (int i = 0; i < num; i++) {
							newHorizontal.add(curRow * columns + curCol + i + 1);
						}
						allList.add(newHorizontal);
					}
					if (curRow <= rows - num) {
						List<Integer> newVertical = new ArrayList<Integer>();
						for (int i = 0; i < num; i++) {
							newVertical.add(curRow * columns + curCol + i * columns + 1);
						}
						allList.add(newVertical);
					}
				}
			}
		}
		return allList;
	}

	// check if the player win
	public WinStatus checkWinning(Player player) {
		List<List<Integer>> winningList = winningCondition();

		// return WinStatus.PLAYER1 or WinStatus.PLAYER2
		for (List<Integer> list : winningList) {
			if (playerPositions.get(player).containsAll(list)) {
				return player.toWinStatus();
			}
		}
		if (playerPositions.get(Player.PLAYER1).size() + playerPositions.get(Player.PLAYER2).size() == rows * columns) {
			return WinStatus.DRAW;
		}
		return WinStatus.INPROGRESS;
	}

	// check if the position that players select is available
	public void checkThePosition(int position) throws PositionNumberException {
		// in this case, the position does not exist
		if (position > rows * columns) {
			throw new PositionNumberException("Enter a number less than " + position);
		}
		if (position < 1) {
			throw new PositionNumberException("Enter a number larger than 0");
		}
		// in this case, cannot select the position because it is already selected
		if (playerPositions.get(Player.PLAYER1).contains(position)
				|| playerPositions.get(Player.PLAYER2).contains(position)) {
			throw new PositionNumberException("You cannot select the position because it is already selected");
		}
	}

}
