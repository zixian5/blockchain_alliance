package com.blockchain.alliance.response;

import com.blockchain.alliance.entity.Payment;
import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.Map;

public class GetDirectPaymentInfoIdReponse extends AbstractResponse{
    Payment payment = null;

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    public String toJSonString()
    {
        Map<String ,Object> map = new LinkedHashMap<>();
        map.put("code",ResponseCode.SUCCESS.getCode());
        map.put("data",payment);
        return new Gson().toJson(map);
    }
}
