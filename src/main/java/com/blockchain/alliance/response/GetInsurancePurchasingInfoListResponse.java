package com.blockchain.alliance.response;

import com.blockchain.alliance.entity.Policy;
import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GetInsurancePurchasingInfoListResponse extends  AbstractResponse {
    private List<Policy> policies;

    public List<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(List<Policy> policies) {
        this.policies = policies;
    }
    public String getJsonStrig()
    {
        Map<String,Object> map =new LinkedHashMap<>();
        map.put("code",code);
        map.put("data",policies);
        return new Gson().toJson(map);
    }
}