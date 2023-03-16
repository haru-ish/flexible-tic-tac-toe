package main;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import main.FlexibleTicTacToe.Player;
import main.FlexibleTicTacToe.WinStatus;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;

@TestMethodOrder(OrderAnnotation.class)
class FlexibleTicTacToeTest {

	int len = 3;

	@Test
	@DisplayName("check next method of enum Player")
	public void checkPlayerNext() {
		assertEquals(Player.PLAYER1.next(), Player.PLAYER2);
		assertEquals(Player.PLAYER2.next(), Player.PLAYER1);
	}
	@Test
	@DisplayName("check to throw TilesNumberException")
	void checkThrowTilesNumberException() {
		Throwable exception = assertThrows(TilesNumberException.class, () -> {
			new FlexibleTicTacToe(2);
		});
		assertEquals(exception.getMessage(), "Please enter a number greater than 2");
	}
	@Test
	// @Order(1)
	@DisplayName("check not to throw TilesNumberException")
	void checkDoesNotThrowTilesNumberException() throws TilesNumberException {
		assertDoesNotThrow(() -> new FlexibleTicTacToe(len));
	}

	@Test
	@DisplayName("check size of tiles")
	void testDecideTilesNumber() throws TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(len);
		char[][] tiles = flex.decideTilesNumber(len);
		assertEquals(tiles.length, len);
	}

	@Test
	@DisplayName("check to throw PositionNumberException when the position does not exist")
	void checkThrowPositionNumberException1() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(len);
		Throwable exception = assertThrows(PositionNumberException.class, () -> {
			flex.checkThePosition(len, 0);
		});
		assertEquals(exception.getMessage(), "Please put the number under " + len);
	}

	@Test
	@DisplayName("check to throw PositionNumberException when the position does not exist")
	void checkThrowPositionNumberException2() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(len);
		Throwable exception = assertThrows(PositionNumberException.class, () -> {
			flex.checkThePosition(len - 1, -5);
		});
		assertEquals(exception.getMessage(), "Please put the number greater 0");
	}

	@Test
	@DisplayName("check to throw PositionNumberException when the position is already selected")
	void checkThrowPositionNumberException3() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(len);
		flex.playerSelectPosition(len - 1, len - 1, Player.PLAYER1);
		Throwable exception = assertThrows(PositionNumberException.class, () -> {
			flex.checkThePosition(len - 1, len - 1);
		});
		assertEquals(exception.getMessage(), "You cannot select the position because it is already selected.");
	}

	// plyaer2の場合もいるのか…？
	@Test
	@DisplayName("check to return INPROGRESS for player1")
	void checkReturnInprogress1() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(len);
		WinStatus result = flex.playerSelectPosition(len - 1, len - 1, Player.PLAYER1);
		assertEquals(result, WinStatus.INPROGRESS);
	}

	@Test
	@DisplayName("check to return INPROGRESS for player2")
	void checkReturnInprogress2() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(len);
		flex.playerSelectPosition(len - 1, len - 1, Player.PLAYER1);
		WinStatus result = flex.playerSelectPosition(len - 2, len - 2, Player.PLAYER2);
		assertEquals(result, WinStatus.INPROGRESS);
	}

	@Test
	@DisplayName("check to return Player1")
	void checkReturnPlayer1() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(len);
		List<List<Integer>> positions = positions(len);
		Player player = Player.PLAYER1;
		WinStatus result = WinStatus.INPROGRESS;
		for (List<Integer> position : positions) {
			result = flex.playerSelectPosition(position.get(0), position.get(1), player);
			player = player.next();
			if (result == WinStatus.PLAYER1) {
				break;
			}
		}
		assertEquals(result, WinStatus.PLAYER1);
	}
	@Test
	@DisplayName("check to return Player2")
	void checkReturnPlayer2() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(len);
		List<List<Integer>> positions = positions(len);
		WinStatus result = WinStatus.INPROGRESS;
		for (List<Integer> position : positions) {
			result = flex.playerSelectPosition(0, position.get(1), Player.PLAYER2);
			if (result == WinStatus.PLAYER2) {
				break;
			}
			result = flex.playerSelectPosition(1, position.get(1), Player.PLAYER1);
		}
		assertEquals(result, WinStatus.PLAYER2);
	}
	@Test
	@DisplayName("check to return Draw")
	void checkReturnDraw() throws PositionNumberException, TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(len);
		List<List<Integer>> positions = positions(len);
		// number in the center of the tile
		int center = (len * len / 2) + 1;
		// put the symbol in the center
		flex.playerSelectPosition(positions.get(center).get(0), positions.get(center).get(1), Player.PLAYER1);
		// remove the center position because it is already selected by player1
		positions.remove(center);
		Player player = Player.PLAYER2;
		WinStatus result = WinStatus.INPROGRESS;
		for (List<Integer> position : positions) {
			result = flex.playerSelectPosition(position.get(0), position.get(1), player);
			player = player.next();
		}
		assertEquals(result, WinStatus.DRAW);
	}

	// return all of positions
	List<List<Integer>> positions(int len) {
		List<List<Integer>> positions = new ArrayList<List<Integer>>();
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				positions.add(Arrays.asList(i, j));
			}
		}
		return positions;
	}

