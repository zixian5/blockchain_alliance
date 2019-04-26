package com.blockchain.alliance.repository;

import com.blockchain.alliance.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record,String> {
}
