package com.oris_spring.persistence.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import com.oris_spring.persistence.entity.UserEntity;

import java.util.*;

@Profile("dev")
@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private final Map<UUID, UserEntity> userMap = new HashMap<>();

    @Override
    public void save(UserEntity user) {
        if (Objects.isNull(user.getId())) {
            user.setId(UUID.randomUUID());
            userMap.put(user.getId(), user);
        } else {
            Optional.ofNullable(userMap.get(user.getId()))
                    .map(existed -> {
                        existed.setName(user.getName());
                        existed.setBirthDate(user.getBirthDate());
                        return existed;
                    })
                    .orElse(userMap.put(user.getId(), user));
        }
    }

    @Override
    public Optional<UserEntity> getById(UUID id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public List<UserEntity> getAll() {
        return userMap.values().stream().toList();
    }

    @Override
    public void update(UserEntity user) {
        UserEntity entity = userMap.getOrDefault(user.getId(), null);
        if (user.getId() == null || entity == null) {
            save(user);
        } else {
            entity.setBirthDate(user.getBirthDate());
            entity.setName(user.getName());
            userMap.put(user.getId(), entity);
        }
    }

    @Override
    public boolean deleteById(UUID id) {
        return !Objects.isNull(userMap.remove(id));
    }

    @Override
    public void deleteAll() {
        // not implemented yet;
    }
}