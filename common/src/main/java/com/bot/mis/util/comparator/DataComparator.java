/* (C) 2025 */
package com.bot.mis.util.comparator;

import com.bot.mis.util.comparator.common.DatabaseUtil;
import com.bot.mis.util.comparator.common.FileUtil;
import com.bot.mis.util.comparator.common.SplitData;
import com.bot.mis.util.xml.mask.DataMasker;
import com.bot.mis.util.xml.mask.XmlParser;
import com.bot.mis.util.xml.mask.xmltag.Field;
import com.bot.mis.util.xml.vo.XmlBody;
import com.bot.mis.util.xml.vo.XmlData;
import com.bot.txcontrol.config.logger.ApLogHelper;
import com.bot.txcontrol.eum.LogType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope("prototype")
public class DataComparator {

    @Value("${localFile.mis.xml.mask.directory}")
    private String maskXmlFilePath;

    @Value("${localFile.mis.xml.output.directory}")
    private String outputXmlFilePath;

    private static final String OLD_DATA_TYPE = "O";
    private static final String NEW_DATA_TYPE = "N";

    private final DataMasker dataMasker = new DataMasker();

    public void executeCompare(String newTxtPath, String oldTxtPath) throws IOException {
        List<String> newFileNameList = FileUtil.getListFiles(newTxtPath);
        List<String> oldFileNameList = FileUtil.getListFiles(oldTxtPath);
        List<Map<String, List<Field>>> maskXmlList =
                getMaskXmlTableNameAndFieldList(maskXmlFilePath);

        SplitData splitData = new SplitData(dataMasker);

        // new Data
        parseAndInsert(NEW_DATA_TYPE, newFileNameList, splitData, maskXmlList);
        ApLogHelper.info(log, false, LogType.NORMAL.getCode(), "insert new data success.");

        // old Data
        parseAndInsert(OLD_DATA_TYPE, oldFileNameList, splitData, maskXmlList);
        ApLogHelper.info(log, false, LogType.NORMAL.getCode(), "insert old data success.");

        // compare and output CSV
        //        compareAndOutputCSV(newFileNameList);
    }

    private void compareAndOutputCSV(List<String> fileNameList) {
        for (String fileName : fileNameList) {
            try {
                // 執行比較
                List<String> diffLines = compareData(fileName);

                // 輸出 CSV 檔案
                outputCsv(fileName, diffLines);

            } catch (Exception e) {
                System.err.println("處理檔案 " + fileName + " 時發生錯誤: " + e.getMessage());
            }
        }
    }

    private List<String> compareData(String fileName) {
        List<String> diffLines = new ArrayList<>();

        Map<String, String> newDataMap = DatabaseUtil.getData(fileName, NEW_DATA_TYPE);
        Map<String, String> oldDataMap = DatabaseUtil.getData(fileName, OLD_DATA_TYPE);

        if (newDataMap == null || oldDataMap == null) {
            throw new RuntimeException("未找到符合條件的數據: " + fileName);
        }

        String dataKey = newDataMap.get("DATA_KEY");
        if (dataKey == null || dataKey.isEmpty()) {
            throw new RuntimeException("未找到 dataKey: " + fileName);
        }

        List<Map<String, Object>> newDataList = parseJsonToList(newDataMap.get("DATA_VALUE"));
        List<Map<String, Object>> oldDataList = parseJsonToList(oldDataMap.get("DATA_VALUE"));
        Map<Object, Map<String, Object>> oldDataIndex = buildIndex(oldDataList, dataKey);

        for (Map<String, Object> newRecord : newDataList) {
            Object keyValue = newRecord.get(dataKey);
            if (keyValue == null) continue;
            Map<String, Object> oldRecord = oldDataIndex.get(keyValue);
            if (oldRecord == null) continue;

            for (String fieldName : newRecord.keySet()) {
                Object newValue = newRecord.get(fieldName);
                Object oldValue = oldRecord.get(fieldName);
                if (!Objects.equals(newValue, oldValue)) {
                    String diffLine =
                            String.format(
                                    "%s,%s,%s",
                                    fieldName,
                                    newValue != null ? newValue.toString() : "",
                                    oldValue != null ? oldValue.toString() : "");
                    diffLines.add(diffLine);
                }
            }
        }

        return diffLines;
    }

    private void outputCsv(String fileName, List<String> diffLines) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName + "_Comparison.csv"))) {
            writer.println("Field,New Value,Old Value");

            for (String diffLine : diffLines) {
                writer.println(diffLine);
            }

        } catch (IOException e) {
            throw new RuntimeException("輸出 CSV 檔案時發生錯誤: " + e.getMessage());
        }
    }

    /** 將 JSON 字串解析為 List<Map<String, Object>> */
    private List<Map<String, Object>> parseJsonToList(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            throw new RuntimeException("ERROR PARSING JSON : " + e.getMessage(), e);
        }
    }

    /** 建立索引以便快速查找 OLD 資料 */
    private Map<Object, Map<String, Object>> buildIndex(
            List<Map<String, Object>> dataList, String dataKey) {
        Map<Object, Map<String, Object>> index = new HashMap<>();
        for (Map<String, Object> record : dataList) {
            Object keyValue = record.get(dataKey);
            if (keyValue != null) {
                index.put(keyValue, record);
            }
        }
        return index;
    }

    /** 比較兩筆資料 */
    private void compareRecords(Map<String, Object> newRecord, Map<String, Object> oldRecord) {
        for (String fieldName : newRecord.keySet()) {
            Object newValue = newRecord.get(fieldName);
            Object oldValue = oldRecord.get(fieldName);

            if (!Objects.equals(newValue, oldValue)) {
                System.out.println(
                        "不匹配: 欄位 "
                                + fieldName
                                + " 的值不同 (NEW: "
                                + newValue
                                + ", OLD: "
                                + oldValue
                                + ")");
            } else {
                System.out.println("匹配: 欄位 " + fieldName + " 的值相同 (" + newValue + ")");
            }
        }
    }

    private void parseAndInsert(
            String dataType,
            List<String> fileNameList,
            SplitData splitData,
            List<Map<String, List<Field>>> maskXmlList) {
        try {
            for (String fileName : fileNameList) {
                XmlParser xmlParser = new XmlParser();
                XmlBody outputXmlBody = xmlParser.parseXmlFile(fileName).getBody();
                String dataKey = outputXmlBody.getDataKey();
                String tempJson =
                        splitData.parseDataToJson(fileName, maskXmlList, outputXmlBody, dataType);
                DatabaseUtil.insertData(fileName, dataType, dataKey, tempJson);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Map<String, List<Field>>> getMaskXmlTableNameAndFieldList(String maskXmlPath)
            throws IOException {
        List<Map<String, List<Field>>> maskXmlList = new ArrayList<>();
        XmlParser xmlParser = new XmlParser();

        for (String maskXmlName : FileUtil.getListFiles(maskXmlPath)) {
            String xmlFilePath =
                    maskXmlName.endsWith(".xml")
                            ? maskXmlPath + maskXmlName
                            : maskXmlPath + maskXmlName + ".xml";
            XmlData maskXmlData = xmlParser.parseXmlFile(xmlFilePath);
            Map<String, List<Field>> maskDataMap =
                    Collections.singletonMap(maskXmlName, maskXmlData.getFieldList());
            maskXmlList.add(maskDataMap);
        }

        return maskXmlList;
    }
}
