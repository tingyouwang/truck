package com.luzhu.truck.service.jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

@Service
public class JasperService {
    public void exportReportToPdf(OutputStream outputStream, List<?> data, HashMap<String, Object> staticColumn, String jasperTemplatePath) throws Exception {
        JasperPrint jasperPrint = getPDF(data, staticColumn, jasperTemplatePath);
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
    }

    private JasperPrint getPDF(List<?> dynamicList, HashMap<String, Object> columnMap, String templatePath) throws JRException {
        JasperReport report = JasperCompileManager.compileReport(templatePath);

        // 透過compile過的JasperReport製作JasperPrint
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, columnMap, new JRBeanCollectionDataSource(dynamicList));
        return jasperPrint;
    }
}
