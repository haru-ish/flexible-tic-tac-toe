package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FlexibleTicTacToe {

	private int rows;
	private int columns;
	private int winLen;
	private HashMap<Player, List<Integer>> playerPositions = new HashMap<Player, List<Integer>>() {
		private static final long serialVersionUID = 1L;
		{
			for (Player player : Player.values()) {
				put(player, new ArrayList<Integer>());
			}
		}
	};
	private List<List<Integer>> winningCondidions = null;

	// specify the number of tiles (rows and columns) and a row to win
	FlexibleTicTacToe(int rows, int columns, int winLen) throws TilesNumberException {
		// check arguments are available or not
		validateBoardSettings(rows, columns, winLen);
		this.rows = rows;
		this.columns = columns;
		this.winLen = winLen;
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

	public void validateBoardSettings(int rows, int columns, int winLen) throws TilesNumberException {
		// in these case, cannot create a board to play tic-tac-toe
		if (rows < 1 || columns < 1 || winLen < 1) {
			throw new TilesNumberException("Enter a number larger than 0");
		}
		if (Math.max(rows, columns) < winLen) {
			throw new TilesNumberException("Set the number of a row to win less than " + winLen);
		}
	}

	// create a flexible tic-tac-toe game board
	public char[][] makeGameBoard() {
		// this is a visualization of the game board
		char[][] tiles = new char[rows][columns];
		// initialize tiles
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				tiles[i][j] = ' ';
			}
		}
		// place the symbol in the game board
		for (Map.Entry<Player, List<Integer>> positionEntry : playerPositions.entrySet()) {
			for (Integer position : positionEntry.getValue()) {
				tiles[(position - 1) / columns][(position - 1) % columns] = positionEntry.getKey().getSymbol();
			}
		}
		return tiles;
	}

	// print the gameBorad to console
	public String textGameBoard() {
		char[][] gameBoard = makeGameBoard();
		// store all rows as string list including separator("|")
		List<String> textRows = new LinkedList<>();

		for (int i = 0; i < rows; i++) {
			// store one row as string list
			List<String> strings = new LinkedList<>();
			for (int j = 0; j < columns; j++) {
				strings.add(" " + gameBoard[i][j] + " ");
			}
			// insert "|" between each tile in the row
			String row = String.join("|", strings);
			textRows.add(row);
		}
		// store one line for separator
		List<String> separatorList = new LinkedList<>();
		for (int i = 0; i < columns; i++) {
			separatorList.add("---");
		}
		// insert blank between each separator in the line
		String separator = String.join(" ", separatorList);
		// insert new line + one line for separator + new line between each row
		String textBoard = String.join("\n" + separator + "\n", textRows);

		return textBoard;
	}

	// add the position in the playerPositions if the position is available
	public WinStatus playerSelectPosition(int position, Player player) throws PositionNumberException {
		// check if the position is available
		checkPositionAvailable(position);
		
		playerPositions.get(player).add(position);
		// check winning status
		return checkWinning();
	}

	// make diagonal winning conditions
	private void winningConditionDiagonals() {
		for (int curRow = 0; curRow <= rows - winLen; curRow++) {
			for (int curCol = 0; curCol <= columns - winLen; curCol++) {
				// make a diagonal winning condition from left and top
				List<Integer> newTL2BR = new ArrayList<Integer>();
				// make a diagonal winning condition from left and bottom
				List<Integer> newBL2TR = new ArrayList<Integer>();
				for (int i = 0; i < winLen; i++) {
					newTL2BR.add(curRow * columns + curCol + (columns + 1) * i + 1);
					newBL2TR.add((rows - curRow - 1) * columns + curCol - (columns - 1) * i + 1);
				}
				winningCondidions.add(newTL2BR);
				winningCondidions.add(newBL2TR);
			}
		}
	}

	// make straight winning conditions
	private void winningConditionStraights() {
		for (int curRow = 0; curRow < rows; curRow++) {
			for (int curCol = 0; curCol < columns; curCol++) {
				if (curCol <= columns - winLen) {
					List<Integer> newHorizontal = new ArrayList<Integer>();
					for (int i = 0; i < winLen; i++) {
						newHorizontal.add(curRow * columns + curCol + i + 1);
					}
					winningCondidions.add(newHorizontal);
				}
				if (curRow <= rows - winLen) {
					List<Integer> newVertical = new ArrayList<Integer>();
					for (int i = 0; i < winLen; i++) {
						newVertical.add(curRow * columns + curCol + i * columns + 1);
					}
					winningCondidions.add(newVertical);
				}
			}
		}
	}

	// call straight and diagonal winning conditions, or make winning condition if a row to win is 1
	public List<List<Integer>> winningCondition() {
		if (winningCondidions == null) {
			winningCondidions = new ArrayList<List<Integer>>();
			// make winning condition if a row to win is 1
			if (winLen == 1) {
				for (int i = 1; i <= rows * columns; i++) {
					List<Integer> newAllPositions = new ArrayList<Integer>();
					newAllPositions.add(i);
					winningCondidions.add(newAllPositions);
				}
				return winningCondidions;
			}
			// append diagonal winning conditions
			winningConditionDiagonals();
			// append straight winning conditions
			winningConditionStraights();
		}
		return winningCondidions;
	}

	// check winnig status
	public WinStatus checkWinning() {
		List<List<Integer>> winningList = winningCondition();

		// return WinStatus.PLAYER
		for (List<Integer> list : winningList) {
			for (Map.Entry<Player, List<Integer>> positionEntry : playerPositions.entrySet()) {
				if (positionEntry.getValue().containsAll(list)) {
					return positionEntry.getKey().toWinStatus();
				}
			}
		}
		
		int totalPositions = 0;
		for (Map.Entry<Player, List<Integer>> positionEntry : playerPositions.entrySet()) {
			// add size of the player's position list
			totalPositions += positionEntry.getValue().size();
		}
		if (totalPositions == rows * columns) {
			return WinStatus.DRAW;
		}
		return WinStatus.INPROGRESS;
	}

	// check if the position that players select is available
	public void checkPositionAvailable(int position) throws PositionNumberException {
		// in this case, the position does not exist
		if (position > rows * columns) {
			throw new PositionNumberException("Enter a number less than " + position);
		}
		if (position < 1) {
			throw new PositionNumberException("Enter a number larger than 0");
		}
		// in this case, cannot select the position because it is already selected
		for (Map.Entry<Player, List<Integer>> positionEntry : playerPositions.entrySet()) {
			if (positionEntry.getValue().contains(position)) {
				throw new PositionNumberException("You cannot select the position because it is already selected");
			}
		}
	}

}
