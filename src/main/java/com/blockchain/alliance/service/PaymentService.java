package com.blockchain.alliance.service;

import com.blockchain.alliance.BlockChainMessage.SubmitHospitalConfirmFinalMessage;
import com.blockchain.alliance.entity.Payment;
import com.blockchain.alliance.entity.Record;
import com.blockchain.alliance.repository.PaymentRepository;
import com.blockchain.alliance.repository.PolicyRepository;
import com.blockchain.alliance.repository.RecordRepository;
import com.bubi.connect.ContractConnect;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static cn.xjfme.encrypt.test.SecurityTestAll.SM2Dec;

@Service
public class PaymentService {
    @Autowired
    private PolicyRepository policyRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private RecordRepository recordRepository;
    @Resource
    private Environment environment;

    public void updatePaymentFromBlockchain()
    {
        try {
            smellInsuranceConfirm();
            handleComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void smellInsuranceConfirm() throws IOException {
        String contract = environment.getProperty("payment.insurance.verify");
        String pubKey = environment.getProperty("insurance.company.pubkey");
        String priKey = environment.getProperty("insurance.company.prikey");
        String json = ContractConnect.get(pubKey,contract);
        System.out.println(json);
        JsonObject object = new JsonParser().parse(json).getAsJsonObject();
        JsonObject result = object.get("result").getAsJsonObject();
        if(result.get("value").getAsString().equals("false")){return ;}
        String[] encValues = result.get("value").getAsString().split(",");

        for(String encValue : encValues)
        {
            String value = SM2Dec(priKey,encValue);
            System.out.println(value);
            handleInsuranceConfirm(value);
        }
    }

    public void handleInsuranceConfirm(String value) {
        JsonObject jsonObject = new JsonParser().parse(value).getAsJsonObject();
        String paymentRawString = jsonObject.getAsJsonObject("data").get("payment").toString();
        Payment payment = new Gson().fromJson(paymentRawString, Payment.class);
        payment.setDirectPaymentStage(2);
        System.out.println("payment: " + payment);
        String policyId = jsonObject.getAsJsonObject("data").get("policyId").getAsString();
        System.out.println("policyId: " + policyId);

        if (policyRepository.findByInsurancePurchasingInfoId(policyId) == null) {
            return;
        }
        if (paymentRepository.findByDirectPaymentInfoId(policyId) != null) {
            return;
        }
        paymentRepository.saveAndFlush(payment);
        JsonArray jsonArray = jsonObject.getAsJsonObject("data").get("records").getAsJsonArray();
        System.out.println(jsonArray.toString());
        for (JsonElement jsonElement :jsonArray)
        {
            Record record =new Gson().fromJson(jsonElement,Record.class);
            record.setInsurancePurchasingInfoId(policyId);
            System.out.println("record: "+record);
            recordRepository.saveAndFlush(record);

        }

    }

    public void submitHospitalConfirmFinalMessage(String paymentId)
    {
        String contract = environment.getProperty("payment.hospital.confirm.payment");
        Payment payment = paymentRepository.findByDirectPaymentInfoId(paymentId);
        if(payment == null){return ;}
        String senderPubkey = environment.getProperty("insurance.company.pubkey");
        String senderPrikey = environment.getProperty("insurance.company.prikey");

        SubmitHospitalConfirmFinalMessage message = new SubmitHospitalConfirmFinalMessage();
        message.setPaymentId(paymentId);
        message.setSenderPrikey(senderPrikey);
        message.setSenderPubkey(senderPubkey);
        String source = message.toJsonString();
        System.out.println(source);

        String receiverPubkey = environment.getProperty("hospital.pubkey");
        String encSource = null;
        try {
            encSource = message.encrypt(source,receiverPubkey);
            System.out.println("encSource: "+encSource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ContractConnect.set(receiverPubkey,encSource,contract);

        receiverPubkey = payment.getPublicKey();
        encSource = null;
        try {
            encSource = message.encrypt(source,receiverPubkey);
            System.out.println("encSource: "+encSource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ContractConnect.set(receiverPubkey,encSource,contract);

    }

    public void submitInsuranceDecline(String paymentId)
    {
        String contract = environment.getProperty("payment.insurance.declined");
        Payment payment = paymentRepository.findByDirectPaymentInfoId(paymentId);
        if(payment == null){return ;}
        String senderPubkey = environment.getProperty("insurance.company.pubkey");
        String senderPrikey = environment.getProperty("insurance.company.prikey");

        SubmitHospitalConfirmFinalMessage message = new SubmitHospitalConfirmFinalMessage();
        message.setPaymentId(paymentId);
        message.setSenderPrikey(senderPrikey);
        message.setSenderPubkey(senderPubkey);
        String source = message.toJsonString();
        System.out.println(source);

        String receiverPubkey = environment.getProperty("hospital.pubkey");
        String encSource = null;
        try {
            encSource = message.encrypt(source,receiverPubkey);
            System.out.println("encSource: "+encSource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ContractConnect.set(receiverPubkey,encSource,contract);

        receiverPubkey = payment.getPublicKey();
        encSource = null;
        try {
            encSource = message.encrypt(source,receiverPubkey);
            System.out.println("encSource: "+encSource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ContractConnect.set(receiverPubkey,encSource,contract);
    }

    public void handleComplete() throws IOException {
        String pubKey = environment.getProperty("insurance.company.pubkey");
        String priKey = environment.getProperty("insurance.company.prikey");
        String contract = environment.getProperty("payment.complete");
        String json = ContractConnect.get(pubKey, contract);
        System.out.println(json);
        JsonObject object = new JsonParser().parse(json).getAsJsonObject();
        JsonObject result = object.get("result").getAsJsonObject();
        if (result.get("value").getAsString().equals("false")) {
            return;
        }

        String[] encValues = result.get("value").getAsString().split(",");

        for (String encValue : encValues) {
            String value = SM2Dec(priKey, encValue);
            System.out.println(value);
            JsonObject jsonObject = new JsonParser().parse(value).getAsJsonObject();
            String paymentId = jsonObject.getAsJsonObject("data").get("paymentId").getAsString();
            System.out.println("paymentId: " + paymentId);
            String publicKey = jsonObject.get("senderPubkey").getAsString();
            System.out.println("senderpubkey:  " + publicKey);
            String sign = jsonObject.get("sign").getAsString();
            System.out.println("sign:  " + sign);

            //应该有个sign验证过程
            Payment payment = paymentRepository.findByDirectPaymentInfoId(paymentId);
            payment.setDirectPaymentStage(4);
            paymentRepository.saveAndFlush(payment);
        }
    }
}
