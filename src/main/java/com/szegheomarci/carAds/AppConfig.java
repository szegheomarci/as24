package com.szegheomarci.carAds;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class AppConfig {

    private List<String> autoscouturls;

    public AppConfig() throws FileNotFoundException {
        Yaml yaml = new Yaml();
        //InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("." + File.separator + "config" + File.separator + "config.yaml");
        //Map<String, Object> config = yaml.load(inputStream);

        FileInputStream file = new FileInputStream("." + File.separator + "config" + File.separator + "config.yaml");
        Map<String, Object> config = yaml.load(file);

        Map<String, Object> datasource = (Map<String, Object>) config.get("datasource");
        Map<String, Object> autoscout24 = (Map<String, Object>) datasource.get("autoscout24");
        autoscouturls = (List<String>) autoscout24.get("urls");
    }

    public List<String> getAutoscouturls() {
        return autoscouturls;
    }
}
