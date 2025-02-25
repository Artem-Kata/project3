package com.bank.authorization.Repositories;

import com.bank.authorization.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByProfileId(Long profileId);
}
