package com.govtech.service;

import com.govtech.repository.UserRepository;
import com.govtech.repository.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getUsers(Double min, Double max, Long offset, Long limit, String sort) {
        return userRepository.findUsers(min, max, offset, limit, sort);
    }
}
