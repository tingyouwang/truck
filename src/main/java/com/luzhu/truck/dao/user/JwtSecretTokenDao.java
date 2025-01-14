package com.luzhu.truck.dao.user;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.user.JwtSecretToken;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JwtSecretTokenDao extends BaseDao<JwtSecretToken, Long> {
    @Query(value = "SELECT secret_key FROM jwt_secret_token WHERE user_id = ?1"
            , nativeQuery = true)
    String getSecretKey(String userId);
}
