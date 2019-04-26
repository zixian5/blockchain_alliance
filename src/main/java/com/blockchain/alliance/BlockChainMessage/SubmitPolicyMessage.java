package com.blockchain.alliance.BlockChainMessage;

import cn.xjfme.encrypt.test.SecurityTestAll;
import cn.xjfme.encrypt.utils.Util;
import cn.xjfme.encrypt.utils.sm2.SM2SignVO;
import com.blockchain.alliance.entity.Detail;
import com.blockchain.alliance.entity.Insurance;
import com.blockchain.alliance.entity.Policy;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static cn.xjfme.encrypt.test.SecurityTestAll.genSM2Signature;

/*
*
* data:|policy:
*      |insurance:
*
* sign:
* senderpubkey:
*
* */

public class SubmitPolicyMessage {
    Policy policy;
    Insurance insurance;
    Detail detail;
    String insuranceCompanyName;
    String senderPriKey;
    String senderPubkey;

    public String toJsonString()
    {
        Map<String,Object> data = new LinkedHashMap<>();
        data.put("insurance",getInsuranceMap());
        policy.setSign(policy.getInsuranceId());
        data.put("policy",policy);
        String signSource = new Gson().toJson(data);
        String sign =null;
        try {
             sign = sign(signSource,senderPriKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String,Object> map =new LinkedHashMap<>();
        map.put("data",data);
        map.put("sign",sign);
        map.put("senderpubkey",senderPubkey);
        return new Gson().toJson(map);
    }
    private String sign(String source,String senderPriKey) throws Exception {
        String s5 = Util.byteToHex(source.getBytes());
        SM2SignVO sign = genSM2Signature(senderPriKey, s5);
        String result = sign.getSm2_signForSoft();
        System.out.println("软加密签名结果:" + result);
        return result;
    }

    public String encrypt(String source ,String senderPubkey) throws IOException {
        String SM2Enc = SecurityTestAll.SM2Enc(senderPubkey, source);
        System.out.println("加密:");
        System.out.println("密文:" + SM2Enc);
        return SM2Enc;
    }

    private Map getInsuranceMap()
    {
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("insuranceId",insurance.getInsuranceId());
        map.put("insuranceSource",insurance.getInsuranceSource());
        map.put("insurance_duration",insurance.getInsuranceDuration());
        map.put("insurancePrice",insurance.getInsurancePrice());
        map.put("insuranceName",detail.getInsuranceName());
        map.put("isSpecialMedicalCare",detail.getIsSpecialMedicalCare());
        map.put("hasSocialSecurity",detail.getHasSocialSecurity());
        map.put("insuranceAmount",detail.getInsuranceAmount());
        map.put("insuranceDiseaseType",detail.getInsuranceDiseaseType());
        map.put(" coveringAge",detail.getCoveringAge());
        map.put("insuranceCompany",insuranceCompanyName);
        map.put("insurancePeriod",detail.getInsurancePeriod());
        return map;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public String getInsuranceCompanyName() {
        return insuranceCompanyName;
    }

    public void setInsuranceCompanyName(String insuranceCompanyName) {
        this.insuranceCompanyName = insuranceCompanyName;
    }

    public String getSenderPriKey() {
        return senderPriKey;
    }

    public void setSenderPriKey(String senderPriKey) {
        this.senderPriKey = senderPriKey;
    }

    public String getSenderPubkey() {
        return senderPubkey;
    }

    public void setSenderPubkey(String senderPubkey) {
        this.senderPubkey = senderPubkey;
    }
}
