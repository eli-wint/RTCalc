package main.java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SaveIO {
	private static File saveFile = new File("src/main/resources/saves/save.txt");
	
	public SaveIO() {}
	
	public static void boot() {
		if (!saveFile.getParentFile().exists()) {
			saveFile.getParentFile().mkdirs();
		}
	}
	
	public static double[] load() {
		double[] result = new double[3];
		int count = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(saveFile));
			
			String line;
			
			while((line = br.readLine()) != null && !line.isEmpty()) {
				result[count] = Double.parseDouble(line);
				count++;
			}
		} catch (FileNotFoundException e) {
			System.out.println("NO FILE :(");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void save(double[] values) {
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile))) {
	        for (double value : values) {
	            bw.write(String.format("%.2f", value));
	            bw.newLine();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
