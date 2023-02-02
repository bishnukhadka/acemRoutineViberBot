package com.acem.demo.response;

import com.acem.demo.entity.Lecture;
import com.acem.demo.entity.enums.SectionEnum;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponse implements Serializable {
    private String course;
    private String batch;
    private String section;
    private String day;
    private List<LectureResponse> lectures;

    public ScheduleResponse batch(String batch) {
        this.batch = batch;
        return this;
    }

    public ScheduleResponse course(String course) {
        this.course = course;
        return this;

    }

    public ScheduleResponse section(String section) {
        this.section = section;
        return this;

    }

    public ScheduleResponse day(String day) {
        this.day = day;
        return this;
    }

    public ScheduleResponse lectures(List<LectureResponse> lectures) {
        this.lectures = lectures;
        return this;
    }
}

