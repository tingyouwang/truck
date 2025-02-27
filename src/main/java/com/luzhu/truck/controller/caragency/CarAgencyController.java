package com.luzhu.truck.controller.caragency;

import com.luzhu.truck.dto.BaseParam;
import com.luzhu.truck.dto.caragency.AddCarAgencyParam;
import com.luzhu.truck.dto.caragency.CarAgencyDropDownDTO;
import com.luzhu.truck.dto.caragency.UpdateCarAgencyParam;
import com.luzhu.truck.entity.caragency.CarAgency;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.caragency.CarAgencyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carAgency")
@CrossOrigin("*")
public class CarAgencyController {
    @Autowired
    private CarAgencyService carAgencyService;

    @PostMapping("/getCarAgency")
    public ResponseModel<PageResult<CarAgency>> getCarAgency(@RequestBody BaseParam param) {
        PageResult<CarAgency> carAgency = carAgencyService.getCarAgency(param);

        return new ResponseModel<>(carAgency);
    }

    @PostMapping("/dropDownGetCarAgency")
    public ResponseModel<List<CarAgencyDropDownDTO>> dropDownGetCarAgency() {
        List<CarAgencyDropDownDTO> carAgencyDropDownDTOS = carAgencyService.dropDownGetCarAgency();

        return new ResponseModel<>(carAgencyDropDownDTOS);
    }

    @PostMapping("/addCarAgency")
    public ResponseModel<Object> addCarAgency(@RequestBody @Valid AddCarAgencyParam addCarAgencyParam) {
        carAgencyService.addCarAgency(addCarAgencyParam);

        return new ResponseModel<>();
    }
    @PostMapping("/updateCarAgency")
    public ResponseModel<Object> updateCarAgency(@RequestBody @Valid UpdateCarAgencyParam updateCarAgencyParam) {
        carAgencyService.updateCarAgency(updateCarAgencyParam);

        return new ResponseModel<>();
    }
    @PostMapping("/deleteCarAgency/{id}")
    public ResponseModel<Object> deleteCarAgency(@PathVariable int id) {
        carAgencyService.deleteCarAgency(id);

        return new ResponseModel<>();
    }
}
