package com.acem.demo.service.impl;

import com.acem.demo.constant.ResponseMessageConstant;
import com.acem.demo.entity.Lecture;
import com.acem.demo.entity.Schedule;
import com.acem.demo.repository.ScheduleRepository;
import com.acem.demo.response.LectureResponse;
import com.acem.demo.response.Response;
import com.acem.demo.response.ScheduleResponse;
import com.acem.demo.service.ScheduleService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }


    @Override
    public Response get(Schedule schedule) {
        try{
            Schedule fetchedSchedule = scheduleRepository.getByCourseAndBatchAndDayAndSection(
                    schedule.getCourse(),
                    schedule.getBatch(),
                    schedule.getDay(),
                    schedule.getSection()
            );
            ScheduleResponse scheduleResponse = mapToScheduleResponse(fetchedSchedule);
            return new Response()
                    .statusCode(HttpServletResponse.SC_OK)
                    .description(ResponseMessageConstant.Batch.FOUND)
                    .success(true)
                    .data(scheduleResponse);
        }catch (Exception ex){
            System.out.println("Exception: "+ex.getMessage());
            return new Response()
                    .statusCode(HttpServletResponse.SC_NOT_FOUND)
                    .description(ResponseMessageConstant.Batch.NOT_FOUND)
                    .success(false)
                    .data("N/A");
        }
    }

    @Override
    public Response save(Schedule schedule) {
        try{
            Schedule savedSchedule = scheduleRepository.save(schedule);
            ScheduleResponse scheduleResponse = mapToScheduleResponse(savedSchedule);
            return new Response()
                    .statusCode(HttpServletResponse.SC_OK)
                    .description(ResponseMessageConstant.Batch.SAVED)
                    .success(true)
                    .data(scheduleResponse);
        }catch (Exception ex){
            System.out.println("Exception: "+ex.getMessage());
            return new Response()
                    .statusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .description(ResponseMessageConstant.Batch.NOT_SAVED)
                    .success(false)
                    .data("N/A");
        }
    }

    @Override
    public Response update(Schedule schedule) {
        try{
            Schedule tempSchedule = scheduleRepository.getByCourseAndBatchAndDayAndSection(
                    schedule.getCourse(),
                    schedule.getBatch(),
                    schedule.getDay(),
                    schedule.getSection()
            );
            tempSchedule.setLectures(schedule.getLectures());
            Schedule updatedSchedule =
                    scheduleRepository.save(tempSchedule);
            ScheduleResponse scheduleResponse = mapToScheduleResponse(updatedSchedule);
            return new Response()
                    .statusCode(HttpServletResponse.SC_OK)
                    .description(ResponseMessageConstant.Batch.UPDATED)
                    .success(true)
                    .data(scheduleResponse);
        }catch (Exception ex){
            System.out.println("Exception: "+ex.getMessage());
            return new Response()
                    .statusCode(HttpServletResponse.SC_NOT_FOUND)
                    .description(ResponseMessageConstant.Batch.NOT_UPDATED)
                    .success(false)
                    .data("N/A");
        }
    }

    public ScheduleResponse mapToScheduleResponse(Schedule schedule){
        ScheduleResponse scheduleResponse = new ScheduleResponse();
        List<LectureResponse> lectureResponseList = new ArrayList<>();
        List<Lecture> lectures = schedule.getLectures();
        for (Lecture lecture :
                lectures) {
            lectureResponseList.add(mapToLectureResponse(lecture));
        }
        scheduleResponse
                .course(schedule.getCourse().name())
                .batch(schedule.getBatch().name())
                .section(schedule.getSection().name())
                .day(schedule.getDay().name())
                .lectures(lectureResponseList);
            return scheduleResponse;
    }

    public LectureResponse mapToLectureResponse(Lecture lecture){
        return new LectureResponse(lecture.getName(),
                    lecture.getStartTime(),
                    lecture.getEndTime(),
                    lecture.getGroup().name());
    }
}
