package com.luzhu.truck.service.jasper;

import jakarta.annotation.PostConstruct;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.extensions.ExtensionsEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

@Service
public class JasperService {
    private static final Logger log = LoggerFactory.getLogger(JasperService.class);
    private static final String CHINESE_FONT_PATH = "/fonts/STSong.TTF";

    @PostConstruct
    public void verifyChineseFont() {
        if (getClass().getResourceAsStream(CHINESE_FONT_PATH) == null) {
            throw new IllegalStateException("Chinese font not found on classpath: " + CHINESE_FONT_PATH);
        }
        List<FontFamily> fontFamilies = ExtensionsEnvironment.getExtensionsRegistry().getExtensions(FontFamily.class);
        boolean registered = fontFamilies.stream().anyMatch(f -> "STSong".equals(f.getName()));
        if (!registered) {
            log.warn("JasperReports font extension 'STSong' not registered. Available: {}",
                    fontFamilies.stream().map(FontFamily::getName).toList());
        }
    }

    public void exportReportToPdf(OutputStream outputStream, List<?> data, HashMap<String, Object> staticColumn, String jasperTemplatePath) throws Exception {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        try {
            JasperPrint jasperPrint = getPDF(data, staticColumn, jasperTemplatePath);
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        } finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    private JasperPrint getPDF(List<?> dynamicList, HashMap<String, Object> columnMap, String templatePath) throws JRException {
        JasperReport report = compileReport(templatePath);
        return JasperFillManager.fillReport(report, columnMap, new JRBeanCollectionDataSource(dynamicList));
    }

    private JasperReport compileReport(String templatePath) throws JRException {
        if (templatePath.startsWith("classpath:")) {
            String resourcePath = templatePath.substring("classpath:".length());
            if (!resourcePath.startsWith("/")) {
                resourcePath = "/" + resourcePath;
            }
            InputStream inputStream = getClass().getResourceAsStream(resourcePath);
            if (inputStream == null) {
                throw new JRException("Jasper template not found: " + templatePath);
            }
            return JasperCompileManager.compileReport(inputStream);
        }
        return JasperCompileManager.compileReport(templatePath);
    }
}
