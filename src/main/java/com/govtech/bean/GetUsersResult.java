package com.govtech.bean;

import com.govtech.repository.bean.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUsersResult implements Serializable {
    private List<User> results;
}
