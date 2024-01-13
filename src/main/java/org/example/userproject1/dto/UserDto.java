package org.example.userproject1.dto;

import lombok.Builder;
import lombok.Data;

import org.example.userproject1.validator.Error;


import java.util.List;


@Builder
@Data
public class UserDto {
    private Long id;
    private String mail;
    private String password;
    private List<Error> error;
}
