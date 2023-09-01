package com.szegheomarci.carAds;

import com.szegheomarci.carAds.controller.datasource.DatasourceReader;
import com.szegheomarci.carAds.controller.datasource.GetDatasourceReaderFactory;
import com.szegheomarci.carAds.controller.output.GetOutputWriterFactory;
import com.szegheomarci.carAds.controller.output.OutputWriter;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppConfig {

    //private Map<String, Object> datasource;
    private DatasourceReader datasourceReader;
    //private Map<String, Object> output;
    private ArrayList<OutputWriter> outputWriters = new ArrayList<>();

    public AppConfig() throws FileNotFoundException {
        Yaml yaml = new Yaml();

        FileInputStream file = new FileInputStream("." + File.separator + "config" + File.separator + "config.yaml");
        Map<String, Object> config = yaml.load(file);
        Map<String, Object> datasource = (Map<String, Object>) config.get("datasource");
        String datasourceConfig = yaml.dump(datasource);
        GetDatasourceReaderFactory datasourceReaderFactory = new GetDatasourceReaderFactory();
        this.datasourceReader = datasourceReaderFactory.getDatasourceReader(datasourceConfig);

        List<Map<String, Object>> outputs = (List<Map<String, Object>>) config.get("output");
        for (Map<String, Object> output: outputs) {
            String outputConfig = yaml.dump(output);
            GetOutputWriterFactory outputWriterFactory = new GetOutputWriterFactory();
            OutputWriter outputWriter = outputWriterFactory.getOutputGeneratorFactory(outputConfig);
            outputWriters.add(outputWriter);
        }

    }

    public DatasourceReader getDatasourceReader() {
        return datasourceReader;
    }

    public List<OutputWriter> getOutputWriters() {
        return outputWriters;
    }

}
