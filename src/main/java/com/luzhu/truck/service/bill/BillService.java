package com.luzhu.truck.service.bill;

import com.luzhu.truck.dao.Healthfee.HealthFeeDao;
import com.luzhu.truck.dao.car.CarDao;
import com.luzhu.truck.dao.fueltax.FuelTaxDao;
import com.luzhu.truck.dao.givebackmoney.GiveBackMoneyDao;
import com.luzhu.truck.dao.insurancefee.InsuranceFeeDao;
import com.luzhu.truck.dao.invoice.InvoiceDao;
import com.luzhu.truck.dao.laborinsurance.LaborInsuranceDao;
import com.luzhu.truck.dao.lastmonthowe.LastMonthOweDao;
import com.luzhu.truck.dao.lendmoney.LendMoneyDao;
import com.luzhu.truck.dao.licensetax.LicenseTaxDao;
import com.luzhu.truck.dao.loanfee.LoanFeeDao;
import com.luzhu.truck.dao.managefee.ManageFeeDao;
import com.luzhu.truck.dao.othergivebackmoney.OtherGiveBackMoneyDao;
import com.luzhu.truck.dao.otherlendmoney.OtherLendMoneyDao;
import com.luzhu.truck.dao.trafficticket.TrafficTicketDao;
import com.luzhu.truck.dao.unionfee.UnionFeeDao;
import com.luzhu.truck.dto.bill.MonthBillReq;
import com.luzhu.truck.dto.bill.MonthBillResponse;
import com.luzhu.truck.dto.givebackmoney.SumGiveBackMoneyAmountAndInterestDto;
import com.luzhu.truck.dto.invoice.InvoiceSumAmountAndTaxDto;
import com.luzhu.truck.dto.lendmoney.SumAmountAndTaxDto;
import com.luzhu.truck.entity.Car;
import com.luzhu.truck.enums.InvoiceType;
import com.luzhu.truck.util.DateTimeValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;


