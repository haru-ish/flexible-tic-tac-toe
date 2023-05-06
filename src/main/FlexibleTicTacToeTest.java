package main;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import main.FlexibleTicTacToe.Player;
import main.FlexibleTicTacToe.WinStatus;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@TestMethodOrder(OrderAnnotation.class)
class FlexibleTicTacToeTest {
	@Test
	@DisplayName("check symbol of enum Player")
	public void checkPlayerSymbol() {
		assertEquals(Player.PLAYER1.getSymbol(), 'o');
		assertEquals(Player.PLAYER2.getSymbol(), 'x');
	}

	@Test
	@DisplayName("check next method of enum Player")
	public void checkPlayerNext() {
		assertEquals(Player.PLAYER1.next(), Player.PLAYER2);
		assertEquals(Player.PLAYER2.next(), Player.PLAYER1);
	}

	@Test
	@DisplayName("check winStatus method of enum Player")
	public void checkPlayerToWinStatus() {
		assertEquals(Player.PLAYER1.toWinStatus(), WinStatus.PLAYER1);
		assertEquals(Player.PLAYER2.toWinStatus(), WinStatus.PLAYER2);
	}

	@Test
	@DisplayName("check to throw TilesNumberException If any of the row, column, or the winnnig row is 0")
	void checkThrowTilesNumberException1() {
		assertThrows(TilesNumberException.class, () -> {
			new FlexibleTicTacToe(0, 0, 0);
		});
	}

	@Test
	@DisplayName("check to throw TilesNumberException if the winning row is larger than the row and column")
	void checkThrowTilesNumberException2() {
		assertThrows(TilesNumberException.class, () -> {
			new FlexibleTicTacToe(1, 1, 3);
		});
	}

	@Test
	@DisplayName("check not to throw TilesNumberException")
	void checkDoesNotThrowTilesNumberException() throws TilesNumberException {
		assertDoesNotThrow(() -> new FlexibleTicTacToe(1, 1, 1));
	}

	@Test
	@DisplayName("check to return INPROGRESS for player1")
	void testTextGameBoard() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(3, 4, 2);
		flex.playerSelectPosition(1, Player.PLAYER1);
		String gameBoard = flex.textGameBoard();
		int rows = 3;
		int columns = 4; 
		int len = 4 * columns * (rows * 2 - 1) -1;
		assertEquals(gameBoard.length(), len);
		assertEquals(gameBoard.charAt(1), Player.PLAYER1.getSymbol());
	}

	@Test
	@DisplayName("check to throw PositionNumberException if the position does not exist")
	void checkThrowPositionNumberException1() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(3, 4, 2);
		assertThrows(PositionNumberException.class, () -> {
			flex.playerSelectPosition(13, Player.PLAYER1);
		});
	}

	@Test
	@DisplayName("check to throw PositionNumberException if the number of position is 0")
	void checkThrowPositionNumberException2() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(3, 4, 2);
		assertThrows(PositionNumberException.class, () -> {
			flex.playerSelectPosition(0, Player.PLAYER1);
		});
	}

	@Test
	@DisplayName("check to throw PositionNumberException when the position is already selected")
	void checkThrowPositionNumberException3() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(3, 4, 2);
		flex.playerSelectPosition(3, Player.PLAYER1);
		assertThrows(PositionNumberException.class, () -> {
			flex.playerSelectPosition(3, Player.PLAYER2);
		});
	}
	
	@Test
	@DisplayName("check not to throw PositionNumberException")
	void checkDoesNotThrowPositionNumberException() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(3, 4, 2);
		assertDoesNotThrow(() -> flex.playerSelectPosition(3, Player.PLAYER1));
	}
	
	@Test
	@DisplayName("check to return INPROGRESS for player1")
	void checkReturnInprogress1() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(3, 4, 2);
		WinStatus result = flex.playerSelectPosition(1, Player.PLAYER1);
		assertEquals(result, WinStatus.INPROGRESS);
	}

	@Test
	@DisplayName("check to return INPROGRESS for player2")
	void checkReturnInprogress2() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(3, 4, 2);
		flex.playerSelectPosition(9, Player.PLAYER1);
		WinStatus result = flex.playerSelectPosition(2, Player.PLAYER2);
		assertEquals(result, WinStatus.INPROGRESS);
	}

	@Test
	@DisplayName("check to return Player1 if the winning row is 1")
	void checkReturnPlay1WithTheWinningRow1() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(3, 4, 1);
		WinStatus result = flex.playerSelectPosition(3, Player.PLAYER1);
		assertEquals(result, WinStatus.PLAYER1);
	}

	@Test
	@DisplayName("check to return Player1 with vertical winning condition")
	void checkReturnPlayer1() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(6, 2, 3);
		flex.playerSelectPosition(1, Player.PLAYER1);
		flex.playerSelectPosition(9, Player.PLAYER2);
		flex.playerSelectPosition(3, Player.PLAYER1);
		flex.playerSelectPosition(4, Player.PLAYER2);
		WinStatus result = flex.playerSelectPosition(5, Player.PLAYER1);
		assertEquals(result, WinStatus.PLAYER1);
	}

	@Test
	@DisplayName("check to return Player2 with diagonal winning condition")
	void checkReturnPlayer2() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(5, 3, 2);
		flex.playerSelectPosition(6, Player.PLAYER1);
		flex.playerSelectPosition(15, Player.PLAYER2);
		flex.playerSelectPosition(10, Player.PLAYER1);
		WinStatus result = flex.playerSelectPosition(11, Player.PLAYER2);

		assertEquals(result, WinStatus.PLAYER2);
	}

	@Test
	@DisplayName("check to return Player1 with horizontal winning condition")
	void checkReturnPlayer1WithHorizontal() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(4, 4, 2);
		flex.playerSelectPosition(11, Player.PLAYER1);
		flex.playerSelectPosition(16, Player.PLAYER2);
		WinStatus result = flex.playerSelectPosition(10, Player.PLAYER1);

		assertEquals(result, WinStatus.PLAYER1);
	}

	@Test
	@DisplayName("check to return Draw")
	void checkReturnDraw() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(2, 3, 3);
		List<Integer> positions = Arrays.asList(1, 2, 3, 4, 5, 6);
		Player player = Player.PLAYER1;
		WinStatus result = WinStatus.INPROGRESS;
		for (Integer position : positions) {
			result = flex.playerSelectPosition(position, player);
			player = player.next();
		}
		assertEquals(result, WinStatus.DRAW);
	}
}
