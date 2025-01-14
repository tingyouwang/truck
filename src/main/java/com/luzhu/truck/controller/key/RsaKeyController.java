package com.luzhu.truck.controller.key;

import com.luzhu.truck.dto.key.RsaPublicKeyDto;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.key.RsaKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/key")
public class RsaKeyController {
    @Autowired
    private RsaKeyService rsaKeyService;

    @PostMapping("/getKey")
    public ResponseModel<RsaPublicKeyDto> getPublicKey() {

        return new ResponseModel<>(rsaKeyService.getPublicKey());
    }
}
