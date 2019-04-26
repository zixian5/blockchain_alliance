package com.blockchain.alliance.response;

import com.blockchain.alliance.entity.Detail;
import com.blockchain.alliance.entity.Insurance;
import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.Map;

public class GetInsuranceDetailReponse extends AbstractResponse{
    private Detail detail;

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public String toJsonString()
    {
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("code",ResponseCode.SUCCESS.getCode());
        map.put("data",detail);
        return new Gson().toJson(map);
    }
}
