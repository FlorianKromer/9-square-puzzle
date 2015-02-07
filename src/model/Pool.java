package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;

public class Pool{

	private ArrayList<Piece> pieces;
	private String title;


	public Pool() {
		pieces = new ArrayList<Piece>();
		title = "";
	}

	public void load(String path) {
		LineNumberReader reader = null;
		try {
			reader = new LineNumberReader(new InputStreamReader(new FileInputStream(new File(path))));
			this.setTitle(reader.readLine());
			this.setPieces(Pool.load(reader));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Pool load(LineNumberReader r) throws IOException {
		List<Piece> loadedPieces = new ArrayList<Piece>();
		String line;
		while ((line = r.readLine()) != null) {
			Scanner s = new Scanner(line);
			if (s.findInLine("(\\S)\\s+(-?\\d+)\\s+(-?\\d+)\\s+(-?\\d+)\\s+(-?\\d+)") != null) {
				MatchResult result = s.match();
				Piece piece = new Piece(""+result.group(1).charAt(0));
				int direction = Piece.TOP;
				for (int i = 2; i <= result.groupCount(); i++) {
					piece.setValueAt(direction, Integer.parseInt(result.group(i)));
					direction++;
				}
				loadedPieces.add(piece);
			}
		}

		Pool result = new Pool();
		
		
		for (Piece p : loadedPieces) {
			result.addPiece(p);
		}
		return result;
	}
	


	private void addPiece(Piece p) {
		pieces.add(p);

	}

	private void setPieces(Pool load) {
		this.pieces = load.pieces;
	}

	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public ArrayList<Piece> getPieces() {
		return pieces;
	}


	public void setPieces(ArrayList<Piece> pieces) {
		this.pieces = pieces;
	}


	public boolean isPerfect() {
		// TODO Auto-generated method stub
		return false;
	}



}
