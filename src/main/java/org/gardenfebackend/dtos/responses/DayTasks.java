package org.gardenfebackend.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayTasks {

    private LocalDate date;
    private List<CalendarTask> tasks;
}

