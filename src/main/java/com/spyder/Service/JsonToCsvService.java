package com.spyder.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonToCsvService {

    public File jsonToFileConverter(String jsonString, String jsonObjectName, String delimiterInput,
            String outputFilePath, Boolean hasHeader)
            throws JsonProcessingException, IOException {
        char delimiter = delimiterInput.equalsIgnoreCase("tab") ? '\t' : delimiterInput.charAt(0);

        // Parse JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonTree = objectMapper.readTree(jsonString);

        if (!jsonObjectName.isEmpty()) {
            jsonTree = jsonTree.get(jsonObjectName);
        }

        // Create file writer
        File outputFile = new File(outputFilePath);
        // File outputFile = Paths.get("C:\\transfer\\", outputFilePath).toFile();
        try (FileWriter fileWriter = new FileWriter(outputFile)) {

            if (hasHeader) {
                // Write headers
                JsonNode firstObject = jsonTree.elements().next();
                Iterator<String> fieldNames = firstObject.fieldNames();
                while (fieldNames.hasNext()) {
                    fileWriter.append(fieldNames.next());
                    if (fieldNames.hasNext()) {
                        fileWriter.append(delimiter);
                    }
                }
                fileWriter.append('\n');
            }

            // Write data
            for (JsonNode jsonNode : jsonTree) {
                Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> field = fields.next();
                    fileWriter.append(field.getValue().asText());
                    if (fields.hasNext()) {
                        fileWriter.append(delimiter);
                    }
                }
                fileWriter.append('\n');
            }
        }

        return outputFile;
    }

}
