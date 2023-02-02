package com.acem.demo.service;

import com.acem.demo.entity.Schedule;
import com.acem.demo.response.Response;

public interface ScheduleService {

    Response get(Schedule schedule);
    Response save(Schedule schedule);
    Response update(Schedule schedule);
    Response delete(Schedule schedule);

}
