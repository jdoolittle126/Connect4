import java.util.ArrayList;
import java.util.Arrays;

public class Memory {
	ArrayList<int[][]> frames;
	
	public Memory() {
		frames = new ArrayList<int[][]>();
	}
	
	public Memory(Memory x) {
		frames = new ArrayList<int[][]>();
		frames.addAll(x.get_frames());
	}

	public void add_frame(int[][] f) {
		frames.add(f);
	}
	
	public int[][] get_frame(int i){
		return frames.get(i);
	}
	public ArrayList<int[][]> get_frames(){
		return (ArrayList<int[][]>) frames.clone();
	}
	
	public int contains_frame(int[][] f) {

		for(int[][] x : frames) {
			if(Arrays.deepEquals(f, x)) return frames.indexOf(x);
		}
		return -1;
	}

}
