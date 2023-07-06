package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.validate.ImageNameValid;
import lombok.*;
import org.aspectj.bridge.IMessage;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String userId;

    @Size(min = 3,max = 15,message = "Invalid name..!!")
    private String name;
    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)[a-z]{2,5}$",message = "Invalid mail address")
    @Email(message = "Invalid email address..!!")
    @NotBlank(message = "Email is required..!!")
    private String email;
    @NotBlank(message = "Password is required..!!")
    private String password;
    @Size(min = 4,max = 6,message = "Invalid gender..!!")
    private String gender;
    @NotBlank(message = "Write  something about yourself..!!")
    private String about;

    //@Pattern
    //@Custom validator
    @ImageNameValid
    private String imageName;
}
