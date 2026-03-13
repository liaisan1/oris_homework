package com.oris_spring.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.oris_spring.persistence.entity.UserEntity;
import com.oris_spring.persistence.repository.InMemoryUserRepositoryImpl;
import com.oris_spring.persistence.repository.UserRepository;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public void save(String name, LocalDate birthDate) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Name must not be null or empty");
        }

        UserEntity user = UserEntity.builder()
                .name(name)
                .birthDate(birthDate)
                .build();

        repository.save(user);
    }

    public UserEntity get(UUID id) {
        return repository.getById(id).orElse(null);
    }
}