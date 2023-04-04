package main;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import main.FlexibleTicTacToe.Player;
import main.FlexibleTicTacToe.WinStatus;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Arrays;
import java.util.List;


@TestMethodOrder(OrderAnnotation.class)
class FlexibleTicTacToeTest {
	@Test
	@DisplayName("check next method of enum Player")
	public void checkPlayerNext() {
		assertEquals(Player.PLAYER1.next(), Player.PLAYER2);
		assertEquals(Player.PLAYER2.next(), Player.PLAYER1);
	}
	@Test
	@DisplayName("check to throw TilesNumberException If any of the row, column, or the winnnig row is 0")
	void checkThrowTilesNumberException1() {
		Throwable exception = assertThrows(TilesNumberException.class, () -> {
			new FlexibleTicTacToe(0,0,0);
		});
		assertEquals(exception.getMessage(), "Enter a number more than 0");
	}
	
	@Test
	@DisplayName("check to throw TilesNumberException if the winning row is larger than the row and column")
	void checkThrowTilesNumberException2() {
		Throwable exception = assertThrows(TilesNumberException.class, () -> {
			new FlexibleTicTacToe(1,1,3);
		});
		assertEquals(exception.getMessage(), "Set the number of a row to win less than " + 3);
	}
	
	@Test
	@DisplayName("check not to throw TilesNumberException")
	void checkDoesNotThrowTilesNumberException() throws TilesNumberException {
		assertDoesNotThrow(() -> new FlexibleTicTacToe(1,1,1));
	}

	@Test
	@DisplayName("check to make game board properly")
	void testMakeGameBoard() throws TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(3, 4, 2);
		char[][] tiles = flex.makeGameBoard(3, 4, 2);
		assertEquals(tiles[3-1][4-1], ' ');
		
	}

	@Test
	@DisplayName("check to throw PositionNumberException if the position does not exist")
	void checkThrowPositionNumberException1() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(3, 4, 2);
		Throwable exception = assertThrows(PositionNumberException.class, () -> {
			flex.checkThePosition(13);
		});
		assertEquals(exception.getMessage(), "Enter a number less than " + 13);
	}
	
	@Test
	@DisplayName("check to throw PositionNumberException if the number of position is 0")
	void checkThrowPositionNumberException2() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(3, 4, 2);
		Throwable exception = assertThrows(PositionNumberException.class, () -> {
			flex.checkThePosition(0);
		});
		assertEquals(exception.getMessage(), "Enter a number more than 0");
	}
	
	@Test
	@DisplayName("check to throw PositionNumberException when the position is already selected")
	void checkThrowPositionNumberException3() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(3, 4, 2);
		flex.checkThePosition(3);
		Throwable exception = assertThrows(PositionNumberException.class, () -> {
			flex.checkThePosition(3);
		});
		assertEquals(exception.getMessage(), "You cannot select the position because it is already selected");
	}
	
	@Test
	@DisplayName("check not to throw PositionNumberException")
	void checkDoesNotThrowPositionNumberException() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(3, 4, 2);
		assertDoesNotThrow(() -> flex.checkThePosition(12));
	}
	
	@Test
	@DisplayName("check selected position is add to the list")
	void testCheckThePosition() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(3, 4, 2);
		List<Integer> list= flex.checkThePosition(12);
		assertTrue(list.contains(12));
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
		flex.playerSelectPosition(1, Player.PLAYER1);
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
		FlexibleTicTacToe flex = new FlexibleTicTacToe(2, 6, 3);
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
		flex.playerSelectPosition(3, Player.PLAYER1);
		flex.playerSelectPosition(1, Player.PLAYER2);
		flex.playerSelectPosition(10, Player.PLAYER1);
		WinStatus result = flex.playerSelectPosition(7, Player.PLAYER2);
		
		assertEquals(result, WinStatus.PLAYER2);
	}
	
	@Test
	@DisplayName("check to return Player1 with horizontal winning condition")
	void checkReturnPlayer1WithHorizontal() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(4, 4, 2);
		flex.playerSelectPosition(1, Player.PLAYER1);
		flex.playerSelectPosition(16, Player.PLAYER2);
		WinStatus result = flex.playerSelectPosition(2, Player.PLAYER1);
		
		assertEquals(result, WinStatus.PLAYER1);
	}
	
	@Test
	@DisplayName("check to return Draw")
	void checkReturnDraw() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(5,4,5);
		List<Integer> positions = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
		Player player = Player.PLAYER1;
		WinStatus result = WinStatus.INPROGRESS;
		for (Integer position : positions) {
			result = flex.playerSelectPosition(position, player);
			player = player.next();
		}
		assertEquals(result, WinStatus.DRAW);
	}
}
