/* (C) 2025 */
package com.bot.mis.util.comparator.common;

import com.bot.mis.util.xml.mask.DataMasker;
import com.bot.mis.util.xml.mask.xmltag.Field;
import com.bot.mis.util.xml.vo.XmlBody;
import com.bot.mis.util.xml.vo.XmlField;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.*;

public class SplitData {

    private static final String OLD_DATA_TYPE = "o";
    private final DataMasker dataMasker;

    public SplitData(DataMasker dataMasker) {
        this.dataMasker = dataMasker;
    }

    public String parseDataToJson(
            String newFileName,
            List<Map<String, List<Field>>> maskXmlList,
            XmlBody outputXmlBody,
            String dataType) {

        List<DataRecord> dataList = new ArrayList<>();
        List<XmlField> outputFieldList = outputXmlBody.getFieldList();
        int totalLength = getTotalLength(outputFieldList);

        // 使用 try-with-resources 避免流過早關閉
        try (BufferedReader br = createBufferedReader(newFileName)) {
            String line;
            int lineNumber = 1;

            while ((line = br.readLine()) != null) {
                if (line.length() != totalLength) {
                    throw new IllegalArgumentException(
                            String.format(
                                    "File: %s, Line: %d - Length error. Expected: %d, Actual: %d",
                                    newFileName, lineNumber, totalLength, line.length()));
                }
                dataList.add(createDataRecord(line, outputFieldList, dataType, maskXmlList));
                lineNumber++;
            }
        } catch (IOException e) {
            throw new RuntimeException(
                    "Error reading file: " + newFileName + " - " + e.getMessage(), e);
        }

        return convertToJson(dataList);
    }

    private BufferedReader createBufferedReader(String filePath) throws IOException {
        return new BufferedReader(new FileReader(filePath));
    }

    private int getTotalLength(List<XmlField> outputFieldList) {
        return outputFieldList.stream().mapToInt(f -> Integer.parseInt(f.getLength().trim())).sum();
    }

    private static String convertToJson(List<DataRecord> dataList) {
        try {
            return new ObjectMapper().writeValueAsString(dataList);
        } catch (IOException e) {
            throw new RuntimeException("ERROR CONVERTING TO JSON : " + e.getMessage(), e);
        }
    }

    private DataRecord createDataRecord(
            String line,
            List<XmlField> outputFieldList,
            String dataType,
            List<Map<String, List<Field>>> maskXmlList) {

        DataRecord record = new DataRecord();
        int startIndex = 0;
        Map<String, Map<String, String>> maskMap = buildMaskMap(maskXmlList);

        for (XmlField xmlField : outputFieldList) {
            int fieldLength = Integer.parseInt(xmlField.getLength().trim());
            String fieldValue = line.substring(startIndex, startIndex + fieldLength).trim();

            if (OLD_DATA_TYPE.equals(dataType)) {
                fieldValue = applyMaskOldData(xmlField, fieldValue, maskMap);
            }

            record.addFieldValue(xmlField.getFieldName(), fieldValue);
            startIndex += fieldLength;
        }

        return record;
    }

    private Map<String, Map<String, String>> buildMaskMap(
            List<Map<String, List<Field>>> maskXmlList) {
        Map<String, Map<String, String>> maskMap = new HashMap<>();

        for (Map<String, List<Field>> map : maskXmlList) {
            for (Map.Entry<String, List<Field>> entry : map.entrySet()) {
                Map<String, String> fieldMap = new HashMap<>();
                for (Field field : entry.getValue()) {
                    fieldMap.put(field.getFieldName(), field.getMaskType());
                }
                maskMap.put(entry.getKey(), fieldMap);
            }
        }
        return maskMap;
    }

    private String applyMaskOldData(
            XmlField xmlField, String fieldValue, Map<String, Map<String, String>> maskMap) {
        Map<String, String> fields = maskMap.get(xmlField.getOTableName());
        if (fields != null && fields.containsKey(xmlField.getOFieldName())) {
            return dataMasker.applyMask(fieldValue, fields.get(xmlField.getOFieldName()));
        }
        return fieldValue;
    }

    public class DataRecord {
        private final Map<String, String> fieldValues = new HashMap<>();

        private void addFieldValue(String fieldName, String value) {
            fieldValues.put(fieldName, value);
        }

        public Map<String, String> getFieldValues() {
            return fieldValues;
        }
    }
}
