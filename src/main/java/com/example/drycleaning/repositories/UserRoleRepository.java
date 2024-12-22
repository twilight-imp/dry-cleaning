package com.example.drycleaning.repositories;

import com.example.drycleaning.models.Role;
import com.example.drycleaning.models.UserRoles;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends BaseRepository<Role> {
    Optional<Role> findRoleByName(UserRoles role);
}

