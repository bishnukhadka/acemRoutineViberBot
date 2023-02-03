package com.acem.demo.request;

import com.acem.demo.entity.Lecture;
import com.acem.demo.entity.Schedule;
import com.acem.demo.entity.enums.CourseName;
import com.acem.demo.entity.enums.DayEnum;
import com.acem.demo.entity.enums.SectionEnum;
import com.acem.demo.entity.enums.Year;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRequest {
    private Integer batch;
    private Integer course;
    private Integer section;
    private Integer day;
    private List<Lecture> lectures;

    public Schedule toSchedule(){
        Schedule tempSchedule = new Schedule();
        tempSchedule.setDay(DayEnum.values()[day]);
        tempSchedule.setBatch(Year.values()[batch]);
        tempSchedule.setCourse(CourseName.values()[course]);
        tempSchedule.setSection(SectionEnum.values()[section]);
        tempSchedule.setLectures(lectures);
        tempSchedule.setCode(generateScheduleCode(tempSchedule));

        return tempSchedule;
    }

    private String generateScheduleCode(Schedule request){
        StringBuilder stringBuilder = new StringBuilder(request.getBatch().toString());
        stringBuilder.append(request.getSection().toString());
        stringBuilder.append(request.getDay().ordinal());
        stringBuilder.append(request.getCourse().toString());
        return stringBuilder.toString();
    }
}
