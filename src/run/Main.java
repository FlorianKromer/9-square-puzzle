package run;

import java.io.File;
import java.util.Scanner;

import model.Pool;
import model.PoolSizeException;

public class Main {

	public static void main(String[] args) {
//		System.out
//				.println(""
//						+
//
//						"______            _  _____              _    _             "
//						+ "\n"
//						+ "| ___ \\          | ||_   _|            | |  (_)            "
//						+ "\n"
//						+ "| |_/ / __ _  ___| | _| |_ __ __ _  ___| | ___ _ __   __ _ "
//						+ "\n"
//						+ "| ___ \\/ _` |/ __| |/ / | '__/ _` |/ __| |/ / | '_ \\ / _` |"
//						+ "\n"
//						+ "| |_/ / (_| | (__|   <| | | | (_| | (__|   <| | | | | (_| |"
//						+ "\n"
//						+ "\\____/ \\__,_|\\___|_|\\_\\_/_|  \\__,_|\\___|_|\\_\\_|_| |_|\\__, |"
//						+ "\n"
//						+ "                                                      __/ |"
//						+ "\n"
//						+ "                                                     |___/ "
//						+ "\n");

		String dataPath = null;
		if (args.length > 0 && (new File(args[0])).exists()) {
			dataPath = args[0];
		} else {
			boolean stopAsking = false;
			Scanner s = new Scanner(System.in);
			while (!stopAsking) {
				System.out
						.print("Veuillez saisir le nom du fichier de donn�es (ou q pour quitter) -> ");
				String input = s.nextLine();
				if (input.equals("q")) {
					System.exit(0);
				} else {
					File f = new File(input);
					stopAsking = (f.exists() && f.isFile());
					dataPath = input;
					System.out.println();
				}
			}
			s.close();
		}

		Pool puzzle = new Pool();
		try {
			puzzle.load(dataPath);
		} catch (PoolSizeException e) {
			e.printStackTrace();
		}
		puzzle.isPerfect();
		// System.out.println(String.format("Le puzzle \"%s\" (%s) %s",
		// puzzle.getTitle(), dataPath,
		// puzzle.isPerfect() ? "est parfait." : "n'est pas parfait."));
		//
		// System.out.println(String.format("Il contient les %d pi�ces suivantes :",
		// puzzle.getPieces().size()));
		// for (Piece p : puzzle.getPieces()) {
		// System.out.println(p);
		//
		// }

	}
}
