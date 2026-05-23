package com.luzhu.truck.controller.invoice;

import com.luzhu.truck.dto.invoice.AddInvoiceParam;
import com.luzhu.truck.dto.invoice.GetInvoiceParam;
import com.luzhu.truck.dto.invoice.UpdateInvoiceParam;
import com.luzhu.truck.dto.invoice.VoidAndRebillToMonthParam;
import com.luzhu.truck.entity.invoice.Invoice;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.invoice.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/invoice")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;
    @PostMapping("/addInvoice")
    public ResponseModel<Object> addInvoice(@RequestBody @Valid AddInvoiceParam param) {
        invoiceService.addInvoice(param);

        return new ResponseModel<>();
    }

    @PostMapping("/getInvoice")
    public ResponseModel<PageResult<Invoice>> getInvoice(@RequestBody @Valid GetInvoiceParam param) {
        PageResult<Invoice> invoice = invoiceService.getInvoiceByType(param);
        return new ResponseModel<>(invoice);
    }

    @PostMapping("/updateInvoice")
    public ResponseModel<Object> updateInvoice(@RequestBody @Valid UpdateInvoiceParam param) {
        invoiceService.updateInvoice(param);

        return new ResponseModel<>();
    }

    @PostMapping("/voidAndRebillToMonth")
    public ResponseModel<Object> voidAndRebillToMonth(@RequestBody @Valid VoidAndRebillToMonthParam param) {
        invoiceService.voidAndRebillToMonth(param);
        return new ResponseModel<>();
    }

}
