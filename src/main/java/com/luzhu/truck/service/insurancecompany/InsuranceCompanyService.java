package com.luzhu.truck.service.insurancecompany;

import com.luzhu.truck.dao.insurancecompany.InsuranceCompanyDao;
import com.luzhu.truck.dto.BaseParam;
import com.luzhu.truck.dto.caragency.AddCarAgencyParam;
import com.luzhu.truck.dto.caragency.UpdateCarAgencyParam;
import com.luzhu.truck.dto.insurancecompany.AddInsuranceComParam;
import com.luzhu.truck.dto.insurancecompany.InsuranceComDropDownList;
import com.luzhu.truck.dto.insurancecompany.UpdateInsuranceComParam;
import com.luzhu.truck.entity.caragency.CarAgency;
import com.luzhu.truck.entity.insurancecompany.InsuranceCompany;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InsuranceCompanyService {
    @Autowired
    private InsuranceCompanyDao insuranceCompanyDao;
    public PageResult<InsuranceCompany> getInsuranceCompany(BaseParam param) {
        Page<InsuranceCompany> allInsuranceCompany = insuranceCompanyDao.getAllInsuranceCompany(param.getPageable());
        return new PageResult<>(allInsuranceCompany);
    }

    public List<InsuranceComDropDownList> getInsuranceComDropDown() {
        return insuranceCompanyDao.getInsuranceComDropDown();
    }

    @Transactional
    public void addInsuranceCom(AddInsuranceComParam addInsuranceComParam) {
        int insertCount = insuranceCompanyDao.insertInsuranceCom(addInsuranceComParam.getCompanyName(), addInsuranceComParam.getShortName(),
                addInsuranceComParam.getContactor(), addInsuranceComParam.getPhone(), addInsuranceComParam.getNote());
        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }

    @Transactional
    public void deleteInsuranceCompany(int id) {
        int deleteCount = insuranceCompanyDao.deleteInsuranceCompanyById(id);
        Validator.isFalseThrow(1 == deleteCount,
                new AppException(SystemExceptionEnum.DELETE_ERROR));
    }

    @Transactional
    public void updateInsuranceCompany(UpdateInsuranceComParam param) {
        InsuranceCompany company = new InsuranceCompany();
        company.setId(param.getId());
        company.setCompanyName(param.getCompanyName());
        company.setShortName(param.getShortName());
        company.setContactor(param.getContactor());
        company.setPhone(param.getPhone());
        company.setNote(param.getNote());
        insuranceCompanyDao.save(company);
    }

}
