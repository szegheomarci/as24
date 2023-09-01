package com.szegheomarci.carAds.controller.output;

import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class GetOutputWriterFactory {
    public OutputWriter getOutputGeneratorFactory(String outputConfig) {
        Yaml yaml = new Yaml();
        Map<String, Object> config = yaml.load(outputConfig);
        String outputType = (String) config.get("type");
        if (outputType == null) {
            return null;
        }
        if (outputType.equalsIgnoreCase("FILE")) {
            return new FileWriter(outputConfig);
        }
        else if (outputType.equalsIgnoreCase("KAFKA")) {
            return new KafkaWriter(outputConfig);
        }
        return null;
    }
}