//	@Test
//	@DisplayName("check to throw PositionNumberException when the position does not exist")
//	void checkExceptionCheckThePosition1() {
//		Throwable exception = assertThrows(PositionNumberException.class, () -> {
//			flex.checkThePosition(len, len);
//		});
//		assertEquals(exception.getMessage(), "Please put the number under " + len);
//		System.out.println("この　の次？");
//	}
//	
//	@Test
//	@Order(3)
//	@DisplayName("check to throw PositionNumberException when the position is already selected")
//	void checkExceptionCheckThePosition2() {
//		tiles[len-1][len-1] = 'x';
//		Throwable exception = assertThrows(PositionNumberException.class, () -> {
//			flex.checkThePosition(len-1, len-1);
//		});
//		assertEquals(exception.getMessage(), "You cannot select the position because it is already selected.");
//		System.out.println("さらに次?");
//	}
//	
//
//	@Test
//	void testPlayerSelectPosition() {
//		
//		flex.playerSelectPosition(0, 0, Player.PLAYER1);
//		flex.playerSelectPosition(1, 0, Player.PLAYER2);
//		flex.playerSelectPosition(0, 1, Player.PLAYER1);
//		flex.playerSelectPosition(1, 1, Player.PLAYER2);
//		WinStatus result = flex.playerSelectPosition(0, 2, Player.PLAYER1);
//		assertEquals(result, WinStatus.PLAYER1);
//		
//		flex.playerSelectPosition(2, 0, Player.PLAYER1);
//		flex.playerSelectPosition(0, 0, Player.PLAYER2);
//		flex.playerSelectPosition(1, 0, Player.PLAYER1);
//		flex.playerSelectPosition(0, 1, Player.PLAYER2);
//		flex.playerSelectPosition(1, 1, Player.PLAYER1);
//		WinStatus result2 = flex.playerSelectPosition(0, 2, Player.PLAYER2);
//		assertEquals(result2, WinStatus.PLAYER1);
//	}
//	
	// winning listのテスト足りないところないかクリスに確認
	@Test
	@DisplayName("check size of winning condition list")
	void testWinningCondition() throws TilesNumberException {
		FlexibleTicTacToe flex = new FlexibleTicTacToe(len);
		List<List<Character>> winningList = flex.winningCondition();
		assertEquals(winningList.size(), len + len + 2,
				"Expected: " + (len + len + 2) + "Result: " + winningList.size());
	}

//
//	@Test
//	void testCheckWinning() {
//		fail("Not yet implemented");
//	}
//

}
