package com.govtech.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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
}
