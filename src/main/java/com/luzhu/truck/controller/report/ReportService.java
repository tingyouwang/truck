package com.luzhu.truck.controller.report;

import com.luzhu.truck.dto.bill.MonthBillDetailReq;
import com.luzhu.truck.dto.bill.MonthsBillDetailResponse;
import com.luzhu.truck.service.jasper.JasperService;
import com.luzhu.truck.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;

@Service
public class ReportService {
    @Value("${env.time.offset}")
    private String timeOffset;
    @Value("${jasper.path.billdetail}")
    private String jasperTemplatePath;
    @Autowired
    private JasperService jasperService;
    public void billDetailPDF(OutputStream outputStream, MonthsBillDetailResponse billDetail, MonthBillDetailReq req) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("ownerName", req.getOwnerName());
        map.put("carNum", req.getCarLicenseNum());
        map.put("printDate", DateTimeUtil.parseToMinguoDate(LocalDate.now(ZoneOffset.ofHours(Integer.parseInt(timeOffset)))));
        map.put("sum", billDetail.getSum());
        map.put("receiveSum", billDetail.getReceiveSum());
        map.put("offsetSum", billDetail.getOffsetSum());
        jasperService.exportReportToPdf(outputStream, billDetail.getDetailDtos(), map, jasperTemplatePath);
    }
}
