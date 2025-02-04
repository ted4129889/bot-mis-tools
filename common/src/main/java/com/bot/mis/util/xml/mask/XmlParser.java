/* (C) 2024 */
package com.bot.mis.util.xml.mask;

import com.bot.mis.util.xml.config.SecureXmlMapper;
import com.bot.mis.util.xml.vo.XmlData;
import com.bot.txcontrol.config.logger.ApLogHelper;
import com.bot.txcontrol.eum.LogType;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

public class XmlParser {

    private static final String XML_PATH = "external-config/xml/";

    public XmlData parseXmlFile(String xmlFileName) throws IOException {
        XmlMapper xmlMapper = SecureXmlMapper.createXmlMapper();

        File validXmlPath = new File(XML_PATH);

        /* FORTIFY: The file path is securely controlled and validated */
        File file = new File(xmlFileName);

        if (!file.getCanonicalPath().startsWith(validXmlPath.getCanonicalPath())) {
            throw new SecurityException("Unauthorized path");
        }

        return xmlMapper.readValue(file, XmlData.class);
    }
}
