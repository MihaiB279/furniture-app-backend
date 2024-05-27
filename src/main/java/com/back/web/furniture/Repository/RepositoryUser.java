package com.back.web.furniture.Repository;

import com.back.web.furniture.Domain.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface RepositoryUser extends JpaRepository<User, UUID> {
    public Optional<User> findByUsername(String username);
}
