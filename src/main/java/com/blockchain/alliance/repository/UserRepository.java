package com.blockchain.alliance.repository;

import com.blockchain.alliance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("from User u where  u.telephone =:telephone")
    User findByTelephone(@Param("telephone") String telephone);
}

