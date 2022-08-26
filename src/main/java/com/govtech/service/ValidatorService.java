package com.govtech.service;

import com.govtech.repository.bean.User;
import com.govtech.util.CSVHelper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ValidatorService {
    public boolean validateGetUsersInput(Double min , Double max, Long offset, Long limit, String sort)
    {
        if(!"".equalsIgnoreCase(sort) && !"name".equalsIgnoreCase(sort) && !"salary".equalsIgnoreCase(sort))
        {
            return false;
        }
        return true;
    }

    @SneakyThrows
    public String validatePerformUpload(MultipartFile file) {
        return CSVHelper.csvValidationCheck(file.getInputStream());
    }
}
