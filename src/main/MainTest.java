package main;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import main.FlexibleTicTacToe.Player;
import main.FlexibleTicTacToe.WinStatus;

class MainTest {

	private static FlexibleTicTacToe tictactoe;
	private static int len = 3;
	
	@BeforeAll
	static void setUp() throws TilesNumberException {
		tictactoe = new FlexibleTicTacToe(len);
	}
	
	@Test
	@DisplayName("check to throw TilesNumberException")
	void testFlexibleTicTacToe1() throws TilesNumberException {
		Throwable exception = assertThrows(TilesNumberException.class, () -> {
			tictactoe = new FlexibleTicTacToe(2);
		});
		assertEquals(exception.getMessage(),"Please enter a number greater than 2");
	}
	@Test
	@DisplayName("check not to throw TilesNumberException")
	void testFlexibleTicTacToe2() throws TilesNumberException {
		assertDoesNotThrow(() -> tictactoe = new FlexibleTicTacToe(len));
	}
	@Test
	@Order(1)
	@DisplayName("test playerSelectPosition method for player1")
	void testPlayer1SelectPosition() throws PositionNumberException {
		WinStatus status = tictactoe.playerSelectPosition(len-1, len-1, Player.PLAYER1);
		assertEquals(status,WinStatus.INPROGRESS);		
	}
	@Test
	@Order(2)
	@DisplayName("test playerSelectPosition method for player2")
	void testPlayer2SelectPosition() throws PositionNumberException {
		WinStatus status = tictactoe.playerSelectPosition(len-2, len-2, Player.PLAYER2);
		assertEquals(status,WinStatus.INPROGRESS);
	}
	
	

}
