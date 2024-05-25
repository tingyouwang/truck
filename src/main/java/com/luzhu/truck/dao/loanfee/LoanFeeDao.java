package com.luzhu.truck.dao.loanfee;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.loanfee.LoanFee;
import com.luzhu.truck.entity.unionfee.UnionFee;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanFeeDao extends BaseDao<LoanFee, String> {
}
