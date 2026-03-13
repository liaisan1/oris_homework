package com.oris_spring.persistence.repository;

import com.oris_spring.persistence.entity.UserEntity;

import java.util.UUID;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {
}