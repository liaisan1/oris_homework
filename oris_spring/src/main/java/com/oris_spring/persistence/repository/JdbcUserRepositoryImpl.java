package com.oris_spring.persistence.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.oris_spring.persistence.entity.UserEntity;

import java.time.LocalDate;
import java.util.*;

@Profile("local")
@Repository
@RequiredArgsConstructor
@Primary
@Slf4j
public class JdbcUserRepositoryImpl implements UserRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<UserEntity> userMapper = (rs, rowNum) ->
            UserEntity.builder()
                    .id(UUID.fromString(rs.getString("id")))
                    .name(rs.getString("name"))
                    .birthDate(rs.getObject("birth_date", LocalDate.class))
                    .build();

    @Override
    public void save(UserEntity user) {
        String sql = "INSERT INTO account (id, name, birth_date) VALUES (:id, :name, :birthDate)";

        Map<String, Object> params = new HashMap<>();
        params.put("id", user.getId() != null ? user.getId() : UUID.randomUUID());
        params.put("name", user.getName());
        params.put("birthDate", user.getBirthDate());

        try {
            namedParameterJdbcTemplate.update(sql, params);
        } catch (DataAccessException e) {
            log.error("Error saving user: {}", user, e);
            throw new RuntimeException("Failed to save user", e);
        }
    }

    @Override
    public Optional<UserEntity> getById(UUID uuid) {
        String sql = "SELECT * FROM account WHERE id = :id";

        Map<String, Object> params = Collections.singletonMap("id", uuid);

        try {
            UserEntity entity = namedParameterJdbcTemplate.queryForObject(sql, params, userMapper);
            return Optional.ofNullable(entity);
        } catch (EmptyResultDataAccessException e) {
            log.warn("User with id {} not found", uuid);
            return Optional.empty();
        } catch (DataAccessException e) {
            log.error("Error fetching user by id: {}", uuid, e);
            throw new RuntimeException("Failed to fetch user", e);
        }
    }

    @Override
    public List<UserEntity> getAll() {
        String sql = "SELECT * FROM account";

        try {
            return namedParameterJdbcTemplate.query(sql, userMapper);
        } catch (DataAccessException e) {
            log.error("Error fetching all users", e);
            throw new RuntimeException("Failed to fetch users", e);
        }
    }

    @Override
    public void update(UserEntity user) {
        String sql = "UPDATE account SET name = :name, birth_date = :birthDate WHERE id = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", user.getId());
        params.put("name", user.getName());
        params.put("birthDate", user.getBirthDate());

        try {
            int updatedRows = namedParameterJdbcTemplate.update(sql, params);
            if (updatedRows == 0) {
                log.warn("No user found with id {} to update", user.getId());
            }
        } catch (DataAccessException e) {
            log.error("Error updating user: {}", user, e);
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public boolean deleteById(UUID uuid) {
        String sql = "DELETE FROM account WHERE id = :id";

        Map<String, Object> params = Collections.singletonMap("id", uuid);

        try {
            int deletedRows = namedParameterJdbcTemplate.update(sql, params);
            return deletedRows > 0;
        } catch (DataAccessException e) {
            log.error("Error deleting user with id: {}", uuid, e);
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM account";

        try {
            int deletedRows = namedParameterJdbcTemplate.update(sql, Collections.emptyMap());
            log.info("Deleted {} users", deletedRows);
        } catch (DataAccessException e) {
            log.error("Error deleting all users", e);
            throw new RuntimeException("Failed to delete all users", e);
        }
    }

    // Дополнительный метод для поиска по нескольким ID (пример работы с IN)
    public List<UserEntity> findByIds(List<UUID> ids) {
        String sql = "SELECT * FROM account WHERE id IN (:ids)";

        Map<String, Object> params = Collections.singletonMap("ids", ids);

        try {
            return namedParameterJdbcTemplate.query(sql, params, userMapper);
        } catch (DataAccessException e) {
            log.error("Error fetching users by ids", e);
            throw new RuntimeException("Failed to fetch users by ids", e);
        }
    }
}