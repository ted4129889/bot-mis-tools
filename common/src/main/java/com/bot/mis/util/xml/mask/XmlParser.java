/* (C) 2024 */
package com.bot.mis.util.xml.mask;

import com.bot.mis.util.xml.config.SecureXmlMapper;
import com.bot.mis.util.xml.vo.XmlData;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.File;
import java.io.IOException;

public class XmlParser {

    private static final String XML_PATH = "external-config/xml/";
    private final XmlMapper xmlMapper;

    public XmlParser() {
        this.xmlMapper = SecureXmlMapper.createXmlMapper();
    }

    public XmlData parseXmlFile(String xmlFileName) throws IOException {
        File validXmlPath = new File(XML_PATH);

        /* FORTIFY: The file path is securely controlled and validated */
        File file = new File(xmlFileName);

        if (!isValidPath(file, validXmlPath)) {
            throw new SecurityException("Unauthorized path");
        }

        return xmlMapper.readValue(file, XmlData.class);
    }

    private boolean isValidPath(File file, File validXmlPath) throws IOException {
        return file.getCanonicalPath().startsWith(validXmlPath.getCanonicalPath());
    }
}
