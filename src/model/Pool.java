package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;

import run.Main;

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

	/**
	* Main.SIZE  représente la taille d'un coté de la grille
	*/
	private ArrayList<Piece> resolve(int profondeur, ArrayList<Piece> l){

		//8e piece is always ok
		//Math.pow = elevation de a à la puissance b
		//si la profondeur = l'avant derniere case c'est bon
		if(profondeur == Math.pow(Main.SIZE, 2)-1){
			return l;
		}
		//on se trouve sur la premiere ligne
		boolean firstLine = profondeur>=0 && profondeur<=Main.SIZE-1;
		//on se trouve sur la derniere ligne
		boolean lastLine = profondeur>=Math.pow(Main.SIZE, 2)-Main.SIZE && profondeur<=Math.pow(Main.SIZE, 2)-1;
		//si on est sur la case 2 (en partant de 0) ou 5 pas de check à droite car fin de ligne 
		if(profondeur == 2 || profondeur == 5 ){
			//pas de check à droite
		}
		//si on est sur la 1ere ou last ligne on check à droite uniquement
		else if(firstLine || lastLine ){
			System.out.println("profondeur:"+profondeur+" => first/last line");
			l.get(profondeur).compareRight(l.get(profondeur+1));
		}
		else{
			System.out.println("profondeur:"+profondeur+" => other line");
			//on parcourt les pieces
			for (int i = 0; i < l.size(); i++) {
				//si c'est la piece en cours osef
				if (i == profondeur) {
					continue;
				}
				//la valeur abs des nombres correspondent
				boolean compareRight = l.get(profondeur).compareRight(l.get(i));
				boolean compareTop = l.get(profondeur).compareTop(l.get(i));
				boolean compareBottom = l.get(profondeur).compareBottom(l.get(i));
				if(  (compareBottom && compareRight && compareTop) ){
					//System.out.println("DEBUG: retour piece precedante");
					//return resolve(--profondeur, l);
					//on échange la piece suivante avec une qui match
					Collections.swap(l, ++profondeur, i);

				}
			}

		}
		System.out.println("DEBUG: avance piece");
		return resolve(++profondeur, l);

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
		System.out.println(resolve(0, pieces));
		return false;
	}



}
