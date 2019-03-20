package com.blockchain.alliance.repository;

import com.blockchain.alliance.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<Policy,String> {
}
