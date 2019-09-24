import java.lang.reflect.Array;
import java.util.Arrays;

public class Game {

	static final int BOARD_WIDTH = 7, BOARD_HEIGHT = 6;
	static int p1w=0, p2w=0;
	static AI p1 = new AI("Player 1");
	static AI p2 = new AI("Player 2");

	public static void main(String[] args) {
		
		int game_count = 2;
		
		for(int i=0; i<game_count;i++) {
			System.out.println(" ---- GAME " + i + " ---- ");
			game();
		}
		System.out.println("Player One Wins: " + p1w + " AVG: " + p1.get_average() + "\nPlayer Two Wins: " + p2w + " AVG: " + p2.get_average() + "\nTies: " + (game_count-(p1w+p2w)));
		System.out.println(p1.mem_size() + " " + p2.mem_size());
		
	}
	
	public static void game() {
		int[][] board = new int[BOARD_HEIGHT][BOARD_WIDTH];
		int round = 1;
		boolean game = true;
		boolean[][] open;
		boolean moves;
		Memory mem_p1 = new Memory(), mem_p2 = new Memory();
		while(game) {
			open = get_open_slots(board);
			Coordinate p1_choice = p1.calc_choice(board, open, get_playable_slots(open));
			board = update_board(board, true, p1_choice.x, p1_choice.y);
			mem_p1.add_frame(deepCopyOf(board));
			draw_board(board, round, true);
			
			if(check_win(board, true)) {
				p1w++;
				p1.add_game(mem_p1, 25-round);
				p2.add_game(mem_p2, round-25);
				game = false;
				break;
			}
			open = get_open_slots(board);
			Coordinate p2_choice = p2.calc_choice(board, open, get_playable_slots(open));
			board = update_board(board, false, p2_choice.x, p2_choice.y);
			mem_p2.add_frame(board);
			draw_board(board, round, false);
			
			if(check_win(board, false)) {
				p2w++;
				p1.add_game(mem_p1, round-25);
				p2.add_game(mem_p2, 25-round);
				game = false;
				break;
			}
			
			moves = false;
			for(boolean[] r : get_open_slots(board)) {
				for(boolean v : r) {
					if(v) moves = true;
				}
			}
			game = moves;
			
			round++;
		}
		System.out.println("P1 AVG: " + p1.avg);
		System.out.println("P2 AVG: " + p2.avg);
		System.out.println(" ---- GAME OVER ---- ");
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] deepCopyOf(T[] array) {

	    if (0 >= array.length) return array;

	    return (T[]) deepCopyOf(
	            array, 
	            Array.newInstance(array[0].getClass(), array.length), 
	            0);
	}

	private static Object deepCopyOf(Object array, Object copiedArray, int index) {

	    if (index >= Array.getLength(array)) return copiedArray;

	    Object element = Array.get(array, index);

	    if (element.getClass().isArray()) {

	        Array.set(copiedArray, index, deepCopyOf(
	                element,
	                Array.newInstance(
	                        element.getClass().getComponentType(),
	                        Array.getLength(element)),
	                0));

	    } else {

	        Array.set(copiedArray, index, element);
	    }

	    return deepCopyOf(array, copiedArray, ++index);
	}
	
	public static boolean[][] get_open_slots(int[][] b) {
		boolean[][] o = new boolean[BOARD_HEIGHT][BOARD_WIDTH];
		for (int x = BOARD_WIDTH - 1; x >= 0; x--) {
			for (int y = BOARD_HEIGHT - 1; y >= 0; y--) {
				if (b[y][x] == 0)
					o[y][x] = true;
			}
		}
		return o;
	}
	public static boolean[][] get_playable_slots(boolean[][] open) {
		boolean[][] p = new boolean[BOARD_HEIGHT][BOARD_WIDTH];
		for (int x = BOARD_WIDTH - 1; x >= 0; x--) {
			for (int y = BOARD_HEIGHT - 1; y >= 0; y--) {
				if (open[y][x]) {
					if (y + 1 < BOARD_HEIGHT) {
						if (!open[y + 1][x])
							p[y][x] = true;
					} else {
						p[y][x] = true;
					}
				}
			}
		}
		return p;
	}
	public static int[][] update_board(int[][] b, boolean t, int x, int y) {
		int v = (t) ? 1 : 2;
		b[y][x] = v;
		return b;
	}
	public static boolean check_win(int[][] b, boolean t) {
		int p = (t) ? 1 : 2;

		for (int j = 0; j < BOARD_HEIGHT - 3; j++) {
			for (int i = 0; i < BOARD_WIDTH; i++) {
				if (b[j][i] == p && b[j + 1][i] == p && b[j + 2][i] == p && b[j + 3][i] == p)
					return true;
			}
		}

		for (int i = 0; i < BOARD_WIDTH - 3; i++) {
			for (int j = 0; j < BOARD_HEIGHT ; j++) {
				if (b[j][i] == p && b[j][i+1] == p && b[j][i+2] == p && b[j][i + 3] == p) {
					return true;
				}
			}
		}

		for (int i = 3; i < BOARD_WIDTH; i++) {
			for (int j = 0; j < BOARD_HEIGHT - 3; j++) {
				if (b[j][i] == p && b[j + 1][i - 1] == p && b[j + 2][i - 2] == p && b[j + 3][i - 3] == p)
					return true;
			}
		}

		for (int i = 3; i < BOARD_WIDTH; i++) {
			for (int j = 3; j < BOARD_HEIGHT; j++) {
				if (b[j][i] == p && b[j - 1][i - 1] == p && b[j - 2][i - 2] == p && b[j - 3][i - 3] == p)
					return true;
			}
		}
		return false;

	}
	public static void draw_board(int[][] b, int n, boolean t) {
		System.out.println("Round: " + n + "  ");
		if (t)
			System.out.print("Player 1's Turn");
		else
			System.out.print("Player 2's Turn");
		for (int[] r : b) {
			System.out.print("\n| ");
			for (int v : r) {
				System.out.print(v + " ");
			}
			System.out.print("|");
		}
		System.out.println("\n-----------------");
	}

	
}
