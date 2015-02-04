import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class Main {

	private static String title;
	
	public static void main(String[] args) {
		System.out.println("" +

"______            _  _____              _    _             "+"\n"+
"| ___ \\          | ||_   _|            | |  (_)            "+"\n"+
"| |_/ / __ _  ___| | _| |_ __ __ _  ___| | ___ _ __   __ _ "+"\n"+
"| ___ \\/ _` |/ __| |/ / | '__/ _` |/ __| |/ / | '_ \\ / _` |"+"\n"+
"| |_/ / (_| | (__|   <| | | | (_| | (__|   <| | | | | (_| |"+"\n"+
"\\____/ \\__,_|\\___|_|\\_\\_/_|  \\__,_|\\___|_|\\_\\_|_| |_|\\__, |"+"\n"+
"                                                      __/ |"+"\n"+
"                                                     |___/ "+"\n");


		Scanner sc = new Scanner(System.in);
		System.out.println("Veuillez saisir le nom d'un fichier: \n");
		String fileName = sc.nextLine();
		
		try(BufferedReader br = new BufferedReader(new FileReader(new File(fileName)))) {
		    for(String line; (line = br.readLine()) != null; ) {
		    	if(title == null)
		    		title = line;
		    	else{
		    		System.out.println(line);
		    	}
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}
	
}
