package main;

import java.util.Scanner;
import main.FlexibleTicTacToe.Player;
import main.FlexibleTicTacToe.WinStatus;

public class Main {

	public static void main(String[] args) throws TilesNumberException, PositionNumberException {
		// players decide the number of tiles
		FlexibleTicTacToe tictactoe = new FlexibleTicTacToe(3);
		// receive the game's result (who is winner)
		WinStatus result = WinStatus.INPROGRESS;
		Player player = Player.PLAYER1;
		// loop until one of them wins
		while (result == WinStatus.INPROGRESS) {
			Scanner scan = new Scanner(System.in);
			System.out.println(player.toString() + "'s turn: Enter row and column");
			// a tile has row and column
			int row = scan.nextInt();
			int column = scan.nextInt();
			// the player select a position (row and column) to put the symbol
			// check if this turn the player wins or not
			result = tictactoe.playerSelectPosition(row, column, player);
			// change player
			player = player.next();
		}
		if(result == WinStatus.DRAW) {
			System.out.println("Outcome: this game is a " + result.toString());
		} else {
			System.out.println("Outcome: " + result.toString() + " won!");
		}
	}
}
