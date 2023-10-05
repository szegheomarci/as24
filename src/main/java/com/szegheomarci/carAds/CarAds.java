package com.szegheomarci.carAds;

import com.szegheomarci.carAds.controller.datasource.DatasourceReader;
import com.szegheomarci.carAds.controller.output.OutputWriter;
import com.szegheomarci.carAds.model.Car;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class CarAds {
	private static ArrayList<Car> autoScoutResults;

	public static void main(String[] args) {

		// read the configuration
		try {
			AppConfig config = new AppConfig();
			DatasourceReader datasourceReader = config.getDatasourceReader();
			datasourceReader.readSource();
			ArrayList<Car> ads = datasourceReader.getResults();
			for (OutputWriter outputWriter : config.getOutputWriters()) {
				outputWriter.generateOutput(ads);
				TimeUnit.SECONDS.sleep(1);
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("config file not found");
		} catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


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
	        	writer.write(car.toText("####")+"\n");
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
