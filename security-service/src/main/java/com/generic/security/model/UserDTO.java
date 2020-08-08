package com.generic.security.model;

import com.generic.security.constant.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class UserDTO {

    private long id;

    private String uuId;

    @NotBlank(message = "Name is mandatory")
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    @Email(message = "Email is mandatory and should be in correct format")
    private String email;

    private int isActive;

    private int isDelete;

    private int isVerified;

    private Date createdDate;

    private Date updatedDate;

    @NotNull(message = "Role is mandatory")
    private Role role;
}
