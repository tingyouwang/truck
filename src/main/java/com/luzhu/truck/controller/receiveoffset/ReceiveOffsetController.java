package com.luzhu.truck.controller.receiveoffset;

import com.luzhu.truck.dto.LicenseAndExpenseYearMonthParam;
import com.luzhu.truck.dto.receiveoffset.AddReceiveOffsetParam;
import com.luzhu.truck.dto.receiveoffset.UpdateReceiveOffsetParam;
import com.luzhu.truck.entity.receiveoffset.ReceiveOffset;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.receiveoffset.ReceiveOffsetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receiveOffset")
@CrossOrigin("*")
public class ReceiveOffsetController {
    @Autowired
    private ReceiveOffsetService receiveOffsetService;
    @PostMapping("/addReceiveOffset")
    public ResponseModel<Object> addReceiveOffset(@RequestBody @Valid AddReceiveOffsetParam param) {
        receiveOffsetService.addReceiveOffset(param);

        return new ResponseModel<>();
    }

    @PostMapping("/updateReceiveOffset")
    public ResponseModel<Object> updateReceiveOffset(@RequestBody @Valid UpdateReceiveOffsetParam param) {
        receiveOffsetService.updateReceiveOffset(param);

        return new ResponseModel<>();
    }

    @PostMapping("/getReceiveOffset")
    public ResponseModel<PageResult<ReceiveOffset>> getReceiveOffsetList(@RequestBody @Valid LicenseAndExpenseYearMonthParam param) {
        return new ResponseModel<>(receiveOffsetService.getReceiveOffsetList(param));
    }
}
