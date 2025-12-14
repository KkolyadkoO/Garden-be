package org.gardenfebackend.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthCalendarResponse {

    private Integer year;
    private Integer month;
    private List<DayTasks> days;
}

