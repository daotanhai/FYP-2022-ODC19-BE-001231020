package com.odc19.billing.paypal;

import com.odc19.clients.billing.BillClient;
import com.odc19.clients.billing.BillDTO;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaypalClient {

    @Autowired
    BillClient billClient;

    private final String clientId = "AeiC_4wHg8KKCYnpFl5ww9-fZZFIeGJpFxXMwgDxrmbclRdVpsbKK34QGd8pRaLjJmp5pipteMW41Z3R";
    private final String clientSecret = "EC_SP3bWWN6mPtwaqzO8CpAHW6Y68S0ZPX3I89vWy4zL72uTt1kvrr1DLesX2LMXaqoisPThW9hZiJnQ";
    private final String cancelUrl = "http://localhost:3000/cancel";
    private final String returnUrl = "http://localhost:3000";

    public Map<String, Object> createPayment(String sum){
        Map<String, Object> response = new HashMap<String, Object>();
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(sum);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(returnUrl);
        payment.setRedirectUrls(redirectUrls);
        Payment createdPayment;
        try {
            String redirectUrl = "";
            APIContext context = new APIContext(clientId, clientSecret, "sandbox");
            createdPayment = payment.create(context);
            if(createdPayment!=null){
                List<Links> links = createdPayment.getLinks();
                for (Links link:links) {
                    if(link.getRel().equals("approval_url")){
                        redirectUrl = link.getHref();
                        break;
                    }
                }
                response.put("status", "success");
                response.put("redirect_url", redirectUrl);
            }
        } catch (PayPalRESTException e) {
            System.out.println("Error happened during payment creation!");
        }
        return response;
    };

    public Map<String, Object> completePayment(HttpServletRequest req, BillDTO billDTO){
        Map<String, Object> response = new HashMap<>();
        Payment payment = new Payment();
        payment.setId(req.getParameter("paymentId"));

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(req.getParameter("PayerID"));
        try {
            APIContext context = new APIContext(clientId, clientSecret, "sandbox");
            Payment createdPayment = payment.execute(context, paymentExecution);
            if(createdPayment!=null){
                response.put("status", "success");
                response.put("payment", createdPayment);
//                call again the update bill API to say that customer has paid the bill
                billClient.updateBill(billDTO);
            }
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
        }
        return response;
    }
}