@Service
@Slf4j
public class BillService {
    @Autowired
    private CarDao carDao;
    @Autowired
    private ManageFeeDao manageFeeDao;
    @Autowired
    private UnionFeeDao unionFeeDao;
    @Autowired
    private LastMonthOweDao lastMonthOweDao;
    @Autowired
    private LoanFeeDao loanFeeDao;
    @Autowired
    private LaborInsuranceDao laborInsuranceDao;
    @Autowired
    private InsuranceFeeDao insuranceFeeDao;
    @Autowired
    private HealthFeeDao healthFeeDao;
    @Autowired
    private LicenseTaxDao licenseTaxDao;
    @Autowired
    private FuelTaxDao fuelTaxDao;
    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private LendMoneyDao lendMoneyDao;
    @Autowired
    private GiveBackMoneyDao giveBackMoneyDao;
    @Autowired
    private OtherLendMoneyDao otherLendMoneyDao;
    @Autowired
    private OtherGiveBackMoneyDao otherGiveBackMoneyDao;
    @Autowired
    private TrafficTicketDao trafficTicketDao;
    public MonthBillResponse getMonthBill(MonthBillReq req) {
        Car searchCar = carDao.getCarById(req.getId());

        MonthBillResponse res = new MonthBillResponse();

        String billDate = req.getBillDate();
        DateTimeValidate.checkYearMonth(billDate);
        //上月欠款
        BigDecimal lastMonthOweAmount = lastMonthOweDao.getAmountById(req.getCarLicenseNum(), billDate);
        res.setLastMonthOweAmount(lastMonthOweAmount);
        //管理費
        BigDecimal manageFee = manageFeeDao.getFeeById(req.getCarLicenseNum(), billDate);
        res.setManageFee(manageFee);
        //公會費
        BigDecimal unionFee = unionFeeDao.getFeeById(req.getCarLicenseNum(), billDate);
        res.setUnionFee(unionFee);
        //車貸
        BigDecimal loanFee = loanFeeDao.getFeeById(req.getCarLicenseNum(), billDate);
        res.setLoanFee(loanFee);
        //勞保
        BigDecimal laborInsuranceFee = laborInsuranceDao.getFeeById(req.getCarLicenseNum(), billDate);
        res.setLaborInsuranceFee(laborInsuranceFee);
        //健保費
        BigDecimal healthFee = healthFeeDao.getFeeById(req.getCarLicenseNum(), billDate);
        res.setHealthFee(healthFee);
        //保費
        BigDecimal insuranceFeeFee = insuranceFeeDao.getFeeById(req.getCarLicenseNum(), billDate);
        res.setInsuranceFee(insuranceFeeFee);
        //牌照稅
        BigDecimal licenseTaxFee = licenseTaxDao.getFeeById(req.getCarLicenseNum(), billDate);
        res.setLicenseTaxFee(licenseTaxFee);
        //燃料稅
        BigDecimal fuelTaxFee = fuelTaxDao.getFeeById(req.getCarLicenseNum(), billDate);
        res.setFuelTaxFee(fuelTaxFee);
        //三種發票
        LocalDate monthFirst = YearMonth.parse(billDate, DateTimeFormatter.ofPattern("yyyy-MM")).atDay(1);
        LocalDate monthEnd = YearMonth.parse(billDate, DateTimeFormatter.ofPattern("yyyy-MM")).atEndOfMonth();
        InvoiceSumAmountAndTaxDto gasInvoice = invoiceDao.getSumAmountByType(req.getCarLicenseNum(), monthFirst, monthEnd, InvoiceType.GAS.getType());
        res.setInvoiceGasAmount(gasInvoice.getSum());
        res.setInvoiceGasAmountTax(gasInvoice.getTaxSum());
        InvoiceSumAmountAndTaxDto saleInvoice = invoiceDao.getSumAmountByType(req.getCarLicenseNum(), monthFirst, monthEnd, InvoiceType.SALE.getType());
        res.setInvoiceSaleAmount(saleInvoice.getSum());
        res.setInvoiceSaleAmountTax(saleInvoice.getTaxSum());
        InvoiceSumAmountAndTaxDto offsetInvoice = invoiceDao.getSumAmountByType(req.getCarLicenseNum(), monthFirst, monthEnd, InvoiceType.OFFSET.getType());
        res.setInvoiceOffsetAmount(offsetInvoice.getSum());
        res.setInvoiceOffsetAmountTax(offsetInvoice.getTaxSum());

        //借款金額
        SumAmountAndTaxDto lendMoney = lendMoneyDao.getLendMoney(req.getCarLicenseNum(), monthFirst, monthEnd);
        res.setLendMoney(lendMoney.getSum());
        res.setLendMoneyInterest(lendMoney.getInterestSum());
        //入款金額
        SumGiveBackMoneyAmountAndInterestDto giveBackMoney = giveBackMoneyDao.getGiveBackMoney(req.getCarLicenseNum(), monthFirst, monthEnd);
        res.setGiveBackMoney(giveBackMoney.getSum());
        res.setGiveBackInterest(giveBackMoney.getInterestSum());
        //其他應收
        BigDecimal otherLendMoneyDaoSumAmount = otherLendMoneyDao.getSumAmount(req.getCarLicenseNum(), monthFirst, monthEnd);
        res.setOtherLendMoneyAmount(otherLendMoneyDaoSumAmount);
        //其他抵收
        BigDecimal otherGiveBackMoneyDaoSumAmount = otherGiveBackMoneyDao.getSumAmount(req.getCarLicenseNum(), monthFirst, monthEnd);
        res.setOtherGiveBackMoneyAmount(otherGiveBackMoneyDaoSumAmount);
        //罰單
        BigDecimal trafficSum = trafficTicketDao.getSumAmount(req.getCarLicenseNum(), monthFirst, monthEnd);
        res.setTrafficSum(trafficSum);

        return res;
    }

    public static void main(String[] args) {
//        LocalDate parse = LocalDate.parse("2024-08", DateTimeFormatter.ofPattern("yyyy-MM"));
        String billDate = "2024-08";

        LocalDate localDate = YearMonth.parse(billDate, DateTimeFormatter.ofPattern("yyyy-MM")).atDay(1);
        LocalDate localDate1 = YearMonth.parse(billDate, DateTimeFormatter.ofPattern("yyyy-MM")).atEndOfMonth();
        String a = "a";
    }


}
