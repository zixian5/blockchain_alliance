package com.blockchain.alliance.controller;

import cn.xjfme.encrypt.test.SecurityTestAll;
import com.blockchain.alliance.entity.Detail;
import com.blockchain.alliance.entity.Insurance;
import com.blockchain.alliance.entity.Policy;
import com.blockchain.alliance.repository.DetailRepository;
import com.blockchain.alliance.repository.InsuranceRepository;
import com.blockchain.alliance.repository.PolicyRepository;
import com.blockchain.alliance.response.GetInsurancePurchasingInfoListResponse;
import com.blockchain.alliance.response.ResponseCode;
import com.blockchain.alliance.service.PolicyService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class PolicyController {
    @Autowired
    private PolicyRepository policyRepository;
    @Autowired
    private PolicyService policyService;
    @Autowired
    private InsuranceRepository insuranceRepository;
    @Autowired
    private DetailRepository detailRepository;

    @RequestMapping("/insurancePurchasingProcess/getInsurancePurchasingInfoList")
    @ResponseBody
    public String getInsurancePurchasingInfoList()
    {
        List<Policy> policies = policyRepository.findAll();
        GetInsurancePurchasingInfoListResponse getInsurancePurchasingInfoListResponse = new GetInsurancePurchasingInfoListResponse();
        getInsurancePurchasingInfoListResponse.setCode(ResponseCode.SUCCESS.getCode());
        getInsurancePurchasingInfoListResponse.setPolicies(policies);
        return getInsurancePurchasingInfoListResponse.getJsonStrig();
    }

    @ResponseBody
    @RequestMapping("/insurancePurchasingDetail/getInsurancePurchasingInfo")
    public String getInsurancePurchasingInfo(String insurancePurchasingInfoId)
    {
        Optional<Policy> optional = policyRepository.findById(insurancePurchasingInfoId);
        Policy policy = optional.get();

        Map<String,Object> map = new LinkedHashMap<>();
        map.put("code",ResponseCode.SUCCESS.getCode());
        map.put("data",policy);
        return new Gson().toJson(map);
    }

    @PostMapping(value = "/insurancePurchasingDetail/submitInsuranceCompanyVerifyResult",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String submitInsuranceCompanyVerifyResult(@RequestBody(required = false) String json)
    {
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        String insurancePurchasingInfoId = jsonObject.get("insurancePurchasingInfoId").getAsString();
        Boolean verifyResult = jsonObject.get("verifyResult").getAsBoolean();
        Policy policy = policyRepository.findById(insurancePurchasingInfoId).get();

        if(verifyResult == true)
        {
            policy.setInsurancePurchasingStage(3);
            policyRepository.saveAndFlush(policy);
        }
        else
        {
            policy.setInsurancePurchasingStage(-1);
            policyRepository.saveAndFlush(policy);
        }
        Map<String,Object> map =new HashMap<>();
        map.put("code",ResponseCode.SUCCESS.getCode());
        return new Gson().toJson(map);
    }

    @RequestMapping("/submitTest")
    @ResponseBody
    public String submitTest(String insurancePurchasingInfoId )
    {
        Policy policy = policyRepository.findById(insurancePurchasingInfoId).get();

        if(true)
        {
            policy.setInsurancePurchasingStage(3);
            policyRepository.saveAndFlush(policy);
            policyService.submitPolicyToBlockChain(insurancePurchasingInfoId);
        }
        else
        {
            policy.setInsurancePurchasingStage(-1);
            policyRepository.saveAndFlush(policy);
        }
        Map<String,Object> map =new HashMap<>();
        map.put("code",ResponseCode.SUCCESS.getCode());
        return new Gson().toJson(map);
    }

    @RequestMapping("/insurancePurchasingDetail/submitPayConfirmation")
    @ResponseBody
    public String submitPayConfirmation(@RequestBody(required = false) String json)
    {
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        String insurancePurchasingInfoId = jsonObject.get("insurancePurchasingInfoId").getAsString();
        Policy policy = policyRepository.findById(insurancePurchasingInfoId).get();
        policyRepository.saveAndFlush(policy);
        Map<String,Object> map =new HashMap<>();
        map.put("code",ResponseCode.SUCCESS.getCode());
        return new Gson().toJson(map);
    }

    @ResponseBody
    @PostMapping("/insurancePolicyFilling/submitInsurancePolicyForm")
    public String submitInsurancePolicyForm(@RequestBody String json)
    {
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        System.out.println(jsonObject);
        String insuranceId = jsonObject.get("insuranceId").getAsString();
        String insurancePurchaserName = jsonObject.get("insurancePurchaserName").getAsString();
        Integer isMale = jsonObject.get("isMale").getAsInt();
        Integer age = jsonObject.get("age").getAsInt();
        String healthState = jsonObject.get("healthState").getAsString();
        String publicKey = jsonObject.get("publicKey").getAsString();

        Insurance insurance = insuranceRepository.findByInsuranceId(insuranceId);
        Detail detail = detailRepository.findByInsuranceId(insuranceId);

        Policy policy = new Policy();
        policy.setAge(age);
        policy.setHealthState(healthState);
        policy.setInsuranceId(insuranceId);
        policy.setName(insurancePurchaserName);
        policy.setPublicKey(publicKey);
        policy.setIsMale(isMale);
        policy.setInsurancePurchasingStage(1);
        policy.setInsurancePrice(insurance.getInsurancePrice());
        policy.setInsurancePeriod(detail.getInsurancePeriod());
        policy.setInsuranceType(detail.getInsuranceDiseaseType());

        Date date = new Date();
        DateFormat df6 = new SimpleDateFormat("yyyy年MM月dd日");
        policy.setInsurancePurchasingTime(df6.format(date));
        policy.setInsurancePrice(insurance.getInsurancePrice());
        policy.setResponsiblePersonId(new Random().nextInt());
        policy.setResponsiblePersonName("皮卡丘");
        policy.setInsurancePurchasingInfoId(SecurityTestAll.generateSM3HASH(policy.toString()));

        policyRepository.saveAndFlush(policy);

        Map<String,Object> map = new LinkedHashMap<>();
        map.put("code",ResponseCode.SUCCESS.getCode());

        return new Gson().toJson(map);
    }

}
