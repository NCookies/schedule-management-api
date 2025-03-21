package xyz.ncookie.sma.repository;

import xyz.ncookie.sma.dto.request.ScheduleRequestDto;
import xyz.ncookie.sma.dto.response.ScheduleResponseDto;

import java.util.List;

public interface ScheduleRepository {

    ScheduleResponseDto saveSchedule(ScheduleRequestDto dto);

    List<ScheduleResponseDto> findAllSchedules(String modifiedDate, Long userId);

    ScheduleResponseDto findScheduleByIdOrElseThrow(Long id);

    int updateSchedule(Long id, String task, String password);

    int deleteSchedule(Long id, String password);

}
