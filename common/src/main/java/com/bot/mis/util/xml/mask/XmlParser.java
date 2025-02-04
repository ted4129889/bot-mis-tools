/* (C) 2024 */
package com.bot.mis.util.xml.mask;

import com.bot.mis.util.xml.config.SecureXmlMapper;
import com.bot.mis.util.xml.vo.XmlData;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.File;
import java.io.IOException;

public class XmlParser {
    public XmlData parseXmlFile(String xmlFileName) throws IOException {
        XmlMapper xmlMapper = SecureXmlMapper.createXmlMapper();
        File file = new File(xmlFileName);
        return xmlMapper.readValue(file, XmlData.class);
    }
}
