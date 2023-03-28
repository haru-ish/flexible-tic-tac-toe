package main;

import java.util.Scanner;
import main.FlexibleTicTacToe.Player;
import main.FlexibleTicTacToe.WinStatus;

public class Main {

	public static void main(String[] args) throws TilesNumberException, PositionNumberException {
		// decide the number of tiles(row, column) and how many in a row are required to win
		FlexibleTicTacToe tictactoe = new FlexibleTicTacToe(3,4,3);
		// set up the player
		Player player = Player.PLAYER1;
		// receive the game's result (who is winner)
		WinStatus result = WinStatus.INPROGRESS;
		// loop until one of them wins
		while (result == WinStatus.INPROGRESS) {
			Scanner scan = new Scanner(System.in);
			System.out.println(player.toString() + "'s turn: Enter your placement (1-" + 3*4 + ")");
			int position = scan.nextInt();
			// the player select a position (row and column) to put the symbol
			// check if this turn the player wins or not
			result = tictactoe.playerSelectPosition(position, player);
			// change player
			player = player.next();
		}
		// for print out
		if(result == WinStatus.DRAW) {
			System.out.println("Outcome: this game is a " + result.toString());
		} else {
			System.out.println("Outcome: " + result.toString() + " won!");
		}
	}
}
