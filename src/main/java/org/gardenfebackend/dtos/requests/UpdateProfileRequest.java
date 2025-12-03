package org.gardenfebackend.dtos.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProfileRequest {

    private String fullName;

    private MultipartFile profilePhoto;
}


