package com.luzhu.truck.dao.key;

import com.luzhu.truck.dao.BaseDao;
import com.luzhu.truck.entity.key.RsaKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface RsaKeyDao extends BaseDao<RsaKey, Integer> {
    @Query(value = "SELECT public FROM rsa_key WHERE ID = 1", nativeQuery = true)
    String getPublicKey();
}
