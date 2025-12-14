package org.gardenfebackend.controllers;

import lombok.RequiredArgsConstructor;
import org.gardenfebackend.dtos.responses.CalendarTask;
import org.gardenfebackend.dtos.responses.MonthCalendarResponse;
import org.gardenfebackend.services.CalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/today")
    public ResponseEntity<List<CalendarTask>> getTodayTasks() {
        List<CalendarTask> tasks = calendarService.getTodayTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/month")
    public ResponseEntity<MonthCalendarResponse> getMonthTasks(
            @RequestParam Integer year,
            @RequestParam Integer month) {
        MonthCalendarResponse response = calendarService.getMonthTasks(year, month);
        return ResponseEntity.ok(response);
    }
}

