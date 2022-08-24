package com.govtech.repository.bean;

import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private String name;
    private Double salary;

    @Override
    public String toString() {
        return String.format(
                "User[name='%s', salary='%s']",
                name, salary);
    }
}