package com.szegheomarci.carAds.controller.datasource;

import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class GetDatasourceReaderFactory {
    public DatasourceReader getDatasourceReader(String datasourceConfig) {
        Yaml yaml = new Yaml();
        Map<String, Object> config = yaml.load(datasourceConfig);
        String datasourceReaderType = (String) config.get("reader");
        if (datasourceReaderType == null) {
            return null;
        }
        if (datasourceReaderType.equalsIgnoreCase("AUTOSCOUTREADER")) {
            return new AutoscoutReader(datasourceConfig);
        }
        //else if(datasourcereader.equalsIgnoreCase("MOBILEREADER")){
        //    return new Mobile(datasourceConfig);
        //}
        return null;
    }
}
