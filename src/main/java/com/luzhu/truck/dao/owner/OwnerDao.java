package com.luzhu.truck.dao.owner;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.owner.Owner;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OwnerDao extends BaseDao<Owner, Integer> {
    @Modifying
    @Query(value = "INSERT INTO owner (name, id_num, sex, birthday, phone1, phone2, mobile, fax, address, mail_address) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10)",
            nativeQuery = true)
    int addOwner(String name, String idNum, String sex, String birthday, String phone1, String phone2, String mobile, String fax, String address, String mailAddress);
}
