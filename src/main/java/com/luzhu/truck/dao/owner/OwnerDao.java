package com.luzhu.truck.dao.owner;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.dto.car.CarOwnerDropDownDto;
import com.luzhu.truck.entity.owner.Owner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OwnerDao extends BaseDao<Owner, Integer> {
    @Modifying
    @Query(value = "INSERT INTO owner (name, id_num, sex, birthday, phone1, phone2, mobile, fax, address, mail_address) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10)",
            nativeQuery = true)
    int addOwner(String name, String idNum, String sex, String birthday, String phone1, String phone2, String mobile, String fax, String address, String mailAddress);
    @Query(value = "SELECT * FROM owner WHERE status = 'enable' ORDER BY id DESC",
            countQuery = " SELECT COUNT(1) FROM owner WHERE status = 'enable' ORDER BY id DESC"
            , nativeQuery = true)
    Page<Owner> getAllOwner(Pageable pageable);

    @Query(value = "SELECT id, name FROM owner WHERE status = 'enable'"
            , nativeQuery = true)
    List<CarOwnerDropDownDto> getAllOwnerDropDown();

    @Query(value = "SELECT * FROM owner WHERE name LIKE %?1% AND status = 'enable' ORDER BY id DESC",
            countQuery = " SELECT COUNT(1) FROM owner WHERE name LIKE %?1% AND status = 'enable'"
            , nativeQuery = true)
    Page<Owner> searchByName(String name, Pageable pageable);

    @Query(value = "SELECT * FROM owner WHERE id = ?1"
            , nativeQuery = true)
    Owner getOwnerById(long id);

    @Modifying
    @Query(value = "UPDATE owner " +
            "SET name = ?1, phone1 = ?2, id_num = ?3, sex = ?4, birthday = ?5, phone2 = ?6, mobile = ?7, fax = ?8, address = ?9, mail_address = ?10 " +
            "WHERE id = ?11",
            nativeQuery = true)
    int updateOwner(String name, String phone1, String idNum, String sex, String birthday, String phone2, String mobile, String fax, String address, String mailAddress, int id);

    @Modifying
    @Query(value = "UPDATE owner SET status = ?2 WHERE id = ?1",
            nativeQuery = true)
    int updateOwnerStatus(int id, String status);
}
