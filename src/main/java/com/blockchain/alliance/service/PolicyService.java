package com.blockchain.alliance.service;

import com.blockchain.alliance.BlockChainMessage.SubmitPolicyMessage;
import com.blockchain.alliance.entity.Detail;
import com.blockchain.alliance.entity.Insurance;
import com.blockchain.alliance.entity.Policy;
import com.blockchain.alliance.repository.DetailRepository;
import com.blockchain.alliance.repository.InsuranceRepository;
import com.blockchain.alliance.repository.PolicyRepository;
import com.bubi.connect.ContractConnect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Service
public class PolicyService {
    @Autowired
    private PolicyRepository policyRepository;
    @Autowired
    private InsuranceRepository insuranceRepository;
    @Autowired
    private DetailRepository detailRepository;
    @Resource
    private Environment environment;

    public void submitPolicyToBlockChain(String insurancePurchasingInfoId){
        Policy policy = policyRepository.findByInsurancePurchasingInfoId(insurancePurchasingInfoId);
        String insuranceId = policy.getInsuranceId();
        Insurance insurance = insuranceRepository.findByInsuranceId(insuranceId);
        Detail detail = detailRepository.findByInsuranceId(insuranceId);
        String senderPriKey = environment.getProperty("insurance.company.pubkey");
        String senderPubkey = environment.getProperty("insurance.company.prikey");
        String companyName = environment.getProperty("insurance.company.name");
        String receiverPubkey = policy.getPublicKey();
        String contract = environment.getProperty("insurance.contract");

        SubmitPolicyMessage message = new SubmitPolicyMessage();
        message.setDetail(detail);
        message.setInsurance(insurance);
        message.setInsuranceCompanyName(companyName);
        message.setPolicy(policy);
        message.setSenderPriKey(senderPriKey);
        message.setSenderPubkey(senderPubkey);

        String result = message.toJsonString();
        System.out.println(result);
        String encResult = null;
        try {
             encResult = message.encrypt(result,receiverPubkey);
            System.out.println("encResult"+encResult);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ContractConnect.set(receiverPubkey,encResult,contract);

    }
}
