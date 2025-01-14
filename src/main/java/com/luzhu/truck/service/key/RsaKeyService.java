package com.luzhu.truck.service.key;

import com.luzhu.truck.dao.key.RsaKeyDao;
import com.luzhu.truck.dto.key.RsaPublicKeyDto;
import com.luzhu.truck.entity.key.RsaKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RsaKeyService {
    @Autowired
    private RsaKeyDao rsaKeyDao;
    public RsaPublicKeyDto getPublicKey() {
        String publicKey = rsaKeyDao.getPublicKey();
        RsaPublicKeyDto rsaPublicKeyDto = new RsaPublicKeyDto();
        rsaPublicKeyDto.setRsaPublicKey(publicKey);
        return rsaPublicKeyDto;
    }
}
