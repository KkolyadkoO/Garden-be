package org.gardenfebackend.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdviceResponse {

    private UUID id;
    private String title;
    private String photoUrl;
    private String description;
}

