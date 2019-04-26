package com.blockchain.alliance.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Detail {
    @Id
    @Column(name = "idinsurance")
    private String insuranceId;
    private String insuranceName;
    private int isSpecialMedicalCare;
    private int hasSocialSecurity;
    private int insuranceAmount;
    private String insurancePeriod;
    private String insuranceDiseaseType;
    private String coveringAge;
    private int insurancePrice;

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    public int getIsSpecialMedicalCare() {
        return isSpecialMedicalCare;
    }

    public void setIsSpecialMedicalCare(int isSpecialMedicalCare) {
        this.isSpecialMedicalCare = isSpecialMedicalCare;
    }

    public int getHasSocialSecurity() {
        return hasSocialSecurity;
    }

    public void setHasSocialSecurity(int hasSocialSecurity) {
        this.hasSocialSecurity = hasSocialSecurity;
    }

    public int getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(int insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public String getInsurancePeriod() {
        return insurancePeriod;
    }

    public void setInsurancePeriod(String insurancePeriod) {
        this.insurancePeriod = insurancePeriod;
    }

    public String getInsuranceDiseaseType() {
        return insuranceDiseaseType;
    }

    public void setInsuranceDiseaseType(String insuranceDiseaseType) {
        this.insuranceDiseaseType = insuranceDiseaseType;
    }

    public String getCoveringAge() {
        return coveringAge;
    }

    public void setCoveringAge(String coveringAge) {
        this.coveringAge = coveringAge;
    }

    public int getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(int insurancePrice) {
        this.insurancePrice = insurancePrice;
    }
}
