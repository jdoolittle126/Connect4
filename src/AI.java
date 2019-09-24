import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class AI {
	int rewards;
	float avg;
	boolean f = true;
	HashMap<Memory, Integer> games;
	String name;
	
	public AI(String name) {
		this.name = name;
		this.rewards = 0;
		this.avg = 0f;
		this.games = new HashMap<Memory, Integer>();
	}
	
	public void add_game(Memory m, int r) {
		this.games.put(m, r);
		rewards+=r;
		if(f) {
			f = false;
			avg = r;
		} else {
			avg = (avg+r)/2;
		}
	}
	
	public int mem_size() {
		return games.size();
	}
	
	public float get_average() {
		return avg;
	}
	
	public Coordinate calc_choice(int[][] b, boolean[][] o, boolean[][] c) {
		ArrayList<int[][]> prior = new ArrayList<int[][]>();
		ArrayList<int[][]> options = new ArrayList<int[][]>();
		ArrayList<Integer> values = new ArrayList<Integer>();
		if(games.isEmpty()) return pick_random(c);
		
		for(Memory m : games.keySet()) {
			int frame_number = m.contains_frame(b);
			if(frame_number != -1) {
				prior.add(m.get_frame(frame_number));
				options.add(m.get_frame(frame_number+1));
				values.add(games.get(m));
			}
		}
		
		ArrayList<int[][]> options_revised = new ArrayList<int[][]>();
		ArrayList<Integer> values_edited = new ArrayList<Integer>(); 
		
		Iterator<Integer> it = values.iterator();
		while(it.hasNext()) {
			values_edited.add((Integer) it.next().intValue());
		}
		while(!values_edited.isEmpty()) {
			int s = values_edited.get(0);
			for(Integer x : values_edited) {
				if(x > s) s = x;
			}
			values_edited.remove((Integer) s);
			if(s > 0) options_revised.add(options.get(values.indexOf(s)));
		}
		
		if(options.isEmpty()) {
			
			ArrayList<int[][]> pos_past = new ArrayList<int[][]>();
			ArrayList<int[][]> pos_future = new ArrayList<int[][]>();
			ArrayList<Integer> pos_values = new ArrayList<Integer>();
			
			for(Memory m : games.keySet()) {
				
				for(int[][] g : m.get_frames()) {
					if(Arrays.deepEquals(c, Game.get_playable_slots(Game.get_open_slots(g)))) {
						if(!Game.check_win(g, true) && !Game.check_win(g, false)) {
							pos_past.add(m.get_frame(m.contains_frame(g)));
							pos_future.add(m.get_frame(m.contains_frame(g)+1));
							pos_values.add(games.get(m));
						}
					}
				}
			}
			
			if(pos_past.isEmpty()) return pick_random(c);
			
			int start = 0;
			int pick = 0;
			for(Integer i : pos_values) {
				if(start < i) pick = pos_values.indexOf(i);
			}
			System.out.println(Arrays.deepToString(pos_past.get(pick)));
			System.out.println(Arrays.deepToString(pos_future.get(pick)));
			return compare(pos_past.get(pick), pos_future.get(pick));
		}
		
		if(options_revised.isEmpty()) return pick_random(c);
		int index = options.indexOf(options_revised.get(0));
		return compare(prior.get(index), options.get(index));
		
	}
	
	public Coordinate compare(int[][] b1, int[][] b2) {
		for (int x = Game.BOARD_WIDTH - 1; x >= 0; x--) {
			for (int y = Game.BOARD_HEIGHT - 1; y >= 0; y--) {
				if(b1[y][x] - b2[y][x] != 0) return new Coordinate(x, y);
			}
		}
		System.out.println("ERROR!");
		return null;
	}
	
	public Coordinate pick_random(boolean[][] c) {
		ArrayList<Coordinate> options = new ArrayList<Coordinate>();
		Random rand = new Random();
		
		for (int x = Game.BOARD_WIDTH - 1; x >= 0; x--) {
			for (int y = Game.BOARD_HEIGHT - 1; y >= 0; y--) {
				if(c[y][x]) options.add(new Coordinate(x, y));
			}
		}
		
		return options.get(rand.nextInt(options.size()));
		
	}
	

}
