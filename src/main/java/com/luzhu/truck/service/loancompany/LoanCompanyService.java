package com.luzhu.truck.service.loancompany;

import com.luzhu.truck.dao.loancompany.LoanCompanyDao;
import com.luzhu.truck.dto.BaseParam;
import com.luzhu.truck.dto.insurancecompany.AddInsuranceComParam;
import com.luzhu.truck.dto.insurancecompany.UpdateInsuranceComParam;
import com.luzhu.truck.dto.loancompany.AddLoadComParam;
import com.luzhu.truck.dto.loancompany.UpdateLoanComParam;
import com.luzhu.truck.entity.insurancecompany.InsuranceCompany;
import com.luzhu.truck.entity.loancompany.LoanCompany;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanCompanyService {
    @Autowired
    private LoanCompanyDao loanCompanyDao;
    public PageResult<LoanCompany> getLoanCompany(BaseParam param) {
        Page<LoanCompany> allLoanCompany = loanCompanyDao.getAllLoanCompany(param.getPageable());
        return new PageResult<>(allLoanCompany);
    }

    @Transactional
    public void addLoanCom(AddLoadComParam param) {
        int insertCount = loanCompanyDao.insertLoanCompany(param.getCompanyName(), param.getShortName(),
                param.getContactor(), param.getPhone(), param.getNote());
        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }

    @Transactional
    public void updateLoanCom(UpdateLoanComParam param) {
        LoanCompany loanCompany = new LoanCompany();
        loanCompany.setId(param.getId());
        loanCompany.setCompanyName(param.getCompanyName());
        loanCompany.setShortName(param.getShortName());
        loanCompany.setContactor(param.getContactor());
        loanCompany.setPhone(param.getPhone());
        loanCompany.setNote(param.getNote());

        loanCompanyDao.save(loanCompany);
    }

    @Transactional
    public void deleteLoanCompany(int id) {
        int deleteCount = loanCompanyDao.deleteLoanCompanyById(id);
        Validator.isFalseThrow(1 == deleteCount,
                new AppException(SystemExceptionEnum.DELETE_ERROR));
    }

}
