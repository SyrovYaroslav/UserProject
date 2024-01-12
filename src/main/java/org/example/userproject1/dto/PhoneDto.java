package org.example.userproject1.dto;

import lombok.Builder;
import lombok.Data;
import org.example.userproject1.entity.User;
import org.example.userproject1.validator.Error;

import java.util.List;

@Builder
@Data
public class PhoneDto {
    private Long id;
    private String phoneNumber;
    private User user;
    private List<Error> error;
}
