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

	/**
	 * Les pi�ces du puzzle
	 */
	private ArrayList<Piece> pieces;
	/**
	 * Le titre du puzzle
	 */
	private String title;
	/**
	 * Toutes les solutions du puzzle
	 */
	private ArrayList<Piece[]> solutions;
	/**
	 * La largeur (ou longueur) du puzzle
	 */
	private int size;
	/**
	 * Le temps que dure la recherche de solutions
	 */
	private long duree;
	/**
	 * Le nombre d'appel recursif
	 */
	private int nbAppel;
	/**
	 * Nombre de piece + position test�
	 */
	private int pieceTry;

	public Pool() {
		pieces = new ArrayList<Piece>();
		title = "";
		solutions = new ArrayList<Piece[]>();
		nbAppel = 0;
		pieceTry = 0;
	}

	public void load(String path) throws PoolSizeException {
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
				// Permet de détecter si c'est un carré
				double sqrt = Math.sqrt(this.pieces.size());
				// Si il n'y a pas de racine entière, la taille n'est pas valide
				if (sqrt % 1 != 0) {
					throw new PoolSizeException("Taille de la liste invalide");
				}
				this.size = (int) sqrt;
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
	 * Fonction r�cursive de backtracking permettant de r�soudre un puzzle
	 * 
	 * @param profondeur
	 *            : La place o� l'on cherche une pi�ce qui pourrait correspondre
	 * @param l
	 *            : La solution courrante
	 */
	private void resolve(int profondeur, ArrayList<Piece> l,
			ArrayList<Piece> listPiece) {
		this.nbAppel++;
		// si la profondeur correspond au nombre de case a place, on a fini
		// (BaseCase)
		if (profondeur == Math.pow(this.size, 2)) {
			this.addSolutions(l);
			System.out.println("TROUVE");
			return;
		}
		// on se trouve sur la premiere ligne
		boolean firstLine = profondeur >= 0 && profondeur <= this.size - 1;
		// on se trouve en fin de ligne
		boolean startLine = profondeur % this.size == 0 && profondeur != 0;

		/* Parcourt de toutes les pieces */
		for (int j = 0; j < listPiece.size(); j++) {

			Piece p = listPiece.get(j);

			/* Si la piece est deja pos�, on prend la suivant */
			if (p.isPose())
				continue;

			/* On fait pivoter la piece 4x */
			for (int i = 0; i < 4; i++) {
				this.pieceTry++;

				p.pivoter();

				boolean estOk = false;

				/*
				 * Si c'est la premiere piece a placer, elle correspond
				 * forcement
				 */
				if (profondeur == 0) {
					estOk = true;
				} else

				/*
				 * Si on est a la premiere ligne, et pas en fin de ligne; Il
				 * faut chercher un piece qui match uniquement avec la droite de
				 * la piece courante
				 */
				if (firstLine && !startLine) {
					if (l.get(profondeur - 1).getRight() + p.getLeft() == 0) {
						estOk = true;
					}
				} else

				/*
				 * Si on est en fin de ligne, il faut checher une piece qui
				 * match uniquement avec le bas de la piece du d�but de la ligne
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
					resolve(profondeur + 1, l, listPiece);
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

	public static ArrayList<Piece> duplique(ArrayList<Piece> l) {
		ArrayList<Piece> newList = new ArrayList<Piece>();
		for (Piece it : l) {
			newList.add(it.duplique());
		}
		return newList;
	}

	public void resolveHelper() {
		ArrayList<Thread> listThread = new ArrayList<Thread>();
		
		// Pour toutes les pieces
		for (int j = 0; j < this.pieces.size(); j++) {
			// Duplique les liste
			final int place = j;
			final ArrayList<Piece> lp = this.pieces;
			Thread t = new Thread() {
				public void run() {
					ArrayList<Piece> listPiece = Pool.duplique(lp);
					Piece p = listPiece.get(place);
					p.prendre();
					ArrayList<Piece> l = new ArrayList<Piece>();
					l.add(p);
					for (int i = 0; i < 4; i++) {
						resolve(1, l, listPiece);
						p.pivoter();
					}
				}
			};
			t.start();
			listThread.add(t);
		}
		for (Thread t : listThread) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isPerfect() {
		long start = System.currentTimeMillis();
//		resolve(0, new ArrayList<Piece>(), this.pieces);
		resolveHelper();
		this.duree = System.currentTimeMillis() - start;
//		int j = 1;
//		for (Piece[] s : this.solutions) {
//			System.out.println("------------SOLUTION N�" + j + "------------");
//			for (int i = 0; i < s.length; i++) {
//				System.out.println(s[i]);
//			}
//			System.out.println("------------END SOLUTION------------");
//			j++;
//		}
		System.out.println("Nombre de solutions :" + this.solutions.size());
		System.out.println("Dur�e d'�x�cution : " + this.duree + " ms");
		System.out.println("Nombre d'appels r�cursif : " + this.nbAppel);
		System.out.println("Nombre de configuration essay�es : "
				+ this.pieceTry);
		return true;
	}

	public ArrayList<Piece[]> getSolutions() {
		return solutions;
	}

	public void setSolutions(ArrayList<Piece[]> solutions) {
		this.solutions = solutions;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
