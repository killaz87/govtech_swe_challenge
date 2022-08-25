package com.govtech.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.govtech.bean.GetUsersResult;
import com.govtech.bean.PerformUploadResult;
import com.govtech.repository.bean.User;
import com.govtech.service.UserService;
import com.govtech.service.ValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/govtech")
public class GovtechController {

    @Autowired
    private UserService userService;

    @Autowired
    private ValidatorService validatorService;

    private static final String users = "/users";
    private static final String upload = "/upload";

    private ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping(users)
    public ResponseEntity<GetUsersResult> getUsers(@RequestParam(required = false) Double min , @RequestParam(required = false) Double max,
                                           @RequestParam(required = false) Long offset, @RequestParam(required = false) Long limit,
                                           @RequestParam(required = false) String sort)
    {
        //default values:
        if(min == null)
            min = 0.0;
        if(max == null)
            max = 4000.0;

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if(!validatorService.validateGetUsersInput(min, max, offset, limit, sort))
        {
            return new ResponseEntity<>(null, headers, HttpStatus.BAD_REQUEST);
        }

        List<User> userList =  userService.getUsers(min, max, offset, limit, sort);

        return new ResponseEntity<>(GetUsersResult.builder().results(userList).build(), headers, HttpStatus.OK);
    }

    @PostMapping(upload)
    public ResponseEntity<PerformUploadResult> performUpload(@RequestParam("file") MultipartFile file)
    {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if(!validatorService.validatePerformUpload(file))
        {
            return new ResponseEntity<>(null, headers, HttpStatus.BAD_REQUEST);
        }

        boolean isSuccessful = userService.performUpload(file);

        if(isSuccessful)
            return new ResponseEntity<>(PerformUploadResult.builder().success(1).build(), headers, HttpStatus.OK);
        else
            return new ResponseEntity<>(PerformUploadResult.builder().success(0).build(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
