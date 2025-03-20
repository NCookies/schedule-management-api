package xyz.ncookie.sma.repository;

import xyz.ncookie.sma.dto.response.ScheduleResponseDto;
import xyz.ncookie.sma.entity.Schedule;

import java.util.List;

public interface ScheduleRepository {

    ScheduleResponseDto saveSchedule(Schedule schedule);

    List<ScheduleResponseDto> findAllSchedules(String modifiedDate, String author);

    ScheduleResponseDto findScheduleByIdOrElseThrow(Long id);

    int updateSchedule(Long id, String task, String author, String password);

    int deleteSchedule(Long id, String password);

}
