package com.blockchain.alliance.controller;

import com.blockchain.alliance.entity.Payment;
import com.blockchain.alliance.repository.PaymentRepository;
import com.blockchain.alliance.response.GetDirectPaymentInfoIdReponse;
import com.blockchain.alliance.response.GetDirectPaymentInfoListResponse;
import com.blockchain.alliance.response.GetInsurancePurchasingInfoListResponse;
import com.blockchain.alliance.response.ResponseCode;
import com.blockchain.alliance.service.PaymentService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PaymentController {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentService paymentService;

    @RequestMapping("/directPaymentProcess/getDirectPaymentInfoList")
    @ResponseBody
    public String GetDirectPaymentInfoList()
    {
        try {
            paymentService.smellInsuranceConfirm();
            paymentService.handleComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GetDirectPaymentInfoListResponse response =new GetDirectPaymentInfoListResponse();
        response.setCode(ResponseCode.SUCCESS.getCode());
        List<Payment> payments = paymentRepository.findAll();
        response.setPayments(payments);
        return response.toJsonString();
    }

    @RequestMapping("/directPaymentDetail/getDirectPaymentInfo")
    @ResponseBody
    public String GetDirectPaymentInfo(String directPaymentInfoId )
    {
        try {
            paymentService.smellInsuranceConfirm();
            paymentService.handleComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ;
        Payment payment = paymentRepository.findByDirectPaymentInfoId(directPaymentInfoId);
        GetDirectPaymentInfoIdReponse reponse = new GetDirectPaymentInfoIdReponse();
        reponse.setPayment(payment);
        return  reponse.toJSonString();
    }

    @PostMapping("/directPaymentDetail/submitInsuranceCompanyVerifyAndPayResult")
    @ResponseBody
    public String submitInsuranceCompanyVerifyAndPayResult(@RequestBody(required = false) String json )
    {
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        String directPaymentInfoId = jsonObject.get("directPaymentInfoId").getAsString();
        Boolean verifyResult = jsonObject.get("verifyResult").getAsBoolean();
        Payment payment = paymentRepository.findByDirectPaymentInfoId(directPaymentInfoId);
        if(verifyResult) {
            payment.setDirectPaymentStage(3);
            paymentRepository.saveAndFlush(payment);
            paymentService.submitHospitalConfirmFinalMessage(payment.getDirectPaymentInfoId());
        }
        else {
            payment.setDirectPaymentStage(-2);
            paymentRepository.saveAndFlush(payment);
            paymentService.submitInsuranceDecline(payment.getDirectPaymentInfoId());
        }


        Map<String,Integer> map = new LinkedHashMap<>();
        map.put("code",ResponseCode.SUCCESS.getCode());

        return new Gson().toJson(map);
    }

    @ResponseBody
    @RequestMapping("/testIns")
    public String submitInsConfirm()
    {
        try {
            paymentService.smellInsuranceConfirm();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "200";
    }

    @ResponseBody
    @RequestMapping("/testFin")
    public String submitFinalConfirm(String paymentId)
    {
        paymentService.submitHospitalConfirmFinalMessage(paymentId);
        return "200";
    }
    @ResponseBody
    @RequestMapping("/testComp")
    public String testComp()
    {
        try {
            paymentService.handleComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "200";
    }
}
