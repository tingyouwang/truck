package com.luzhu.truck.controller.invoice;

import com.luzhu.truck.dto.insurancecompany.AddInsuranceComParam;
import com.luzhu.truck.dto.invoice.AddInvoiceParam;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.invoice.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;
    @PostMapping("/addInvoice")
    public ResponseModel<Object> addInsuranceCompany(@RequestBody @Valid AddInvoiceParam param) {
        invoiceService.addInvoice(param);

        return new ResponseModel<>();
    }

}
