package com.blockchain.alliance.repository;

import com.blockchain.alliance.entity.Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailRepository extends JpaRepository<Detail, String> {
    @Query("from Detail d where d.insuranceId=:insuranceid")
    Detail findByInsuranceId(@Param("insuranceid") String insuranceId);
}
