package com.blockchain.alliance.repository;

import com.blockchain.alliance.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyRepository extends JpaRepository<Policy,String> {
    @Query("from Policy p where p.insurancePurchasingInfoId =:infoId")
    Policy findByInsurancePurchasingInfoId(@Param("infoId") String infoId);
}
