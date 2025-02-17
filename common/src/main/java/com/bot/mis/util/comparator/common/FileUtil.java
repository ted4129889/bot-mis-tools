/* (C) 2025 */
package com.bot.mis.util.comparator.common;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtil {

    /** 讀取指定檔案內容 */
    public static String readFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    /** 取得檔案名稱 */
    public static List<String> getListFiles(String filePath) {
        File folder = new File(filePath);
        // 取得檔案清單
        return Arrays.stream(folder.listFiles())
                .filter(File::isFile)
                .map(File::getName)
                .collect(Collectors.toList());
    }

    /** 讀取 BIG5 */
    public static String readBig5File(String filePath) {
        StringBuilder content = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(filePath);
                InputStreamReader isr = new InputStreamReader(fis, "BIG5");
                BufferedReader br = new BufferedReader(isr)) {

            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    public static byte[] convertToBytes(String content) {
        if (content == null || content.isEmpty()) {
            return new byte[0];
        }

        return content.getBytes(Charset.forName("BIG5"));
    }
}
