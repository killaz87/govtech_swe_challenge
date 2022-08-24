package com.govtech.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.govtech.bean.GetUsersResult;
import com.govtech.repository.bean.User;
import com.govtech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/govtech")
public class GovtechController {

    @Autowired
    private UserService userService;

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

        List<User> userList =  userService.getUsers(min, max, offset, limit, sort);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(GetUsersResult.builder().results(userList).build(), headers, HttpStatus.OK);
    }

    @PostMapping(upload)
    public ResponseEntity<Object> performUpload()
    {
        return null;
    }
}
