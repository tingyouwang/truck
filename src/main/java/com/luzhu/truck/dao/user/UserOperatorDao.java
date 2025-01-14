package com.luzhu.truck.dao.user;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.user.UserOperator;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOperatorDao extends BaseDao<UserOperator, String> {
    @Query(value = "SELECT * FROM user_operator WHERE user_id = ?1"
            , nativeQuery = true)
    UserOperator getUserById(String userId);

    @Modifying
    @Query(value = "UPDATE user_operator SET last_login_time = ?2, last_login_ip = ?3 WHERE user_id = ?1 ", nativeQuery = true)
    int updateByLogin(String userId, long loginTime, String loginIp);
}
