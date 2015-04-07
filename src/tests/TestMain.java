package tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import model.Pool;
import model.PoolSizeException;

import org.junit.Test;

public class TestMain {
	public final static String FileName="data9.txt";
	
	@Test
	public void test() {
		File f = new File("average.txt");
		f.delete();
		for (int i = 0; i < 50; i++) {
			Pool puzzle = new Pool();
			try {
				puzzle.load(FileName);
			} catch (PoolSizeException e) {
				e.printStackTrace();
			}
			puzzle.isPerfect();			
		}
		ArrayList<Integer> tabAvg = new ArrayList<>();
		
		try(BufferedReader br = new BufferedReader(new FileReader("average.txt"))) {
		    for(String line; (line = br.readLine()) != null; ) {
		        tabAvg.add(Integer.parseInt(line));
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		double avg = 0;
		for (Integer integer : tabAvg) {
			avg += integer;
		}
		System.out.println(avg/tabAvg.size() + "ms");

	}

}
