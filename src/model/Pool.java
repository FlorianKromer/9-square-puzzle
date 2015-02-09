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

public class Pool {

	private ArrayList<Piece> pieces;
	private String title;
	private ArrayList<Piece[]> solutions;
	private int size;
	private long duree;

	public Pool() {
		pieces = new ArrayList<Piece>();
		title = "";
		solutions = new ArrayList<Piece[]>();
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
				this.size = (int) Math.sqrt(this.pieces.size());
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
		if (profondeur == Math.pow(this.size, 2)) {
			this.addSolutions(l);
			System.out.println("TROUVE");
			return;
		}
		// on se trouve sur la premiere ligne
		boolean firstLine = profondeur >= 0 && profondeur <= this.size - 1;
		// on se trouve en fin de ligne
		boolean startLine = profondeur % this.size == 0 && profondeur != 0;

		/* La piece trouvé */
		for (int j = 0; j < this.pieces.size(); j++) {

			Piece p = this.pieces.get(j);
			
			if (p.isPose())
				continue;
			
			for (int i = 0; i < 4; i++) {
				p.pivoter();
				
				boolean estOk = false;
				
				if (profondeur == 0) {
					estOk = true;
				} else
				/*
				 * Si on est a la premiere ligne, et pas en fin de ligne; Il
				 * faut chercher un piece qui match uniquement avec la droite de
				 * la piece courrant
				 */
				if (firstLine && !startLine) {

					if (l.get(profondeur - 1).getRight() + p.getLeft() == 0) {
						estOk = true;
					}

				} else
				/*
				 * Si on est en fin de ligne, il faut checher une piece qui
				 * match uniquement avec le bas de la piece du début de la ligne
				 * courrante
				 */
				if (startLine) {

					if (l.get(profondeur - this.size).getBottom() + p.getTop() == 0) {
						estOk = true;
					}

				}
				/*
				 * Sinon, on cherche une piece qui match avec la droite de la
				 * piece courrant et le bas de la piece en dessous de la ligne
				 * du dessous
				 */
				else {

					if (p.getLeft() + l.get(profondeur - 1).getRight() == 0
							&& p.getTop()
									+ l.get(profondeur - this.size).getBottom() == 0) {
						estOk = true;
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
	}

	private void addSolutions(ArrayList<Piece> l) {
		Piece[] solution = new Piece[l.size()];
		for (int i = 0; i < l.size(); i++) {
			solution[i] = l.get(i).duplique();
		}
		this.solutions.add(solution);
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
		long start = System.currentTimeMillis();
		resolve(0, new ArrayList<Piece>());
		this.duree = System.currentTimeMillis() - start;
		int j = 1;
		for(Piece[] s:this.solutions){
			System.out.println("------------SOLUTION N°" + j + "------------");
			for(int i = 0; i < s.length; i++){
				System.out.println(s[i]);
			}
			System.out.println("------------END SOLUTION------------");
			j++;
		}
		
		System.out.println("Durée d'éxécution : " + this.duree + " ms");
		return true;
	}

}
