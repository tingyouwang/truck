package com.luzhu.truck.service.bill;

import com.luzhu.truck.dao.Healthfee.HealthFeeDao;
import com.luzhu.truck.dao.car.CarDao;
import com.luzhu.truck.dao.carfee.CarFeeDao;
import com.luzhu.truck.dao.fueltax.FuelTaxDao;
import com.luzhu.truck.dao.givebackmoney.GiveBackMoneyDao;
import com.luzhu.truck.dao.insurancefee.InsuranceFeeDao;
import com.luzhu.truck.dao.insurnacefeesetting.InsuranceFeeSettingDao;
import com.luzhu.truck.dao.invoice.InvoiceDao;
import com.luzhu.truck.dao.laborinsurance.LaborInsuranceDao;
import com.luzhu.truck.dao.lastmonthowe.LastMonthOweDao;
import com.luzhu.truck.dao.lendmoney.LendMoneyDao;
import com.luzhu.truck.dao.licensetax.LicenseTaxDao;
import com.luzhu.truck.dao.loanfee.LoanFeeDao;
import com.luzhu.truck.dao.loanfeesetting.LoanFeeSettingDao;
import com.luzhu.truck.dao.managefee.ManageFeeDao;
import com.luzhu.truck.dao.othergivebackmoney.OtherGiveBackMoneyDao;
import com.luzhu.truck.dao.otherlendmoney.OtherLendMoneyDao;
import com.luzhu.truck.dao.payinterest.PayInterestDao;
import com.luzhu.truck.dao.receiveoffset.ReceiveOffsetDao;
import com.luzhu.truck.dao.returnmoney.ReturnMoneyDao;
import com.luzhu.truck.dao.trafficticket.TrafficTicketDao;
import com.luzhu.truck.dao.unionfee.UnionFeeDao;
import com.luzhu.truck.dto.bill.*;
import com.luzhu.truck.dto.givebackmoney.SumGiveBackMoneyAmountAndInterestDto;
import com.luzhu.truck.dto.invoice.InvoiceSumAmountAndTaxDto;
import com.luzhu.truck.dto.lendmoney.SumAmountAndTaxDto;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.entity.fuel.FuelTax;
import com.luzhu.truck.entity.givebackmoney.GiveBackMoney;
import com.luzhu.truck.entity.healthfee.HealthFee;
import com.luzhu.truck.entity.insurancefee.InsuranceFee;
import com.luzhu.truck.entity.insurancefeesetting.InsuranceFeeSetting;
import com.luzhu.truck.entity.invoice.Invoice;
import com.luzhu.truck.entity.laborInsurance.LaborInsurance;
import com.luzhu.truck.entity.lendmoney.LendMoney;
import com.luzhu.truck.entity.licensetax.LicenseTax;
import com.luzhu.truck.entity.loanfee.LoanFee;
import com.luzhu.truck.entity.loanfeesetting.LoanFeeSetting;
import com.luzhu.truck.entity.managefee.ManageFee;
import com.luzhu.truck.entity.othergivebackmoney.OtherGiveBackMoney;
import com.luzhu.truck.entity.otherlendmoney.OtherLendMoney;
import com.luzhu.truck.entity.payinterest.PayInterest;
import com.luzhu.truck.entity.receiveoffset.ReceiveOffset;
import com.luzhu.truck.entity.returnmoney.ReturnMoney;
import com.luzhu.truck.entity.trafficticket.TrafficTicket;
import com.luzhu.truck.entity.unionfee.UnionFee;
import com.luzhu.truck.enums.InvoiceType;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.schedule.service.*;
import com.luzhu.truck.service.insurancefeesetting.InsuranceFeeSettingService;
import com.luzhu.truck.service.laborinsurance.LaborInsuranceService;
import com.luzhu.truck.service.monthbillsnapshot.MonthBillSnapshotService;
import com.luzhu.truck.entity.monthbillsnapshot.MonthBillSnapshot;
import com.luzhu.truck.util.DateTimeUtil;
import com.luzhu.truck.util.DateTimeValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.luzhu.truck.exception.SystemExceptionEnum.*;


