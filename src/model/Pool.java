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

import run.Main;

public class Pool {

	private ArrayList<Piece> pieces;
	private String title;
	private ArrayList<ArrayList<Piece>> solutions;

	public Pool() {
		pieces = new ArrayList<Piece>();
		title = "";
		solutions = new ArrayList<ArrayList<Piece>>();
	}

	public void load(String path) {
		LineNumberReader reader = null;
		try {
			reader = new LineNumberReader(new InputStreamReader(
					new FileInputStream(new File(path))));
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
				Piece piece = new Piece("" + result.group(1).charAt(0));
				int direction = Piece.TOP;
				for (int i = 2; i <= result.groupCount(); i++) {
					piece.setValueAt(direction,
							Integer.parseInt(result.group(i)));
					direction++;
				}
				loadedPieces.add(piece);
			}
			s.close();
		}

		Pool result = new Pool();

		for (Piece p : loadedPieces) {
			result.addPiece(p);
		}
		return result;
	}

	/**
	 * Main.SIZE représente la taille d'un coté de la grille
	 */
	private void resolve(int profondeur, ArrayList<Piece> l) {
		// si la profondeur = l'avant derniere case c'est bon
		if (profondeur == Math.pow(Main.SIZE, 2)) {
			this.solutions.add((ArrayList<Piece>) l.clone());
			for(Piece pp:l){
				System.out.println(pp);
			}
			System.out.println("TROUVE");
			return;
		}
		// on se trouve sur la premiere ligne
		boolean firstLine = profondeur >= 0 && profondeur <= Main.SIZE - 1;
		// on se trouve sur la derniere ligne
		// boolean lastLine = profondeur >= Math.pow(Main.SIZE, 2) - Main.SIZE
		// && profondeur <= Math.pow(Main.SIZE, 2) - 1;
		// on se trouve en fin de ligne
		boolean startLine = profondeur % Main.SIZE == 0 && profondeur != 0;

		/* La piece trouvé */
		for (int j = 0; j < this.pieces.size(); j++) {
			boolean estOk = false;

			if (this.pieces.get(j).isPose())
				continue;

			Piece p = this.pieces.get(j);
			if (profondeur == 0) {
				estOk = true;
			} else
			/*
			 * Si on est a la premiere ligne, et pas en fin de ligne; Il faut
			 * chercher un piece qui match uniquement avec la droite de la piece
			 * courrant
			 */
			if (firstLine && !startLine) {
				for (int i = 0; i < 4; i++) {
					p = p.pivoter();
					if (l.get(profondeur - 1).getRight() + p.getLeft() == 0) {
						estOk = true;
						break;
					}
				}
			} else
			/*
			 * Si on est en fin de ligne, il faut checher une piece qui match
			 * uniquement avec le bas de la piece du début de la ligne courrante
			 */
			if (startLine) {
				for (int i = 0; i < 4; i++) {
					p = p.pivoter();
					if (l.get(profondeur - Main.SIZE).getBottom() + p.getTop() == 0) {
						estOk = true;
						break;
					}
				}
			}
			/*
			 * Sinon, on cherche une piece qui match avec la droite de la piece
			 * courrant et le bas de la piece en dessous de la ligne du dessous
			 */
			else {
				for (int i = 0; i < 4; i++) {
					p = p.pivoter();
					if (p.getLeft() + l.get(profondeur - 1).getRight() == 0
							&& p.getTop()
									+ l.get(profondeur - Main.SIZE).getBottom() == 0) {
						estOk = true;
						break;
					}
				}
			}
			
			if (estOk) {
				p.prendre();
				l.add(p);
				resolve(profondeur + 1, l);
				l.remove(profondeur);
				p.retirer();
			}
		}
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
		resolve(0, new ArrayList<Piece>());
		System.out.println(this.solutions.size());
		for (Piece pp:this.solutions.get(0)){
			System.out.println(pp);
		}
		return false;
	}

}
