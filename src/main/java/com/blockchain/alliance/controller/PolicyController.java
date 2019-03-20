package com.blockchain.alliance.controller;

import com.blockchain.alliance.entity.Policy;
import com.blockchain.alliance.repository.PolicyRepository;
import com.blockchain.alliance.response.GetInsuranceListResponse;
import com.blockchain.alliance.response.GetInsurancePurchasingInfoListResponse;
import com.blockchain.alliance.response.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/insurancePurchasingProcess")
public class PolicyController {
    @Autowired
    private PolicyRepository policyRepository;

    @RequestMapping("/getInsurancePurchasingInfoList")
    @ResponseBody
    public String getInsurancePurchasingInfoList()
    {
        List<Policy> policies = policyRepository.findAll();
        GetInsurancePurchasingInfoListResponse getInsurancePurchasingInfoListResponse = new GetInsurancePurchasingInfoListResponse();
        getInsurancePurchasingInfoListResponse.setCode(ResponseCode.SUCCESS.getCode());
        getInsurancePurchasingInfoListResponse.setPolicies(policies);
        return getInsurancePurchasingInfoListResponse.getJsonStrig();
    }
}
