package com.szegheomarci.carAds;

import com.szegheomarci.carAds.controller.datasource.DatasourceReader;
import com.szegheomarci.carAds.controller.output.OutputWriter;
import com.szegheomarci.carAds.model.Car;

import java.io.InputStream;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class CarAds {

    public static void main(String[] args) {
		if (args.length > 0 && args[0].equalsIgnoreCase("version")) {
			String version = getVersionFromPom();
			System.out.println("Version: " + version);
			return;
		}

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

	private static String getVersionFromPom() {
		try (InputStream input = CarAds.class.getClassLoader().getResourceAsStream("project.properties")) {
			Properties properties = new Properties();
			properties.load(input);
			return properties.getProperty("version");
		} catch (Exception e) {
			e.printStackTrace();
			return "Version not available";
		}
	}

}
