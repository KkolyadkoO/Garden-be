package org.gardenfebackend.services;

import lombok.RequiredArgsConstructor;
import org.gardenfebackend.dtos.responses.CalendarTask;
import org.gardenfebackend.dtos.responses.DayTasks;
import org.gardenfebackend.dtos.responses.MonthCalendarResponse;
import org.gardenfebackend.models.Garden;
import org.gardenfebackend.models.PlantedPlant;
import org.gardenfebackend.models.User;
import org.gardenfebackend.repositories.GardenRepository;
import org.gardenfebackend.repositories.PlantedPlantRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final PlantedPlantRepository plantedPlantRepository;
    private final GardenRepository gardenRepository;

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            return user;
        }
        throw new IllegalStateException("Пользователь не аутентифицирован");
    }

    @Transactional(readOnly = true)
    public List<CalendarTask> getTodayTasks() {
        User user = getCurrentUser();
        LocalDate today = LocalDate.now();
        return calculateTasksForDate(user, today);
    }

    @Transactional(readOnly = true)
    public MonthCalendarResponse getMonthTasks(Integer year, Integer month) {
        User user = getCurrentUser();
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Garden> gardens = gardenRepository.findByUser(user);
        List<PlantedPlant> allPlantedPlants = new ArrayList<>();
        for (Garden garden : gardens) {
            allPlantedPlants.addAll(plantedPlantRepository.findByGarden(garden));
        }

        Map<LocalDate, List<CalendarTask>> tasksByDate = new HashMap<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            tasksByDate.put(date, new ArrayList<>());
        }

        for (PlantedPlant plantedPlant : allPlantedPlants) {
            LocalDate plantedDate = plantedPlant.getPlantedDate();
            Integer wateringDays = plantedPlant.getPlant().getWateringDays();

            if (wateringDays == null || wateringDays <= 0) {
                continue;
            }

            LocalDate nextWateringDate = plantedDate;
            while (!nextWateringDate.isAfter(endDate)) {
                if (!nextWateringDate.isBefore(startDate)) {
                    CalendarTask task = createCalendarTask(plantedPlant);
                    tasksByDate.get(nextWateringDate).add(task);
                }
                nextWateringDate = nextWateringDate.plusDays(wateringDays);
            }
        }

        List<DayTasks> dayTasksList = tasksByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new DayTasks(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return new MonthCalendarResponse(year, month, dayTasksList);
    }

    private List<CalendarTask> calculateTasksForDate(User user, LocalDate date) {
        List<Garden> gardens = gardenRepository.findByUser(user);
        List<PlantedPlant> allPlantedPlants = new ArrayList<>();
        for (Garden garden : gardens) {
            allPlantedPlants.addAll(plantedPlantRepository.findByGarden(garden));
        }

        List<CalendarTask> tasks = new ArrayList<>();

        for (PlantedPlant plantedPlant : allPlantedPlants) {
            LocalDate plantedDate = plantedPlant.getPlantedDate();
            Integer wateringDays = plantedPlant.getPlant().getWateringDays();

            if (wateringDays == null || wateringDays <= 0) {
                continue;
            }

            if (isWateringDay(plantedDate, date, wateringDays)) {
                CalendarTask task = createCalendarTask(plantedPlant);
                tasks.add(task);
            }
        }

        return tasks;
    }

    private boolean isWateringDay(LocalDate plantedDate, LocalDate checkDate, Integer wateringDays) {
        if (checkDate.isBefore(plantedDate)) {
            return false;
        }

        long daysSincePlanted = java.time.temporal.ChronoUnit.DAYS.between(plantedDate, checkDate);
        return daysSincePlanted % wateringDays == 0;
    }

    private CalendarTask createCalendarTask(PlantedPlant plantedPlant) {
        Garden garden = plantedPlant.getGarden();
        return new CalendarTask(
                garden.getId(),
                garden.getName(),
                plantedPlant.getPlant().getId(),
                plantedPlant.getPlant().getName(),
                plantedPlant.getX(),
                plantedPlant.getY()
        );
    }
}

