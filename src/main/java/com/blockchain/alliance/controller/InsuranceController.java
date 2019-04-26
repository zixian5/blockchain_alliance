package com.blockchain.alliance.controller;

import com.blockchain.alliance.entity.Detail;
import com.blockchain.alliance.entity.Insurance;
import com.blockchain.alliance.repository.DetailRepository;
import com.blockchain.alliance.repository.InsuranceRepository;
import com.blockchain.alliance.response.GetInsuranceDetailReponse;
import com.blockchain.alliance.response.GetInsuranceListResponse;
import com.blockchain.alliance.response.ResponseCode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InsuranceController {
    @Autowired
    private InsuranceRepository insuranceRepository;

    @Autowired
    private DetailRepository detailRepository;

    @RequestMapping("/insuranceList/getInsuranceList")
    @ResponseBody
    public String getInsuranceList()
    {
        List<Insurance> insuranceList = insuranceRepository.findAll();
        GetInsuranceListResponse response = new GetInsuranceListResponse();
        response.setCode(ResponseCode.SUCCESS.getCode());
        response.setInsuranceList(insuranceList);
        return response.getJsonString();
    }

    @RequestMapping("/insuranceDetail/getInsuranceDetail")
    @ResponseBody
    public String getInsuranceDetail(String insuranceId)
    {
        Detail detail = detailRepository.findByInsuranceId(insuranceId);
        GetInsuranceDetailReponse reponse = new GetInsuranceDetailReponse();
        reponse.setCode(ResponseCode.SUCCESS.getCode());
        reponse.setDetail(detail);
        return reponse.toJsonString();
    }

    public String submitInsurancePolicyForm(@RequestBody(required = false)String json)
    {
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        System.out.println(jsonObject);
        String insuranceId = jsonObject.get("insuranceId").getAsString();
        String insurancePurchaserName = jsonObject.get("insurancePurchaserName").getAsString();
        String insurancePurchaserIdentificationNumber = jsonObject.get("insurancePurchaserIdentificationNumber").getAsString();
        String email = jsonObject.get("email").getAsString();
        String insuranceName = jsonObject.get("insuredName").getAsString();
        Integer insuredIsMale = jsonObject.get("insuredIsMale").getAsInt();

        return null;
    }

}