@Service
@Slf4j
public class BillService {
    @Value("${env.time.offset}")
    private String timeOffset;
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
    @Autowired
    private PayInterestDao payInterestDao;
    @Autowired
    private ReceiveOffsetDao receiveOffsetDao;
    @Autowired
    private ReturnMoneyDao returnMoneyDao;
    @Autowired
    private HealthFeeService healthFeeService;
    @Autowired
    private LaborInsuranceService laborInsuranceService;
    @Autowired
    private CarFeeDao carFeeDao;
    @Autowired
    private FuelTaxService fuelTaxService;
    @Autowired
    private InsuranceFeeSettingDao insuranceFeeSettingDao;
    @Autowired
    private InsuranceFeeSettingService insuranceFeeSettingService;
    @Autowired
    private LicenseTaxService licenseTaxService;
    @Autowired
    private LoanFeeSettingDao loanFeeSettingDao;
    @Autowired
    private LoanFeeService loanFeeService;
    @Autowired
    private ManageFeeService manageFeeService;
    @Autowired
    private UnionFeeService unionFeeService;
    @Autowired
    private MonthBillSnapshotService monthBillSnapshotService;
    
    public MonthBillResponse getMonthBill(MonthBillReq req) {
        String billDate = req.getBillDate();
        String carLicenseNum = req.getCarLicenseNum();
        
        DateTimeValidate.checkYearMonth(billDate);
        
        // 优先查询快照
        Optional<MonthBillSnapshot> snapshotOpt = monthBillSnapshotService.getSnapshot(carLicenseNum, billDate);
        if (snapshotOpt.isPresent()) {
            log.info("从快照中读取账单: 车牌={}, 月份={}", carLicenseNum, billDate);
            return monthBillSnapshotService.convertToResponse(snapshotOpt.get());
        }
        
        // 快照不存在，执行即时计算
        log.info("快照不存在，即时计算账单: 车牌={}, 月份={}", carLicenseNum, billDate);
        return calculateMonthBill(req);
    }

    /**
     * 忽略快照，始终以当前费用数据重新计算（供手动/排程生成快照使用）
     */
    public MonthBillResponse getMonthBillForceRecalculate(MonthBillReq req) {
        String billDate = req.getBillDate();
        String carLicenseNum = req.getCarLicenseNum();
        DateTimeValidate.checkYearMonth(billDate);
        log.info("强制即时计算账单（忽略快照）: 车牌={}, 月份={}", carLicenseNum, billDate);
        return calculateMonthBill(req);
    }
    
    private MonthBillResponse calculateMonthBill(MonthBillReq req) {
        MonthBillResponse res = new MonthBillResponse();

        String billDate = req.getBillDate();
        DateTimeValidate.checkYearMonth(billDate);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth yearMonth = YearMonth.parse(billDate, dateTimeFormatter);

        String lastMonthBillDate = yearMonth.minusMonths(1).format(dateTimeFormatter);

        //上月欠款
        BigDecimal lastMonthOweAmount = lastMonthOweDao.getAmountById(req.getCarLicenseNum(), lastMonthBillDate);
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
        LocalDate monthFirst = yearMonth.atDay(1);
        LocalDate monthEnd = yearMonth.atEndOfMonth();
        InvoiceSumAmountAndTaxDto gasInvoice = invoiceDao.getSumAmountByType(req.getCarLicenseNum(), monthFirst, monthEnd, InvoiceType.GAS.getType(), 0);
        res.setInvoiceGasAmount(gasInvoice.getSum());
        res.setInvoiceGasAmountTax(gasInvoice.getTaxSum());
        InvoiceSumAmountAndTaxDto saleInvoice = invoiceDao.getSumAmountByType(req.getCarLicenseNum(), monthFirst, monthEnd, InvoiceType.SALE.getType(), 0);
        res.setInvoiceSaleAmount(saleInvoice.getSum());
        res.setInvoiceSaleAmountTax(saleInvoice.getTaxSum());
        InvoiceSumAmountAndTaxDto offsetInvoice = invoiceDao.getSumAmountByType(req.getCarLicenseNum(), monthFirst, monthEnd, InvoiceType.OFFSET.getType(), 0);
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
        //代支利息
        BigDecimal payInterestSum = payInterestDao.getSumAmount(req.getCarLicenseNum(), monthFirst, monthEnd);
        res.setPayInterest(payInterestSum);
        //收據抵收
        BigDecimal receiveOffsetSum = receiveOffsetDao.getSumAmount(req.getCarLicenseNum(), monthFirst, monthEnd);
        res.setReceiveOffset(receiveOffsetSum);
        //入款退回
        BigDecimal returnMoneySum = returnMoneyDao.getSumAmount(req.getCarLicenseNum(), monthFirst, monthEnd);
        res.setReturnMoney(returnMoneySum);

        // 先把组成字段统一取整，再计算 totalSum，确保 totalSum 与各字段显示一致
        res.roundAllFieldsToInteger();

        //本月欠款
        res.calculateTotalSum();

        // totalSum 再做一次取整（calculateTotalSum 可能产生小数）
        res.roundAllFieldsToInteger();

        return res;
    }

