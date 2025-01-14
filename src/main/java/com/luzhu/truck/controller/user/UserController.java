package com.luzhu.truck.controller.user;

import com.luzhu.truck.dto.GeoInfo;
import com.luzhu.truck.dto.user.LoginReqDto;
import com.luzhu.truck.dto.user.LoginResDto;
import com.luzhu.truck.dto.user.RegisterDto;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.user.RegisterService;
import com.luzhu.truck.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private RegisterService registerService;
    @Autowired
    private UserService userService;
    // 註冊
//    @ApiDecryptData
    @PostMapping("/register")
    public ResponseModel<LoginResDto> register(@RequestBody @Valid RegisterDto reqDto,
                                               HttpServletRequest request) {
        return new ResponseModel<>(registerService.register(reqDto, GeoInfo.create(request)));
    }

    @PostMapping("/login")
    public ResponseModel<LoginResDto> login(@RequestBody @Valid LoginReqDto reqDto,
                                            HttpServletRequest request) {
        return new ResponseModel<>(userService.login(reqDto, GeoInfo.create(request)));
    }
}
