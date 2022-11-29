package com.odc19.billing.controller;

import com.odc19.billing.service.iService.BillingService;
import com.odc19.clients.billing.BillDTO;
import com.odc19.clients.billing.BillDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bill")
public class BillController {

    @Autowired
    BillingService billingService;

    @PostMapping("/new")
    public void createNewBill(@RequestBody BillDTO billDTO) {
        billingService.saveNewBill(billDTO);
    }

    @PutMapping("/update")
    public BillDTO updateBill(@RequestBody BillDTO billDTO) {
        return billingService.updateBill(billDTO);
    }

    @GetMapping("/list")
    public List<BillDTO> getAllBill() {
        return billingService.getAll();
    }

    @GetMapping("/{billId}")
    public BillDTO getBillById(@PathVariable String billId) {
        return billingService.getBillById(billId);
    }

    @GetMapping("/shipper/{shipperId}")
    public List<BillDTO> getBillByShipperId(@PathVariable String shipperId) {
        return billingService.getBillForShipper(shipperId);
    }

    @GetMapping("/detail/{billId}")
    public BillDetailsDTO getBillDetailsByBillId(@PathVariable String billId) {
        return billingService.getBillDetailById(billId);
    }

    @GetMapping("/detail/list")
    public List<BillDetailsDTO> getBillDetailsList() {
        return billingService.getBillDetailList();
    }

    @GetMapping("/detail/shipper/{shipperId}")
    public List<BillDetailsDTO> getBillDetailsByShipper(@PathVariable String shipperId) {
        return billingService.getBillDetailByShipperId(shipperId);
    }

    @PutMapping("/delivered/{billId}")
    public void confirmedDeliveredBillById(@PathVariable String billId) {
        billingService.deliveredBillById(billId);
    }
}