    public MonthsBillDetailResponse getBillDetail(MonthBillDetailReq req) {
        List<String> billDateList = req.getBillDateList();
        List<MonthsBillDetailDto> res = new ArrayList<>();

        for (String billDate : billDateList) {
            DateTimeValidate.checkYearMonth(billDate);

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
            YearMonth yearMonth = YearMonth.parse(billDate, dateTimeFormatter);

            String carLicenseNum = req.getCarLicenseNum();
            List<String> date = List.of(billDate);
            //管理費
            List<ManageFee> manageFees = manageFeeDao.getDetailByExpenseYearMonth(carLicenseNum, date);
            res.addAll(manageFees.stream().map(fee -> MonthsBillDetailDto.builder().expenseYearMonth(DateTimeUtil.parseToMinguoDateYearMonth(fee.getExpenseYearMonth()))
                    .name("管理費")
                    .receiveAmount(fee.getAmount().intValue()).build()).toList());
            //公會費
            List<UnionFee> unionFees = unionFeeDao.getDetailByExpenseYearMonth(req.getCarLicenseNum(), date);
            res.addAll(unionFees.stream().map(fee -> MonthsBillDetailDto.builder().expenseYearMonth(DateTimeUtil.parseToMinguoDateYearMonth(fee.getExpenseYearMonth()))
                    .name("公會費")
                    .receiveAmount(fee.getAmount().intValue()).build()).toList());
            //車貸
            List<LoanFee> loanFees = loanFeeDao.getDetailByExpenseYearMonth(req.getCarLicenseNum(), date);
            res.addAll(loanFees.stream().map(fee -> MonthsBillDetailDto.builder().expenseYearMonth(DateTimeUtil.parseToMinguoDateYearMonth(fee.getExpenseYearMonth()))
                    .name("車貸款")
                    .receiveAmount(fee.getAmount().intValue()).build()).toList());
            //勞保
            List<LaborInsurance> laborInsurances = laborInsuranceDao.getDetailByExpenseYearMonth(req.getCarLicenseNum(), date);
            res.addAll(laborInsurances.stream().map(fee -> MonthsBillDetailDto.builder().expenseYearMonth(DateTimeUtil.parseToMinguoDateYearMonth(fee.getExpenseYearMonth()))
                    .name("勞保費")
                    .receiveAmount(fee.getAmount().intValue()).build()).toList());
            //健保費
            List<HealthFee> healthFees = healthFeeDao.getDetailByExpenseYearMonth(req.getCarLicenseNum(), date);
            res.addAll(healthFees.stream().map(fee -> MonthsBillDetailDto.builder().expenseYearMonth(DateTimeUtil.parseToMinguoDateYearMonth(fee.getExpenseYearMonth()))
                    .name("健保費")
                    .receiveAmount(fee.getAmount().intValue()).build()).toList());
            //保費
            List<InsuranceFee> insuranceFees = insuranceFeeDao.getDetailByExpenseYearMonth(req.getCarLicenseNum(), date);
            res.addAll(insuranceFees.stream().map(fee -> MonthsBillDetailDto.builder().expenseYearMonth(DateTimeUtil.parseToMinguoDateYearMonth(fee.getExpenseYearMonth()))
                    .name("保險費")
                    .receiveAmount(fee.getAmount().intValue()).build()).toList());
            //牌照稅
            List<LicenseTax> licenseTaxes = licenseTaxDao.getDetailByExpenseYearMonth(req.getCarLicenseNum(), date);
            res.addAll(licenseTaxes.stream().map(fee -> MonthsBillDetailDto.builder().expenseYearMonth(DateTimeUtil.parseToMinguoDateYearMonth(fee.getExpenseYearMonth()))
                    .name("牌照稅")
                    .receiveAmount(fee.getAmount().intValue()).build()).toList());
            //燃料稅
            List<FuelTax> fuelTaxes = fuelTaxDao.getDetailByExpenseYearMonth(req.getCarLicenseNum(), date);
            res.addAll(fuelTaxes.stream().map(fee -> MonthsBillDetailDto.builder().expenseYearMonth(DateTimeUtil.parseToMinguoDateYearMonth(fee.getExpenseYearMonth()))
                    .name("燃料稅")
                    .receiveAmount(fee.getAmount().intValue()).build()).toList());
            //三種發票
            LocalDate monthFirst = yearMonth.atDay(1);
            LocalDate monthEnd = yearMonth.atEndOfMonth();
            List<Invoice> invoices = invoiceDao.getDetailByInvoiceDate(req.getCarLicenseNum(), monthFirst, monthEnd, 0);
            List<MonthsBillDetailDto> invoiceRes = invoices.stream().map(fee -> {
                MonthsBillDetailDto.MonthsBillDetailDtoBuilder builder = MonthsBillDetailDto.builder();
                if (fee.getType().equals("SALE")) {
                    builder.name("銷發發票").receiveAmount(fee.getAmountTax().intValue());
                } else if (fee.getType().equals("GAS")) {
                    builder.name("油單發票").offsetAmount(fee.getAmountTax().intValue());
                } else if (fee.getType().equals("OFFSET")) {
                    builder.name("抵發發票").offsetAmount(fee.getAmountTax().intValue());
                }
                LocalDate parse = LocalDate.parse(fee.getInvoiceDate(), DateTimeFormatter.ISO_DATE);
                return builder.expenseYearMonth(DateTimeUtil.tryToMinguoDateStr(DateTimeUtil.fullDateToYearMonth(fee.getHandleDate())))
                        .note(fee.getNote())
                        .date(DateTimeUtil.parseToMinguoDate(parse))
                        .build();
            }).toList();
            res.addAll(invoiceRes);

            //借款金額
            List<LendMoney> lendMoneyList = lendMoneyDao.getDetailByDate(req.getCarLicenseNum(), monthFirst, monthEnd);
            res.addAll(lendMoneyList.stream().map(fee -> MonthsBillDetailDto.builder().expenseYearMonth(DateTimeUtil.tryToMinguoDateStr(DateTimeUtil.fullDateToYearMonth(fee.getLendDate())))
                    .name("借款金額")
                    .receiveAmount(fee.getAmount().intValue())
                    .date(DateTimeUtil.parseToMinguoDate(LocalDate.parse(fee.getLendDate())))
                    .note(fee.getNote())
                    .build()).toList());
            res.addAll(lendMoneyList.stream().map(fee -> MonthsBillDetailDto.builder().expenseYearMonth(DateTimeUtil.tryToMinguoDateStr(DateTimeUtil.fullDateToYearMonth(fee.getLendDate())))
                    .name("借款利息")
                    .receiveAmount(fee.getInterestAmount().intValue())
                    .date(DateTimeUtil.parseToMinguoDate(LocalDate.parse(fee.getLendDate())))
                    .note(fee.getNote())
                    .build()).toList());

            //入款金額
            List<GiveBackMoney> giveBackMoneyList = giveBackMoneyDao.getDetailByDate(req.getCarLicenseNum(), monthFirst, monthEnd);
            res.addAll(giveBackMoneyList.stream().map(fee -> MonthsBillDetailDto.builder()
                    .expenseYearMonth(DateTimeUtil.tryToMinguoDateStr(DateTimeUtil.fullDateToYearMonth(fee.getGiveBackDate())))
                    .name("入款金額")
                    .offsetAmount(fee.getAmount().intValue())
                    .date(DateTimeUtil.tryToMinguoDateStr(fee.getGiveBackDate()))
                    .note(fee.getNote())
                    .build()).toList());
            res.addAll(giveBackMoneyList.stream().map(fee -> MonthsBillDetailDto.builder()
                    .expenseYearMonth(DateTimeUtil.tryToMinguoDateStr(DateTimeUtil.fullDateToYearMonth(fee.getGiveBackDate())))
                    .name("入票利息")
                    .receiveAmount(fee.getInterestAmount().intValue())
                    .date(DateTimeUtil.tryToMinguoDateStr(fee.getGiveBackDate()))
                    .note(fee.getNote())
                    .build()).toList());
            //其他應收
            List<OtherLendMoney> otherLendMoneyList = otherLendMoneyDao.getDetailByDate(req.getCarLicenseNum(), monthFirst, monthEnd);
            res.addAll(otherLendMoneyList.stream().map(fee -> MonthsBillDetailDto.builder()
                    .expenseYearMonth(DateTimeUtil.tryToMinguoDateStr(DateTimeUtil.fullDateToYearMonth(fee.getLendDate())))
                    .name("其他應收")
                    .receiveAmount(fee.getAmount().intValue())
                    .date(DateTimeUtil.tryToMinguoDateStr(fee.getLendDate()))
                    .note(fee.getNote())
                    .build()).toList());

            //其他抵收
            List<OtherGiveBackMoney> otherGiveBackMoneyList = otherGiveBackMoneyDao.getDetailByDate(req.getCarLicenseNum(), monthFirst, monthEnd);
            res.addAll(otherGiveBackMoneyList.stream().map(fee -> MonthsBillDetailDto.builder()
                    .expenseYearMonth(DateTimeUtil.tryToMinguoDateStr(DateTimeUtil.fullDateToYearMonth(fee.getGiveBackDate())))
                    .name("其他抵收")
                    .offsetAmount(fee.getAmount().intValue())
                    .date(DateTimeUtil.tryToMinguoDateStr(fee.getGiveBackDate()))
                    .note(fee.getNote())
                    .build()).toList());

            //罰單
            List<TrafficTicket> ticketList = trafficTicketDao.getDetailByDate(req.getCarLicenseNum(), monthFirst, monthEnd);
            res.addAll(ticketList.stream().map(fee -> MonthsBillDetailDto.builder()
                    .expenseYearMonth(DateTimeUtil.tryToMinguoDateStr(DateTimeUtil.fullDateToYearMonth(fee.getHandleDate())))
                    .name("罰單")
                    .receiveAmount(fee.getAmount().intValue())
                    .date(DateTimeUtil.tryToMinguoDateStr(fee.getHandleDate()))
                    .note(fee.getNote())
                    .build()).toList());
            //代支利息
            List<PayInterest> payInterestList = payInterestDao.getDetailByDate(req.getCarLicenseNum(), monthFirst, monthEnd);
            res.addAll(payInterestList.stream().map(fee -> MonthsBillDetailDto.builder()
                    .expenseYearMonth(DateTimeUtil.tryToMinguoDateStr(DateTimeUtil.fullDateToYearMonth(fee.getPayDate())))
                    .name("代支利息")
                    .receiveAmount(fee.getAmount().intValue())
                    .date(DateTimeUtil.tryToMinguoDateStr(fee.getPayDate()))
                    .note(fee.getNote())
                    .build()).toList());
            //收據抵收
            List<ReceiveOffset> receiveOffsetList = receiveOffsetDao.getDetailByDate(req.getCarLicenseNum(), monthFirst, monthEnd);
            res.addAll(receiveOffsetList.stream().map(fee -> MonthsBillDetailDto.builder()
                    .expenseYearMonth(DateTimeUtil.tryToMinguoDateStr(DateTimeUtil.fullDateToYearMonth(fee.getPayDate())))
                    .name("收據抵收")
                    .offsetAmount(fee.getAmount().intValue())
                    .date(DateTimeUtil.tryToMinguoDateStr(fee.getPayDate()))
                    .note(fee.getNote())
                    .build()).toList());
            //入款退回
            List<ReturnMoney> returnMoneyList = returnMoneyDao.getDetailByDate(req.getCarLicenseNum(), monthFirst, monthEnd);
            res.addAll(returnMoneyList.stream().map(fee -> MonthsBillDetailDto.builder()
                    .expenseYearMonth(DateTimeUtil.tryToMinguoDateStr(DateTimeUtil.fullDateToYearMonth(fee.getPayDate())))
                    .name("入款退回")
                    .receiveAmount(fee.getAmount().intValue())
                    .date(DateTimeUtil.tryToMinguoDateStr(fee.getPayDate()))
                    .note(fee.getNote())
                    .build()).toList());
        }

        int sum = res.stream().map(dto -> Math.subtractExact(dto.getReceiveAmount(), dto.getOffsetAmount()))
                .toList().stream().mapToInt(Integer::intValue).sum();
        int receiveSum = res.stream().mapToInt(MonthsBillDetailDto::getReceiveAmount).sum();
        int offsetSum = res.stream().mapToInt(MonthsBillDetailDto::getOffsetAmount).sum();

        MonthsBillDetailResponse response = new MonthsBillDetailResponse();
        response.setDetailDtos(res);
        response.setSum(sum);
        response.setReceiveSum(receiveSum);
        response.setOffsetSum(offsetSum);
        return response;
    }

