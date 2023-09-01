package com.szegheomarci.carAds.controller.datasource;

import com.szegheomarci.carAds.model.Car;

import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.Map;

public abstract class DatasourceReader {
    protected Yaml yaml = new Yaml();
    protected String config;
    protected Map<String, Object> datasource;
    protected ArrayList<Car> results = new ArrayList<>();
    public DatasourceReader(String datasourceConfig){
        this.config = datasourceConfig;
        datasource = yaml.load(config);
    }
    public abstract void readSource();
    public ArrayList<Car> getResults() {
        return this.results;
    }
}
