package com.blockchain.alliance.response;

import com.blockchain.alliance.entity.Payment;
import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GetDirectPaymentInfoListResponse extends AbstractResponse{
    private List<Payment> payments ;

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
    public String toJsonString()
    {
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("code",code);
        Map<String,Object> data = new LinkedHashMap<>();
        data.put("directPaymentInfoList", payments);
        map.put("data",data);
        return  new Gson().toJson(map);
    }
}