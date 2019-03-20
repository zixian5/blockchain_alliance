package com.blockchain.alliance.controller;

import com.blockchain.alliance.entity.Insurance;
import com.blockchain.alliance.repository.InsuranceRepository;
import com.blockchain.alliance.response.GetInsuranceListResponse;
import com.blockchain.alliance.response.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/insuranceList")
public class InsuranceController {
    @Autowired
    private InsuranceRepository insuranceRepository;

    @RequestMapping("/getInsuranceList")
    @ResponseBody
    public String getInsuranceList()
    {
        List<Insurance> insuranceList = insuranceRepository.findAll();
        GetInsuranceListResponse response = new GetInsuranceListResponse();
        response.setCode(ResponseCode.SUCCESS.getCode());
        response.setInsuranceList(insuranceList);
        return response.getJsonString();
    }

}
