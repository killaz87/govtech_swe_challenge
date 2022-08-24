package com.govtech.repository;

import com.govtech.repository.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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

        //Conditioning logic
        sql += "WHERE salary BETWEEN "+min+" AND "+max;

        //Ordering logic
        if("name".equalsIgnoreCase(sort))
        {
            sql += "order by name asc";
        }
        else if("salary".equalsIgnoreCase(sort))
        {
            sql += "order by salary asc";
        }

        //Limiting logic
        if(limit != null && limit >= 0)
        {
            sql += "limit "+limit;
        }

        //Offsetting logic
        if(offset != null && offset >= 0)
        {
            sql += "offset "+offset;
        }

        List<User> result = jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class));

        return result;
    }
}