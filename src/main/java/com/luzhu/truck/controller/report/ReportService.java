package com.luzhu.truck.controller.report;

import com.luzhu.truck.dto.bill.MonthBillDetailReq;
import com.luzhu.truck.dto.bill.MonthsBillDetailDto;
import com.luzhu.truck.dto.bill.MonthsBillDetailResponse;
import com.luzhu.truck.dto.car.CarInfo;
import com.luzhu.truck.service.jasper.JasperService;
import com.luzhu.truck.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;

@Service
public class ReportService {
    @Value("${env.time.offset}")
    private String timeOffset;
    @Value("${jasper.path.billdetail}")
    private String jasperTemplatePath;
    @Autowired
    private JasperService jasperService;
    public void billDetailPDF(OutputStream outputStream, MonthsBillDetailResponse billDetail, MonthBillDetailReq req, CarInfo carInfo) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("ownerName", carInfo.getOwnerName());
        map.put("carNum", req.getCarLicenseNum());
        map.put("companyName", carInfo.getCarAgency() != null ? carInfo.getCarAgency() : "");
        map.put("printDate", DateTimeUtil.parseToMinguoDate(LocalDate.now(ZoneOffset.ofHours(Integer.parseInt(timeOffset)))));
        map.put("sum", billDetail.getSum());
        map.put("receiveSum", billDetail.getReceiveSum());
        map.put("offsetSum", billDetail.getOffsetSum());

        List<MonthsBillDetailDto> detailDtos = billDetail.getDetailDtos();
        if (detailDtos.isEmpty()) detailDtos.add(MonthsBillDetailDto.builder()
                .expenseYearMonth("")
                .name("")
                .receiveAmount(0)
                .note("本月尚未產出費用")
                .build());

        jasperService.exportReportToPdf(outputStream, billDetail.getDetailDtos(), map, jasperTemplatePath);
    }
}
