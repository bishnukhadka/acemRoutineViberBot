package com.acem.demo.controller;

import com.acem.demo.entity.Schedule;
import com.acem.demo.request.ScheduleRequest;
import com.acem.demo.response.Response;
import com.acem.demo.service.ScheduleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public Response get(@Validated @RequestBody ScheduleRequest scheduleRequest){
        Schedule tempSchedule = scheduleRequest.toSchedule();
        return scheduleService.get(tempSchedule);
    }

    @PostMapping
    public Response save(@Validated @RequestBody ScheduleRequest scheduleRequest){
        Schedule tempSchedule = scheduleRequest.toSchedule();
        return scheduleService.save(tempSchedule);
    }

    @PutMapping
    public Response update(@Validated @RequestBody ScheduleRequest scheduleRequest){
        Schedule tempSchedule = scheduleRequest.toSchedule();
        return scheduleService.update(tempSchedule);
    }

    @DeleteMapping
    public Response delete(@Validated @RequestBody ScheduleRequest scheduleRequest){
        Schedule tempSchedule = scheduleRequest.toSchedule();
        return scheduleService.delete(tempSchedule);
    }


}
