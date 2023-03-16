package main;

import java.util.ArrayList;
import java.util.List;

public class FlexibleTicTacToe {

	private char[][] tiles;
	private int len;
	private List<Character> player1Positions = new ArrayList<Character>();
	private List<Character> player2Positions = new ArrayList<Character>();
//	private List<List<Character>> winningList = new ArrayList<List<Character>>();

	// constructor
	FlexibleTicTacToe(int number) throws TilesNumberException {
		decideTilesNumber(number);
		len = number;
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

	// specify the number of tiles (rows and columns)
	public char[][] decideTilesNumber(int i) throws TilesNumberException {
		// in these case, cannot create a board to play tic-tac-toe
		if (i < 2) {
			throw new TilesNumberException("Please enter a number greater than 2");
		}
		// create a flexible tic-tac-toe game board
		return tiles = new char[i][i];

	}

	// place their symbol on the board if the position is available
	public WinStatus playerSelectPosition(int row, int column, Player player) throws PositionNumberException {
		// check if the position is available
		checkThePosition(row, column);
		// when player1's turn
		if (player == Player.PLAYER1) {
			// put the symbol in the position
			tiles[row][column] = 'x';
			player1Positions.add(tiles[row][column]);
			// check if the player win
			return checkWinning(player);
		}
		// when player2's turn
		if (player == Player.PLAYER2) {
			tiles[row][column] = 'o';
			player2Positions.add(tiles[row][column]);
			return checkWinning(player);
		}
		// want to throw error
		return null;
	}
	
	// make winning condition 
	public List<List<Character>> winningCondition() {
		List<List<Character>> winningList = new ArrayList<List<Character>>();
		// winning condition in horizontal
		for (int i = 0; i < len; i++) {
			List<Character> horizontal = new ArrayList<Character>();
			for (int j = 0; j < len; j++) {
				horizontal.add(tiles[i][j]);
			}
			// add to winning condition list
			winningList.add(horizontal);
		}
		// winning condition in vertical
		for (int i = 0; i < len; i++) {
			List<Character> vertical = new ArrayList<Character>();
			for (int j = 0; j < len; j++) {
				vertical.add(tiles[j][i]);
			}
			winningList.add(vertical);
		}
		// winning condition in diagonal from top
		List<Character> diagonalT = new ArrayList<Character>();
		for (int i = 0; i < len; i++) {
			diagonalT.add(tiles[i][i]);
		}
		winningList.add(diagonalT);

		// winning condition in diagonal from bottom
		List<Character> diagonalB = new ArrayList<Character>();
		int y = 0;
		for (int i = len - 1; i >= 0; i--) {
			diagonalB.add(tiles[i][y]);
			y++;
		}
		winningList.add(diagonalB);
		return winningList;
	}
	
	// check if the player win
	public WinStatus checkWinning(Player player) {
		List<List<Character>> winningList = winningCondition();
		if (player1Positions.size() + player2Positions.size() == len * len) {
			return WinStatus.DRAW;
		}
		switch (player) {
		case PLAYER1:
			for (List<Character> list : winningList) {
				if (player1Positions.containsAll(list)) {
					return WinStatus.PLAYER1;
				}
			}
			break;
		case PLAYER2:
			for (List<Character> list : winningList) {
				if (player2Positions.containsAll(list)) {
					return WinStatus.PLAYER2;
				}
			}
		}
		return WinStatus.INPROGRESS;
	}

	// check if the position that players select is available
	public void checkThePosition(int row, int column) throws PositionNumberException {
		// in this case, the position does not exist
		if (row >= len || column >= len) {
			throw new PositionNumberException("Please put the number under " + len);
		}
		if(row < 0 || column < 0) {
			throw new PositionNumberException("Please put the number greater 0");
		}
		// in this case, cannot select the position because it is already selected
		if (tiles[row][column] == 'x' || tiles[row][column] == 'o') {
			throw new PositionNumberException("You cannot select the position because it is already selected.");
		}
	}

}
