package com.blockchain.alliance.entity;

import cn.xjfme.encrypt.test.SecurityTestAll;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Insurance {
    @Id
    @Column(name = "idinsurance")
    private String insuranceId;
    @Column(name = "insurance_source")
    private String insuranceSource;
    @Column(name = "insurance_duration")
    private String insuranceDuration;
    @Column(name = "insurancePrice")
    private Integer insurancePrice;

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getInsuranceSource() {
        return insuranceSource;
    }

    public void setInsuranceSource(String insuranceSource) {
        this.insuranceSource = insuranceSource;
    }

    public String getInsuranceDuration() {
        return insuranceDuration;
    }

    public void setInsuranceDuration(String insuranceDuration) {
        this.insuranceDuration = insuranceDuration;
    }

    public Integer getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(Integer insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public String genetateInsuranceId()
    {
        String src = insuranceSource+insuranceDuration+insurancePrice.toString();
        return SecurityTestAll.generateSM3HASH(src).substring(0,32);
    }
}
