package com.govtech.service;

import com.govtech.repository.UserRepository;
import com.govtech.repository.bean.User;
import com.govtech.util.CSVHelper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getUsers(Double min, Double max, Long offset, Long limit, String sort) {
        return userRepository.findUsers(min, max, offset, limit, sort);
    }

    @SneakyThrows
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean performUpload(MultipartFile file) {
        try {
            List<User> inputUsers = CSVHelper.csvToUsers(file.getInputStream());

            //Logic to insert into db, replacing the ones with same name, inserting the new ones
            //Spring Data JPA doesnt seem to have upsert, so have to do it manually this way in java, this affects efficiency and speed
            List<User> allUsers = userRepository.findAllUsers();

            //Separate the inputUsers into to update ones and to insert new ones.
            List<User> toUpdateUsers = new ArrayList<>();
            List<User> toInsertUsers = new ArrayList<>();
            for(User inputUser : inputUsers)
            {
                //Negative salary needs to be ignored, 0 salary can proceed to be updated
                if(inputUser.getSalary() < 0.0d)
                    continue;

                boolean foundExistingUser = false;
                for (User dbUser : allUsers) {
                    if(inputUser.getName().equalsIgnoreCase(dbUser.getName()))
                    {
                        toUpdateUsers.add(inputUser);
                        foundExistingUser = true;
                        break;
                    }
                }
                if(!foundExistingUser)
                {
                    toInsertUsers.add(inputUser);
                }
            }

            userRepository.updateUsers(toUpdateUsers);
            userRepository.insertNewUsers(toInsertUsers);

            return true;
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }
}
