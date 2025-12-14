package org.gardenfebackend.dtos.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateAdviceRequest {

    private String title;

    private MultipartFile photo;

    private String description;
}