    @Transactional
    public void generateCurrentMonthBill(GenerateCurrentMonthBillReq req) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(Integer.parseInt(timeOffset)));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String yearMonth = now.format(formatter);

        //檢查是否有重複產出
        String carLicenseNum = req.getCarLicenseNum();
        List<HealthFee> detailByExpenseYearMonth = healthFeeDao.getDetailByExpenseYearMonth(carLicenseNum, Collections.singletonList(yearMonth));
        if (!detailByExpenseYearMonth.isEmpty()) throw new AppException(GENERATE_CURRENT_BILL);

        LocalDate nowDate = LocalDate.now(ZoneOffset.ofHours(Integer.parseInt(timeOffset)));
        //保險設定
        InsuranceFeeSetting usingInsuranceFeeSetting = insuranceFeeSettingDao.getInsuranceFeeSettingByCarNum(nowDate, carLicenseNum);
        if (Optional.ofNullable(usingInsuranceFeeSetting).isEmpty()) throw new AppException(NEED_SET_INSURANCE_CAR_FEE);

        List<InsuranceFeeSetting> insuranceFeeSettingList = List.of(usingInsuranceFeeSetting);

        //貸款設定
        List<LoanFeeSetting> loanFeeSettingList = new ArrayList<>();
        LoanFeeSetting usingLoanFeeSetting = loanFeeSettingDao.getUsingLoanFeeSettingByCarLicenseNum(nowDate, carLicenseNum);
        if (Optional.ofNullable(usingLoanFeeSetting).isPresent()) loanFeeSettingList.add(usingLoanFeeSetting);

        CarFee carFee = carFeeDao.getCarFeeByLicenseNum(carLicenseNum);
        if (Optional.ofNullable(carFee).isEmpty()) throw new AppException(NEED_SET_INSURANCE_CAR_FEE);
        List<CarFee> carFeeList = List.of(carFee);

        int i = healthFeeService.monthlyInsertFee(yearMonth, now, carFeeList);
        int i1 = laborInsuranceService.monthlyInsertFee(yearMonth, now, carFeeList);
        int i2 = fuelTaxService.seasonlyInsertFee(yearMonth, now, carFeeList);

        int i3 = insuranceFeeSettingService.monthlyInsertFee(yearMonth, nowDate, insuranceFeeSettingList);
        insuranceFeeSettingService.updateGenerateBillStatus(insuranceFeeSettingList);

        int i4 = licenseTaxService.halfYearInsertFee(yearMonth, now, carFeeList);

        int i5 = loanFeeService.monthlyInsertFee(yearMonth, nowDate, loanFeeSettingList);
        int i6 = manageFeeService.monthlyInsertFee(yearMonth, now, carFeeList);
        int i7 = unionFeeService.monthlyInsertFee(yearMonth, now, carFeeList);
        log.info(String.format("[手動新增當月帳單:%s:車牌號碼:%s] 健保筆數:%s, 勞保筆數:%s, 燃料稅筆數:%s, 保險費筆數:%s, 牌照稅筆數:%s, 貸款筆數:%s, 管理費筆數:%s, 公會費筆數:%s",
                yearMonth, carLicenseNum, i, i1, i2, i3, i4, i5, i6, i7));

    }


}
