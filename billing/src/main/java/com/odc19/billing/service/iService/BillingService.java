package com.odc19.billing.service.iService;

import com.odc19.clients.billing.BillDTO;
import com.odc19.clients.billing.BillDetailsDTO;

import java.util.List;

public interface BillingService {
    void saveNewBill(BillDTO billDTO);

    BillDTO updateBill(BillDTO billDTO);

    List<BillDTO> getAll();

    BillDTO getBillById(String billId);

    void deleteBill(String billId);

    BillDTO recoverBillIsDeleted(String billId);

    List<BillDTO> getBillForShipper(String shipperId);

    BillDetailsDTO getBillDetailById(String billId);

    List<BillDetailsDTO> getBillDetailList();

    List<BillDetailsDTO> getBillDetailByShipperId(String shipperId);

    void deliveredBillById(String billId);
}
