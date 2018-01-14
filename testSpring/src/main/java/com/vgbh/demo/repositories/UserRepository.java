package com.vgbh.demo.repositories;

import com.vgbh.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>{



}
