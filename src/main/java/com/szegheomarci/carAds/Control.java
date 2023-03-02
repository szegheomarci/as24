package com.szegheomarci.carAds;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Control {
	private static ArrayList<Car> autoScoutResults;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AutoScoutLister();
		exportToTextFile();
	}
	
	private static void AutoScoutLister(){
		AutoscoutLister as = new AutoscoutLister();
		autoScoutResults = new ArrayList<Car>();
		autoScoutResults = as.Lister();
	}
	
	private static void exportToTextFile(){
		BufferedWriter writer = null;
	    try {
	        //create a temporary file
	        String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	        File logFile = new File("./" + timeLog + "as");

	        // This will output the full path where the file will be written to...
	        System.out.println(logFile.getCanonicalPath());

	        writer = new BufferedWriter(new FileWriter(logFile));
	        //writer.write("Hello world!");
	        
	        for (Car car: autoScoutResults) {
	        	writer.write(car.toText()+"\n");
	        }
	        
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            // Close the writer regardless of what happens...
	            writer.close();
	        } catch (Exception e) {
	        }
	    }
	}

}
