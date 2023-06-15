package com.szegheomarci.carAds;

import com.szegheomarci.carAds.model.Car;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class Control {
	private static ArrayList<Car> autoScoutResults;

	public static void main(String[] args) {

		// read the configuration
		/*try {
			AppConfig config = new AppConfig();
			List<String> asUrls = config.getAutoscouturls();
			int i = 0;
			while (i < asUrls.size()) {
				String url = asUrls.get(i);
				System.out.println("fetch: " + url);
				//Parser(url,1,"URL[]");
				i++;
			}
		//} catch (FileNotFoundException e) {
		//	System.out.println("config file with urls not found");
		//}*/
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
