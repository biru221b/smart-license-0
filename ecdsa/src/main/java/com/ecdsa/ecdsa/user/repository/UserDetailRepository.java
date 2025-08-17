package com.ecdsa.ecdsa.user.repository;

import com.ecdsa.ecdsa.user.model.dto.LoginDto;
import com.ecdsa.ecdsa.user.model.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail,Integer> {

    @Query("From UserDetail d where d.username=:username and d.password=:password")
    List<UserDetail> loginUser(@Param("username") String username, @Param("password") String password);
}
