package com.govtech.repository;

import com.govtech.repository.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public User findById(long id) {
        return jdbcTemplate.queryForObject("select * from users where id=?", new BeanPropertyRowMapper<>(User.class), id);
    }

    public List<User> findUsers(Double min, Double max, Long offset, Long limit, String sort) {

        String sql = "SELECT * FROM users ";
        List<Object> parameters = new ArrayList<>();
        List<Integer> parameterTypes = new ArrayList<>();

        //Conditioning logic
        sql += "WHERE salary BETWEEN ? AND ? ";
        parameters.add(min);
        parameters.add(max);
        parameterTypes.add(Types.DOUBLE);
        parameterTypes.add(Types.DOUBLE);

        //Ordering logic
        if("name".equalsIgnoreCase(sort))
        {
            sql += "order by name asc ";
        }
        else if("salary".equalsIgnoreCase(sort))
        {
            sql += "order by salary asc ";
        }

        //Limiting logic
        if(limit != null && limit >= 0)
        {
            sql += "limit ? ";
            parameters.add(limit);
            parameterTypes.add(Types.INTEGER);
        }

        //Offsetting logic
        if(offset != null && offset >= 0)
        {
            sql += "offset ? ";
            parameters.add(offset);
            parameterTypes.add(Types.INTEGER);
        }

        int[] parameterTypesArray = new int[parameterTypes.size()];
        for (int i = 0;i < parameterTypes.size();i++) {
            parameterTypesArray[i] = parameterTypes.get(i);
        }

        List<User> result = jdbcTemplate.query(sql, parameters.toArray(), parameterTypesArray, new BeanPropertyRowMapper(User.class));

        return result;
    }

    public int[] updateUsers(List<User> toUpdateUsers) {

        return jdbcTemplate.batchUpdate("UPDATE users SET salary = ? WHERE name = UPPER(?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setDouble(1, toUpdateUsers.get(i).getSalary());
                        ps.setString(2, toUpdateUsers.get(i).getName());
                    }
                    @Override
                    public int getBatchSize() {
                        return toUpdateUsers.size();
                    }
                });
    }

    public int[] insertNewUsers(List<User> toInsertUsers) {
        return jdbcTemplate.batchUpdate("INSERT INTO users VALUES (UPPER(?), ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, toInsertUsers.get(i).getName());
                        ps.setDouble(2, toInsertUsers.get(i).getSalary());
                    }
                    @Override
                    public int getBatchSize() {
                        return toInsertUsers.size();
                    }
                });
    }

    public List<User> findAllUsers() {
        String sql = "SELECT * FROM users ";

        List<User> result = jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class));

        return result;
    }
}