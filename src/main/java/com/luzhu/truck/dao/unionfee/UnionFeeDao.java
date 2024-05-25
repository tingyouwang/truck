package com.luzhu.truck.dao.unionfee;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.managefee.ManageFee;
import com.luzhu.truck.entity.unionfee.UnionFee;
import org.springframework.stereotype.Repository;

@Repository
public interface UnionFeeDao extends BaseDao<UnionFee, String> {
}
