package com.blockchain.alliance.repository;

import com.blockchain.alliance.entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, String> {

    @Query("from Insurance i where i.insuranceId =:ind")
    Insurance findByInsuranceId(@Param("ind") String insuranceId);
}
