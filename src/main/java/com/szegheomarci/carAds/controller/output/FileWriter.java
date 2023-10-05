package com.szegheomarci.carAds.controller.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szegheomarci.carAds.model.Car;

import java.io.BufferedWriter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FileWriter extends OutputWriter {
    private String format;
    public FileWriter(String outputconfig) {
        super(outputconfig);
    }

    @Override
    public void generateOutput(ArrayList<Car> results) {
        this.results = results;
        this.format = (String) config.get("format");
        switch (format) {
            case "dsv":
                separatedValuesFile((String) config.get("delimiter"));
                break;
            case "json":
                jsonFile();
                break;
            default:
                return;
        }
    }
    private void separatedValuesFile(String delimiter) {

        BufferedWriter writer = null;
        try {
            //create a temporary file
            String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            File resultFile = new File("./out/" + timeLog + ".txt");

            // This will output the full path where the file will be written to...
            System.out.println(resultFile.getCanonicalPath());

            writer = new BufferedWriter(new java.io.FileWriter(resultFile));

            for (Car car: results) {
                writer.write(car.toText(delimiter)+"\n");
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

    private  void jsonFile() {
        String json;
        ObjectMapper objectMapper = new ObjectMapper();
        BufferedWriter writer = null;
        try {
            //create a temporary file
            String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            File resultFile = new File("./out/" + timeLog + ".json");

            // This will output the full path where the file will be written to...
            System.out.println(resultFile.getCanonicalPath());

            // Serialize the list of objects to the JSON file
            objectMapper.writeValue(resultFile, results);

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
