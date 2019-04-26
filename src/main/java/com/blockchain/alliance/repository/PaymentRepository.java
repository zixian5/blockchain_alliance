package com.blockchain.alliance.repository;


import com.blockchain.alliance.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,String> {
    @Query("from Payment p where p.directPaymentInfoId =:directPaymentInfoId")
    Payment findByDirectPaymentInfoId (@Param("directPaymentInfoId") String directPaymentInfoId );
}
