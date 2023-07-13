package com.lcwd.electronic.store.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponse {

    private String imageName;
    private String multipartFile;
    private String message;
    private boolean success;
    private HttpStatus status;
}
