package org.gardenfebackend.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EnumOptionResponse {

    private String value;
    private String label;
}



